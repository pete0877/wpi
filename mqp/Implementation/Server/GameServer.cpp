// GameServer.cpp
//

#include "stdafx.h"
#include "GameServer.h"
#include "Engine.h"
#include "ServerThreadMessage.h"
#include "StringTool.h"
#include "SimpleDateTime.h"
#include "UnevenDistributionAlgorithm.h"

GameServer::GameServer()
{	
	serversocket = NULL;
	engine = new Engine();
	engine->setDistributionAlgorithm(new UnevenDistributionAlgorithm());
	defaultEngine = true;
	shutDown = false;
	serverUp = false;
	countClients = 0;

	// Default configuration:
	configuration.setServerName("DGTE Founder 2K");
	configuration.setServerOwner("Peter Golaszewski");
	configuration.setServerDescription("This server is used for development of the DGTE MQP Project.");
	configuration.setPort(23);
	configuration.setMaxClients(100);
	configuration.setMaxClientsPerGameType(10);
	configuration.setMaxDecisionTime(60 * 15);
	configuration.setMaxGamePly(50);

	logContext = NULL;
}

GameServer::GameServer(const GameServerConfiguration& new_configuration)
{
	serversocket = NULL;
	engine = new Engine();
	engine->setDistributionAlgorithm(new UnevenDistributionAlgorithm());
	defaultEngine = true;
	shutDown = false;
	serverUp = false;
	countClients = 0;
	logContext = NULL;

	// Custom configuration:
	configuration = new_configuration;
}

GameServer::GameServer(Engine* new_engine)
{
	serversocket = NULL;
	engine = new_engine;
	defaultEngine = false;
	shutDown = false;
	serverUp = false;
	countClients = 0;
	logContext = NULL;

	// Default configuration:
	configuration.setServerName("DGTE Founder 2K");
	configuration.setServerOwner("Peter Golaszewski");
	configuration.setServerDescription("This server is used for development of the DGTE MQP Project.");
	configuration.setPort(23);
	configuration.setMaxClients(100);
	configuration.setMaxClientsPerGameType(10);
	configuration.setMaxDecisionTime(60 * 15);
	configuration.setMaxGamePly(50);
}

GameServer::GameServer(const GameServerConfiguration& new_configuration, Engine* new_engine)
{
	serversocket = NULL;
	engine = new_engine;
	defaultEngine = false;
	shutDown = false;
	serverUp = false;
	countClients = 0;
	logContext = NULL;

	// Custom configuration:
	configuration = new_configuration;
}

void GameServer::start()
{
	if (!serverUp) {			
		shutDown = false;
		// Initialize the socket:
		log("Initiating...");		
		serversocket = new CSocket();	
		if (!serversocket->Create(configuration.getPort())) {		
			delete serversocket;
			serversocket = NULL;
			log ("Cannot open server socket.");		
			return;
		}	
		if (!serversocket->Listen()) {		
			delete serversocket;
			serversocket = NULL;
			log  ("Cannot open server socket.");		
			return;
		}					

		// create listening thread:
		ServerThreadMessage* params = new ServerThreadMessage;	
		params->clientsocket		= serversocket;
		params->clientSocketHandle  = serversocket->Detach();	
		params->engine				= this->engine;
		params->jobManagers			= &this->jobManagers;
		params->currentGameTypes	= &this->currentGameTypes;
		params->jobManagersLock		= &this->jobManagersLock;
		params->configuration		= &this->configuration;
		params->logContext			= this->logContext;
		params->countClients		= &this->countClients;
		params->lockCountClients	= &this->lockCountClients;
		params->shutDown			= &this->shutDown;

		// call the connection awaiting thread.
		// note that the new thread is responsible for terminating 
		// both .. the parameters structure (params) and the server
		// socket (params->clientsocket)
		AfxBeginThread(waitForClients, (LPVOID) params);

		// mark the fact that the server is now up:
		serverUp = true;
		log ("Server started");  

	}		
	return;
}

void GameServer::stop()
{	
	// check to make sure that the server is up and not in the process of shotting down:
	if (serverUp && !shutDown) {	
		log  ("Killing all connections... ");
		// mark the fact that the server is going down:
		shutDown = true;

		// first we shut down the listener by issuing a dummy client request
		// once the listener wakes up, it will realize that the server is going
		// down and it will ignore the dummy client.
		CSocket dummySocket;
		dummySocket.Create();
		dummySocket.Connect("127.0.0.1", configuration.getPort());
		dummySocket.Close();

		// no new connections should be created from now on.
		// wait till all the running threads quit:
		int currentCount;
		do {
			Sleep(500);
			// we don't need to lock this region because we are only reading:
			currentCount = countClients;								
		} while (currentCount>0);
		// no more connections are open. 
		serversocket = NULL;
		
		serverUp= false;
		log  ("Server stopped");	
	}		
}

void GameServer::quickSend(CSocket* s, CString& t, boolean* goingDown) {		
	if (s->Send(t.GetBuffer(0), t.GetLength() + 1)==SOCKET_ERROR ) throw new CException();		
	if (*goingDown) throw new CException();
}

void GameServer::quickReceive(CSocket* s, CString& t, boolean* goingDown) {
	t = "";		
	char tmp[10];
	char delimiter = 0;
	int size;		
	do {
		if ((size=s->Receive(tmp, 1))==0) throw new CException;			
		if (GetLastError()!=0) throw new CException;			
		if ((size==1) && (tmp[0]!=delimiter)) t = t + tmp[0];
	} while (tmp[0]!=delimiter);	
	if (*goingDown) throw new CException();
}

UINT GameServer::waitForClients(LPVOID params_pointer)
{	
	// retreive all parameters passed to the thread:
	ServerThreadMessage* params = (ServerThreadMessage*) params_pointer;
	CSocket* serversocket = params->clientsocket;
	serversocket->Attach(params->clientSocketHandle);
	LogContext* logContext = params->logContext;

	ServerThreadMessage* handler_params;

	CSocket* new_clientsocket;		
	do {
		new_clientsocket = new CSocket();							
		// wait for incoming connections:
		serversocket->Accept(*new_clientsocket);
		// check if we are supposed to shut down:
		if (*(params->shutDown)==false) {
			if (serversocket->GetLastError()) {			
				delete params;
				serversocket->Close();
				delete serversocket;	
				delete new_clientsocket;
				log  (logContext, "Cannot accept connections.");			
				return (UINT)0;
			}	
			UINT tmpPort;
			CString tmpAddress;			
			new_clientsocket->GetPeerName(tmpAddress, tmpPort);
			tmpAddress = "Accepted connection from " + tmpAddress;
			log  (logContext, tmpAddress);
			handler_params = new ServerThreadMessage;
			*handler_params = *params;
			handler_params->clientsocket = new_clientsocket;
			handler_params->clientSocketHandle = new_clientsocket->Detach();		
			// create new thread:
			// the new thread will be responsible for deleting both:
			// the parameter structure and the socket.
			AfxBeginThread(handleClient, (LPVOID) handler_params);			
			// loop until the server was ordered to go down:
		}
	} while ((*(params->shutDown)==false)) ;		
	delete params;
	serversocket->Close();
	delete serversocket;	
	delete new_clientsocket;
	return (UINT)0;
}


UINT GameServer::handleClient(LPVOID params_pointer)
{	

	// retreive all parameters passed to the thread:
	ServerThreadMessage* params			= (ServerThreadMessage*) params_pointer;
	CSocket* clientsocket				= params->clientsocket;
	clientsocket->Attach(params->clientSocketHandle);
	Engine* engine						= params->engine;
	vector<JobManager*>* jobManagers		= params->jobManagers;
	vector<GameType*>* currentGameTypes	= params->currentGameTypes;
	CSemaphore* jobManagersLock			= params->jobManagersLock;
	GameServerConfiguration* configuration = params->configuration;
	LogContext* logContext				= params->logContext;
	boolean* goingDown					= params->shutDown;

	// increase the connections count:
	params->lockCountClients->Lock();
	*(params->countClients) = *(params->countClients) + 1;
	params->lockCountClients->Unlock();

	// string variable for storing log events
	CString tmpEvent;

	// get the peer info
	CString clientAddress;	
	UINT clientPort;
	clientsocket->GetPeerName(clientAddress, clientPort);

	// initiate text passing variables:
	CString text;
	char tmp[100];
	CString command;
	CString argument;

	int count;		
	try {

		// send the welcome message:	
		command = "SERVER_MESSAGE";	
		quickSend(clientsocket, command, goingDown);		
		argument = configuration->getWelcomeMessage();					
		quickSend(clientsocket, argument, goingDown);

		// wait for the encode command:
		quickReceive(clientsocket, command, goingDown);
		quickReceive(clientsocket, argument, goingDown);

		// check validity of the command:
		if ((command != "CLIENT_ENCODING") || (argument=="")) { 	
			command = "SERVER_ERROR";	
			quickSend(clientsocket, command, goingDown);		
			argument = "ERROR: Do not know client's encoding type.";
			quickSend(clientsocket, argument, goingDown);
			command = "SERVER_DISCONNECT";	
			quickSend(clientsocket, command, goingDown);
			throw new CException();
		}
	
		// fetch all game types and select those with the matching encoding:
		vector<GameType> gametypes = engine->getAllGameTypes();
		vector<GameType> choices;
		GameType gametype;	
		for (count=0; count < gametypes.size(); count++) {
			gametype = gametypes[count];
			if (gametype.getGameEncoding() == argument) {
				choices.push_back(gametype);
			}
		}
	
		// check if there were any game types matching:
		if (choices.size() ==0) {
			// send error message back:
			command = "SERVER_ERROR";	
			quickSend(clientsocket, command, goingDown);		
			argument = "ERROR: Server does not support '" + argument + "' encoding.";
			quickSend(clientsocket, argument, goingDown);
			command = "SERVER_DISCONNECT";	
			quickSend(clientsocket, command, goingDown);
			throw new CException();
		}
	
		// found at least one matching game type, message the client:
		command = "SERVER_NUMGAMETYPES";	
		quickSend(clientsocket, command, goingDown);		
		sprintf(tmp, "%d", choices.size()); 	
		argument = tmp; 	
		quickSend(clientsocket, argument, goingDown);
	
		// send the choices to the client:
		for (count = 0; count<choices.size(); count++) {
			command = "SERVER_GAMETYPE";	
			quickSend(clientsocket, command, goingDown);		
			sprintf(tmp, "%d", count); 	
			argument = tmp; 	
			quickSend(clientsocket, argument, goingDown);
			argument = choices[count].toString();
			quickSend(clientsocket, argument, goingDown);
		}
	
		// wait for the command:
		quickReceive(clientsocket, command, goingDown);
		quickReceive(clientsocket, argument, goingDown);
	
		// check validity of the command:
		if ((command != "CLIENT_GAMETYPE") || (argument=="")) {
			// send error message back:
			command = "SERVER_ERROR";	
			quickSend(clientsocket, command, goingDown);		
			argument = "ERROR: Do not know client's game type.";
			quickSend(clientsocket, argument, goingDown);
			command = "SERVER_DISCONNECT";	
			quickSend(clientsocket, command, goingDown);
			throw new CException();
		}
	
		// convert the string argument into number: 
		int choice;
		StringTool* s = new StringTool();			
		try {			
			s->set(argument);
			choice = s->toLong();			
		} catch (CException* e) {
			e->Delete();
			choice = -1;
		}
		delete s;
	
		// check if the number is within the range:
		if (choice<0 || choice>=choices.size()) {
			// send error message back:		
			command = "SERVER_ERROR";	
			quickSend(clientsocket, command, goingDown);		
			argument = "ERROR: Incorrect game type number.";
			quickSend(clientsocket, argument, goingDown);
			command = "SERVER_DISCONNECT";	
			quickSend(clientsocket, command, goingDown);
			throw new CException();
		}

		GameType clientGameType;
		clientGameType = choices[choice];

		// TODO: Check if the game type reached client limit
		// ...
	
		// wait for the command:
		quickReceive(clientsocket, command, goingDown);
		quickReceive(clientsocket, argument, goingDown);
	
		// check validity of the command:
		if ((command != "CLIENT_PLY") || (argument=="")) {
			// send error message back:
			command = "SERVER_ERROR";	
			quickSend(clientsocket, command, goingDown);		
			argument = "ERROR: Do not know desired game ply.";
			quickSend(clientsocket, argument, goingDown);
			command = "SERVER_DISCONNECT";	
			quickSend(clientsocket, command, goingDown);
			throw new CException();
		}

		// convert the argument in to integer PLY:
		s = new StringTool();	
		int PLY;
		try {			
			s->set(argument);
			PLY = s->toLong();			
		} catch (CException* e) {
			e->Delete();
			PLY = -1;
		}
		delete s;
	
		// check the ply
		
		if (PLY < 2 || PLY > configuration->getMaxGamePly()) {
			// send error message back:
			command = "SERVER_ERROR";	
			quickSend(clientsocket, command, goingDown);					
			sprintf(tmp, "%d", configuration->getMaxGamePly()); 	
			argument = tmp;
			argument = "ERROR: The game ply needs to be between 2 and " + argument + " inclusive.";
			quickSend(clientsocket, argument, goingDown);
			command = "SERVER_DISCONNECT";	
			quickSend(clientsocket, command, goingDown);
			throw new CException();
		}

		command = "SERVER_STARTGAME";	
		quickSend(clientsocket, command, goingDown);		
		
		tmpEvent = "Started game with " + clientAddress + " (" + clientGameType.getGameName() + ")";
		log(logContext, tmpEvent);	

		CString received_board;
		SimpleDateTime* dead_line = NULL;
		long WAITTIME;
		GameState board;		
		// now we go into a loop waiting for client's moves:
		do {			
			try {
				// wait for the command:
				quickReceive(clientsocket, command, goingDown);
				quickReceive(clientsocket, received_board, goingDown);			
				// check validity of the command:
				if (command != "CLIENT_GAMESTATE") {
					// send error message back:
					command = "SERVER_ERROR";	
					quickSend(clientsocket, command, goingDown);		
					argument = "ERROR: Server expected a move request.";
					quickSend(clientsocket, argument, goingDown);
					command = "SERVER_DISCONNECT";	
					quickSend(clientsocket, command, goingDown);
					throw new CException();
				};


				tmpEvent = "Received game state from " + clientAddress;
				log(logContext, tmpEvent);

				// lock the job manager for game types:
				jobManagersLock->Lock();
				
				// find the game type among current game types if it exists:
				int jobManagerLocation = -1;
				for (count=0; count<currentGameTypes->size(); count++) {
					GameType tmpGameType = (GameType) *(currentGameTypes->at(count));
					if (tmpGameType==clientGameType) {
						jobManagerLocation = count;
					}
				}
				if (jobManagerLocation<0) {
					// there is no such game type job manager running
					GameType* newGameType = new GameType(clientGameType);
					currentGameTypes->push_back(newGameType);
					JobManager* newJobManager = new JobManager();
					jobManagers->push_back(newJobManager);
					jobManagerLocation = jobManagers->size() - 1;
				}


				try {
					// we now need to evaluate the estimated wait time and send the result to the client:
					WAITTIME = engine->estimateMoveTime(clientGameType, PLY);	
				} catch (GameNotSupportedException* e_nosupport) {					
					jobManagersLock->Unlock();
					throw e_nosupport;
				} 

			
				// TODO: Check if the estimated Move Time exceeds the max limit
				// ...


				// find out the current queue length and total turn-around time:
				JobManager* tmpJobManager;
				tmpJobManager = jobManagers->at(jobManagerLocation);
				long turnAround = WAITTIME + tmpJobManager->getTotalQueueTime();

				command = "SERVER_WAITTIME";	
				quickSend(clientsocket, command, goingDown);		
				sprintf(tmp, "%d", turnAround);		
				argument = tmp; 			
				quickSend(clientsocket, argument, goingDown);			
				
				// create the dead-line and assigment specs:
				dead_line = new SimpleDateTime();				
				board.gamestate = new unsigned char[received_board.GetLength()+1];
				strcpy ((char *) board.gamestate, (char *) received_board.GetBuffer(0));								
				dead_line->add(turnAround);				

				// create the new job and insert it into the job manager:
				CSemaphore* jobSemaphore = new CSemaphore();
				jobSemaphore->Lock(); // this resents the counter to 0

				// add new job to the job manager and sleep on the new semaphore:
				int jobNumber = tmpJobManager->addJob(WAITTIME, jobSemaphore);
				jobManagersLock->Unlock();
				jobSemaphore->Lock();

				// at this point we know that the job manager will leave the semaphore alone.
				// the job has started
				delete jobSemaphore;
				jobSemaphore = NULL;

				MoveDecision result;
				try {
					// use the engine to evaluate:
					result = engine->makeMove(board, clientGameType, PLY, dead_line);
					// let the manager know that the job has been completed and clean up 
					// some variables:
					tmpJobManager->deleteJob(jobNumber);
					delete board.gamestate;
					delete dead_line;
					board.gamestate = NULL;
					dead_line = NULL;
				} catch (GameNotSupportedException* e_nosupport) {
					tmpJobManager->deleteJob(jobNumber);
					delete board.gamestate;
					delete dead_line;					
					throw e_nosupport;
				} catch (GameOverException* e_gameover) {
					tmpJobManager->deleteJob(jobNumber);
					delete board.gamestate;
					delete dead_line;
					throw e_gameover;
				}

				tmpEvent = "Sending response move to " + clientAddress;
				log(logContext, tmpEvent);

				command = "SERVER_MOVE";	
				quickSend(clientsocket, command, goingDown);			
				argument = "CONTINUE";
				if (result.gamestatus == MOVEDECISION_WIN) argument = "WIN";
				if (result.gamestatus == MOVEDECISION_LOOSE) argument = "LOOSE";
				if (result.gamestatus == MOVEDECISION_DRAW) argument = "DRAW";								
				quickSend(clientsocket, argument, goingDown);
				argument = result.move;
				quickSend(clientsocket, argument, goingDown);
				sprintf(tmp, "%d", (int) result.pctCompleted);
				argument = tmp;
				quickSend(clientsocket, argument, goingDown);
				argument = "Now server is waiting for your next move.";
				if (result.gamestatus == MOVEDECISION_WIN) argument = "Server won. Thank you.";
				if (result.gamestatus == MOVEDECISION_LOOSE) argument = "You won. Thank you.";
				if (result.gamestatus == MOVEDECISION_DRAW) argument = "Draw. Thank you.";
				quickSend(clientsocket, argument, goingDown);
				if (result.gamestatus != MOVEDECISION_CONTINUE) {
					log  (logContext, "Game ended.");	
					command = "SERVER_DISCONNECT";	
					quickSend(clientsocket, command, goingDown);
					throw new CException();	
				}				
			} catch (GameNotSupportedException* e_nosupport) {
				e_nosupport->Delete();
				// send the message to the client and close the connnection
				command = "SERVER_ERROR";	
				quickSend(clientsocket, command, goingDown);		
				argument = "ERROR: Chosen game type is not supported at the moment.";
				quickSend(clientsocket, argument, goingDown);
				command = "SERVER_DISCONNECT";	
				quickSend(clientsocket, command, goingDown);
				throw new CException();
			} catch (GameOverException* e_gameover) {
				e_gameover->Delete();
				// send the message to the client and close the connnection
				command = "SERVER_ENDGAME";	
				quickSend(clientsocket, command, goingDown);		
				argument = "The game is over.";
				quickSend(clientsocket, argument, goingDown);
				command = "SERVER_DISCONNECT";	
				quickSend(clientsocket, command, goingDown);
				log  (logContext, "   Game ended.");	
				throw new CException();
			} 
		} while (1);		
	} catch (CException* e) {
		e->Delete();
		tmpEvent = "Disconnecting client " + clientAddress;
		log(logContext, tmpEvent);
	}	

	// memState.DumpAllObjectsSince();

	// delete the current socket and passed parameters
	::Sleep(500);
	clientsocket->Close();	 
	delete clientsocket;	

	// decrease the connections count:
	params->lockCountClients->Lock();
	*(params->countClients) = *(params->countClients) - 1;
	params->lockCountClients->Unlock();
	delete params;

	return (UINT)0;
}

Engine* GameServer::getEngine()
{
    return engine;
}

void GameServer::setEngine(Engine* new_engine)
{
	if (defaultEngine) delete engine;
	engine = new_engine;
	defaultEngine = false;
}

GameServerConfiguration GameServer::getConfiguration()
{	
	return configuration;
}

void GameServer::setConfiguration(const GameServerConfiguration& new_configuration)
{
	configuration = new_configuration;
}

GameServer::~GameServer()
{
	this->logContext=NULL;
	this->stop();
	if (defaultEngine) {		
		delete (engine->getDistributionAlgorithm());
		delete engine;
	}
	jobManagersLock.Lock();
	for (int a=0; a<jobManagers.size(); a++) {
		delete jobManagers[a];
		delete currentGameTypes[a];
	}
	jobManagers.clear();
	currentGameTypes.clear();
	jobManagersLock.Unlock();
}

void GameServer::setLogContext(LogContext* new_logContext) {
	logContext = new_logContext;
	engine->setLogContext(new_logContext);
}


void GameServer::log(CString event) {
	// log function to be used by object methods .. object pointer is in []
	char instanceID[30];
	sprintf(instanceID, "[%d]", (long) this);
	if (logContext!=NULL) logContext->addEntry("GAME SERVER", instanceID, event);	
}

void GameServer::log(LogContext* new_logContext, CString event) {
	// log function to be used by threads .. thread ID is in ()
	char instanceID[30];
	sprintf(instanceID, "(%d)", (long) AfxGetThread());
	
	if (new_logContext!=NULL) new_logContext->addEntry("GAME SERVER", instanceID, event);	
}


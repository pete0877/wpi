// GameServer.h
//

#if !defined(GameServer_h)
#define GameServer_h

/*
 * GameServer Class is responsible for providing multi-threaded
 * interface to outside game clients. It provides simple text-based
 * interface using TCP sockets. GameServer Class uses the Engine
 * Class for all game computations and it stores all current game
 * sessions.
 */

#include "resource.h"
#include <afxmt.h>
#include "GameServerConfiguration.h"
#include "JobManager.h"
#include "GameType.h"
#include "Engine.h"
#include "GameServerWatcher.h"
#include "GameServerExceptions.h"

class GameServer
{
public:
	GameServer();
    GameServer(const GameServerConfiguration& new_configuration);
    GameServer(Engine* new_engine);
    GameServer(const GameServerConfiguration& new_configuration, Engine* new_engine);    
	void start();
	void stop();
    Engine* getEngine();
    void setEngine(Engine* new_engine);	
    GameServerConfiguration getConfiguration();
    void setConfiguration(const GameServerConfiguration& new_configuration);	
	void setLogContext(LogContext* new_logContext);
    ~GameServer();		

protected:
	LogContext*					logContext;
	static void					log(LogContext* new_logContext, CString event);
	void						log(CString event);
    Engine*						engine;
    vector<JobManager*>			jobManagers;
    vector<GameType*>			currentGameTypes;
    CSemaphore					jobManagersLock;
    GameServerConfiguration		configuration;
	CSocket*					serversocket;	
	boolean						defaultEngine;	
	int							countClients;
	CCriticalSection			lockCountClients;
	boolean						shutDown;
	boolean						serverUp;
	static UINT					handleClient(LPVOID arg);
	static UINT					waitForClients(LPVOID arg);
	static void					quickSend(CSocket* s, CString& t, boolean* goingDown);	
	static void					quickReceive(CSocket* s, CString& t, boolean* goingDown);	

private:


};

#endif /* GameServer_h */


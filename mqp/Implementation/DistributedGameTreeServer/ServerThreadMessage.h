///////////////////////////////////////////////////////////////////////////
// ServerThreadMessage.h
//

#if !defined(ServerThreadMessage_h)
#define ServerThreadMessage_h

// ServerThreadMessage - servers communication between threads in the
// game server class.
typedef struct tagServerThreadMessage {
	CSocket*					clientsocket;
	SOCKET						clientSocketHandle;	
	Engine*						engine;
	vector<JobManager*>*		jobManagers;
	vector<GameType*>*			currentGameTypes;
	CSemaphore*					jobManagersLock;
	GameServerConfiguration*	configuration;	
	LogContext*					logContext;		
	int*						countClients;
	CCriticalSection*			lockCountClients;
	boolean*					shutDown;
} ServerThreadMessage;


#endif /* ServerThreadMessage_h */
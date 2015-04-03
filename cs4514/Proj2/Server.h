/////////////////////////////////////////////////////////////////////////////
// Server.h
//
//	Version 1.00
//
//	This is the server program.
//
/////////////////////////////////////////////////////////////////////////////

#ifndef SERVER_H
#define SERVER_H

#include "Common.h"
#include "FileTransferProtocol.h"
#include "DLLSocket.h"
#include "DLLServerSocket.h"
#include "Socket.h"

#include <iostream.h>
#include <stdlib.h>

/////////////////////////////////////////////////////////////////////////////
// Class Server

class Server
{
	ServerSocket *m_server;	// ftp's server socket

public:
	// Constructor.
	Server(int argc, String argv[]);

	// Deconstructor.
	~Server();

	// Handles requests from clients.
	void run();
};

#endif // END OF SERVER.H ///////////////////////////////////////////////////

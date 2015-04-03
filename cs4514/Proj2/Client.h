/////////////////////////////////////////////////////////////////////////////
// Client.h
//
//	Version 1.00
//
//	This is the client program.
//
/////////////////////////////////////////////////////////////////////////////

#ifndef CLIENT_H
#define CLIENT_H

#include "Common.h"
#include "FileTransferProtocol.h"
#include "DLLSocket.h"
#include "Socket.h"

#include <iostream.h>
#include <stdlib.h>

/////////////////////////////////////////////////////////////////////////////
// Class Client

class Client
{
	Command m_cmd;
	String m_host;
	String m_inFile;
	String m_outFile;
	Socket *m_socket;

public:
	// Constructor.
	Client(int argc, String argv[]);

	// Deconstructor.
	~Client();

	// Processes request.
	void run();
};

#endif // END OF CLIENT.H ///////////////////////////////////////////////////

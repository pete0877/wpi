/////////////////////////////////////////////////////////////////////////////
// Server.cpp
//
//	Version 1.10
//
//	This is the Server program.
//
//		12.09.98 - crc_errorpct added to the command line
//
/////////////////////////////////////////////////////////////////////////////

#include "Server.h"

/////////////////////////////////////////////////////////////////////////////
// The one and only Server object.

Server *server;

/////////////////////////////////////////////////////////////////////////////
// Main - Server's Main Entry Point

void main(int argc, String argv[])
{
	try {
		cout << "Starting Server..." << endl;
		server = new Server(argc, argv);

		server->run();

		cout << "Shuting down Server..." << endl;
		delete server;
	}
	catch (WrongArguments)
	{
		cout << "Wrong arguments specified." << endl;
		cout << "Usage:" << endl << "\tserver errorpct crc_errorpct" << endl;
		if (server)
			delete server;
	}
	catch (FileException)
	{
		cout << "File exception caught." << endl;
		if (server)
			delete server;
	}
	catch (IOException)
	{
		cout << "I/O exception caught." << endl;
		if (server)
			delete server;
	}
	catch (InitException)
	{
		cout << "Initialization error has occured. Exiting..." << endl;
		if (server)
			delete server;
	}
}

/////////////////////////////////////////////////////////////////////////////
// Server Implementation

//
// Server() - Constructor.
//

Server::Server(int argc, String argv[])
{
	#ifdef _DEBUG
		cout << "Server: Constructor called.\n";
	#endif

	int errorpct;
	int crc_errorpct;

	if (argc != 3)
		throw WrongArguments();

	errorpct = atoi(argv[1]);
	crc_errorpct = atoi(argv[2]);

	// Initialize os socket layer.
	#ifdef WIN32
		WSADATA wsaData;
		if(WSAStartup(MAKEWORD(2, 2), &wsaData) != 0)
			throw IOException();
	#endif

	try {
		m_server = new DLLServerSocket(TRANSFER_PORT, errorpct, crc_errorpct);
	}
	catch (IOException) {
		cout << "Server: Couldn't create server socket.\n";
		throw InitException();
	}
}

//
// ~Server() - Deconstructor
//

Server::~Server()
{
	#ifdef _DEBUG
		cout << "Server: Deconstructor called.\n";
	#endif

	try {
		if (m_server)
			delete m_server;
	}
	catch (IOException)
	{
		cout << "Server: Couldn't close server socket.\n";
	}

	// Close down the os socket layer.
	#ifdef WIN32
		WSACleanup();
	#endif
}

//
// run() -
//

void Server::run()
{
	Socket *client = m_server->accept();

	Command cmd;
	char	file[64];
	FileTransferProtocol session(client);

	session.getCommand(cmd, (byte *) file, sizeof(file));

	switch(cmd)
	{
		case cmdGet:
			try {
				session.sendFile(file);
			}
			catch (FileException) {
				cout << "Error has occured while sending '" << file << "'. " << endl;
			}
			break;
		case cmdPut:
			try {
				session.recvFile(file);
			}
			catch (FileException) {
				cout << "Error has occured while receiving '" << file << "'. " << endl;
			}
			break;
		default:
			cout << "Unknown command received from the client." << endl;
	}

	delete client;
}

// END OF SERVER.CPP ////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
// Client.cpp
//
//	Version 1.10
//
//	This is the client program.
//
//		12.09.98 - crc_errorpct added to the command line
//
/////////////////////////////////////////////////////////////////////////////

#include "Client.h"

/////////////////////////////////////////////////////////////////////////////
// The one and only Client object.

Client *client;

/////////////////////////////////////////////////////////////////////////////
// Main - Client's Main Entry Point

void main(int argc, String argv[])
{
	try {
		cout << "Starting Client..." << endl;
		client = new Client(argc, argv);

		client->run();

		cout << "Shuting down Client..." << endl;
		delete client;
	}
	catch (WrongArguments)
	{
		cout << "Wrong arguments specified." << endl;
		cout << "Usage:" << endl
				<< "\tclient errorpct crc_errorpct (put/get) inputfile [outputfile] [host]" << endl;
		if (client)
			delete client;
	}
	catch (FileException)
	{
		cout << "File exception caught." << endl;
		if (client)
			delete client;
	}
	catch (IOException)
	{
		cout << "I/O exception caught." << endl;
		if (client)
			delete client;
	}
	catch (InitException)
	{
		cout << "Initialization error has occured. Exiting..." << endl;
		if (client)
			delete client;
	}

}


/////////////////////////////////////////////////////////////////////////////
// Client Implementation

//
// Client() - Constructor.
//

Client::Client(int argc, String argv[])
{
	#ifdef _DEBUG
		cout << "Client: Constructor called.\n";
	#endif

	int errorpct;
	int crc_errorpct;

	if ((argc < 5) || (argc > 7))
		throw WrongArguments();

	errorpct = atoi(argv[1]);
	crc_errorpct = atoi(argv[2]);

	m_inFile  = argv[4];
	m_outFile = argv[5];
	if (m_outFile == NULL)
		m_outFile = m_inFile;

	if (argc == 7)
		m_host = argv[6];
	else
		m_host = HOST;

	if (!strcmp("get", argv[3]))
		m_cmd = cmdGet;
	else if (!strcmp("put", argv[3]))
		m_cmd = cmdPut;
	else
		throw WrongArguments();

	// Initialize os socket layer.
	#ifdef WIN32
		WSADATA wsaData;
		if(WSAStartup(MAKEWORD(2, 2), &wsaData) != 0)
			throw IOException();
	#endif

	try {
		m_socket = new DLLSocket(m_host, TRANSFER_PORT, errorpct, crc_errorpct);
	}
	catch (IOException) {
		cout << "Client: Couldn't create client socket.\n";
		throw InitException();
	}
}

//
// ~Client() - Deconstructor
//

Client::~Client()
{
	#ifdef _DEBUG
		cout << "Client: Deconstructor called.\n";
	#endif

	// Close down and free all internal objects.
	try {
		if (m_socket)
			delete m_socket;
	}
	catch (IOException)
	{
		cout << "Client: Couldn't close client socket.\n";
	}

	// Close down the os socket layer.
	#ifdef WIN32
		WSACleanup();
	#endif
}

//
// run() -
//

void Client::run()
{
	FileTransferProtocol session(m_socket);

	switch(m_cmd)
	{
		case cmdGet:
			try {
				session.putCommand(cmdGet, (byte *) m_inFile, strlen(m_inFile) + 1);
				session.recvFile(m_outFile);
			}
			catch (FileException)
			{
				cout << "Error has occured while downloading '" << m_inFile << "'. " << endl;
			}
			break;
		case cmdPut:
			try {
				session.putCommand(cmdPut, (byte *) m_outFile, strlen(m_outFile) + 1);
				session.sendFile(m_inFile);
			}
			catch (FileException)
			{
				cout << "Error has occured while uploading '" << m_inFile << "'. " << endl;
			}
			break;
	}
}

// END OF CLIENT.CPP ////////////////////////////////////////////////////////
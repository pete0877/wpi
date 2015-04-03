/////////////////////////////////////////////////////////////////////////////
// FileTransferProtocol.h
//
//	Version 1.00
//
//	This class implements File Transfer Protocol.
//
/////////////////////////////////////////////////////////////////////////////

#ifndef FILETRANSFERPROTOCOL_H
#define FILETRANSFERPROTOCOL_H

#include "Common.h"
#include "Socket.h"

#include "fstream.h"
#include "iostream.h"

/////////////////////////////////////////////////////////////////////////////
// Definitions

struct Message {
	Command	cmd; 			 // What's being done
	int		size;			 // Data size
	byte	data[MESSAGE_DATA_SIZE]; // Data / filename / error message
};

/////////////////////////////////////////////////////////////////////////////
// Class FileTransferProtocol

class FileTransferProtocol
{
	Socket *m_socket;

public:
	// Constructor.
	FileTransferProtocol(Socket *socket);

	// Deconstructor.
	~FileTransferProtocol();

	// Send out a file.
	void sendFile(String file);

	// Receive a file.
	void recvFile(String file);

	// Sends out a command.
	void getCommand (Command &cmd, byte data[] = NULL, int size = 0);

	// Receives a command,
	void putCommand (Command cmd, byte data[] = NULL, int size = 0);

	//
	void recvMessage(Message &msg);

	//
	void sendMessage(Message &msg);

private:
	void bcopy (byte dest[], byte src[], int length);
};

#endif // END OF FILETRANSFERPROTOCOL.H /////////////////////////////////////

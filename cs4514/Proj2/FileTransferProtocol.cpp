/////////////////////////////////////////////////////////////////////////////
// FileTransferProtocol.cpp
//
//	Version 1.00
//
//	This class implements File Transfer Protocol.
//
/////////////////////////////////////////////////////////////////////////////

#include "FileTransferProtocol.h"

/////////////////////////////////////////////////////////////////////////////
// FileTransferProtocol Implementation

FileTransferProtocol::FileTransferProtocol(Socket *socket)
{
	m_socket = socket;
}

FileTransferProtocol::~FileTransferProtocol()
{
	;
}

void FileTransferProtocol::recvFile(String filename)
{
	cout << "FTP: Receiving '" << filename << "'..." << endl;

	int			count = 0, total = 0;
	byte		buffer[MESSAGE_DATA_SIZE];
	Message		msg;
	ofstream	file(filename);

	// Check whether a file was successfully opened.

	if (!file) {
		cout << "FTP: Unable to open '" << filename << "' for writing." << endl;
		throw FileException();
	}

	// Receive the first message.

	recvMessage(msg);

	// While the message contains data, write it out to
	// the file.

	while (msg.cmd == cmdData) {
		#ifdef _DEBUG
			cout << "FTP: \t" << msg.size << " bytes received." << endl;
		#endif
		total += count = msg.size;
		bcopy (buffer, msg.data, count);
		file.write(buffer, count);
		recvMessage(msg);
	}

	// A successful transfer is terminated by a message
	// containing cmdEnd.

	if (msg.cmd != cmdEnd)
		throw FileException();

	cout << "FTP: '" << filename << "' received successfully. " << endl;
	cout << "FTP: Total of " << total << " bytes received." << endl;

	file.close();
}

void FileTransferProtocol::sendFile(String filename)
{
	cout << "FTP: Sending '" << filename << "'..." << endl;

	int			count = 0, total = 0;
	byte		buffer[MESSAGE_DATA_SIZE];
	Message		msg;
	ifstream	file(filename);

	// Check whether a file was successfully created.

	if (!file) {
		cout << "FTP: Unable to open '" << filename << "' for reading." << endl;
		msg.cmd = cmdErr;
		sendMessage(msg);
		throw FileException();
	}

	// Send the file in chunks of data.

	while (file.eof() == 0) {
		file.read(buffer, sizeof(buffer));
		msg.cmd = cmdData;
		total += msg.size = file.gcount();
		bcopy (msg.data, buffer, msg.size);
		sendMessage(msg);
		#ifdef _DEBUG
			cout << "FTP: \t" << msg.size << " bytes sent." << endl;
		#endif
	}

	// Acknowledge successful completion of a transfer by
	// sending cmdEnd.

	msg.cmd = cmdEnd;
	sendMessage(msg);

	cout << "FTP: '" << filename << "' transfered successfully. " << endl;
	cout << "FTP: Total of " << total << " bytes sent." << endl;

	file.close();
}

void FileTransferProtocol::getCommand (Command &cmd, byte data[], int size)
{
	Message msg;

	recvMessage(msg);

	cmd = msg.cmd;
	size = (msg.size > size) ? size : msg.size;
	bcopy(data, msg.data, size);
}

void FileTransferProtocol::putCommand (Command cmd, byte data[], int size)
{
	Message msg;

	msg.cmd = cmd;
	msg.size = (size > sizeof(msg.data)) ? sizeof(msg.data) : size;
	bcopy(msg.data, data, msg.size);

	sendMessage(msg);
}

void FileTransferProtocol::recvMessage (Message &msg)
{
	m_socket->recv((void *) &msg, sizeof(Message));
}

void FileTransferProtocol::sendMessage (Message &msg)
{
	m_socket->send((void *) &msg, sizeof(Message));
}

void FileTransferProtocol::bcopy (byte dest[], byte src[], int length)
{
	while (length--) *dest++ = *src++;
}

// END OF FILETRANSFERPROTOCOL.H ////////////////////////////////////////////

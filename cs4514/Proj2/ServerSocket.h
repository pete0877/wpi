/////////////////////////////////////////////////////////////////////////////
// ServerSocket.h
//
//	Version 1.00
//
//	This class implements server sockets.
//
/////////////////////////////////////////////////////////////////////////////

#ifndef SERVERSOCKET_H
#define SERVERSOCKET_H

#include "Common.h"
#include "Socket.h"

/////////////////////////////////////////////////////////////////////////////
// Class ServerSocket

class ServerSocket
{
protected:
	SOCKET m_socket;	// socket descriptor

public:
	// Creates a server socket and binds it to the specified local port
	// number.
	ServerSocket(int port = 0, int backlog = 5);

	//  Deconstructor.
	virtual ~ServerSocket();

	// Closes this socket.
	virtual void close();

	// Listens for a connection to be made to this socket and accepts it.
	virtual Socket *accept();

	// Returns the port on which this socket is listening.
	int getPort();

	// Returns the local address of this server socket.
	int	getIPAddress();

	// Returns the socket address structure of this server socket.
	sockaddr_in *getSocketAddress();
};

#endif // END OF SERVERSOCKET.H /////////////////////////////////////////////


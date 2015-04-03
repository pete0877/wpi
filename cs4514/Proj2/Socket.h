/////////////////////////////////////////////////////////////////////////////
// Socket.h
//
//	Version 1.00
//
//	This class implements client sockets. A socket is an endpoint for
//  communication between two machines.
//
/////////////////////////////////////////////////////////////////////////////

#ifndef SOCKET_H
#define SOCKET_H

#include "Common.h"

/////////////////////////////////////////////////////////////////////////////
// Class Socket

class Socket
{
protected:
	SOCKET m_socket;	// socket descriptor

public:
	// Initializes this object with the given socket descriptor.
	Socket(SOCKET socket);

	// Creates a socket and connects it to the specified remote address on
	// the specified remote port.
	Socket(sockaddr_in &address);

	// Creates a socket and connects it to the specified port number
	// on the named host.
	Socket(const char *hostname, int port, int type = SOCK_STREAM);

	//  Deconstructor.
	virtual ~Socket();

	// Closes this socket.
	virtual void close();

	// Receives data from the socket and stores it into the specified buffer
	virtual void recv(void *buffer, int length);

	// Sends data to the socket from the specified buffer.
	virtual void send(void *buffer, int length);

	// Returns the socket descriptor.
	SOCKET getSocket() { return m_socket; }

	// Returns the name of the host to which this socket is connected.
	char *getINetName();
};

#endif // END OF SOCKET.H ///////////////////////////////////////////////////

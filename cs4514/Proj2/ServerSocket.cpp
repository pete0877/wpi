/////////////////////////////////////////////////////////////////////////////
// ServerSocket.cpp
//
//	Version 1.00
//
//	This class implements server sockets.
//
/////////////////////////////////////////////////////////////////////////////

#include "ServerSocket.h"

/////////////////////////////////////////////////////////////////////////////
// ServerSocket Implementation

ServerSocket::ServerSocket(int port, int backlog)
{
	#ifdef _DEBUG
		cout << "ServerSocket: Constructor called.\n";
	#endif

	sockaddr_in address;

	// Create a TCP socket.
	if ((m_socket = ::socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET)
		throw IOException();

	address.sin_family = AF_INET;
	address.sin_port = port;
	address.sin_addr.s_addr = INADDR_ANY;

	// Bind the socker to an unused port.
	if (::bind(m_socket, (struct sockaddr *) &address, sizeof(address)) == SOCKET_ERROR)
		throw IOException();

	// Sets the backlog that this socket will accept.
	::listen(m_socket, backlog);
}

ServerSocket::~ServerSocket()
{
	#ifdef _DEBUG
		cout << "ServerSocket: Deconstructor called.\n";
	#endif

	if (m_socket != INVALID_SOCKET)
		close();
}

void ServerSocket::close()
{
	#ifdef _DEBUG
		cout << "ServerSocket: close() called.\n";
	#endif

	if (::close(m_socket) == SOCKET_ERROR)
		throw IOException();
	else {
		#ifdef _DEBUG
			cout << "ServerSocket: close() successful.\n";
		#endif
	}

}

Socket *ServerSocket::accept()
{
	int socket;
	sockaddr_in address;
	int len = sizeof(address);

	if((socket = ::accept(m_socket, (struct sockaddr *) &address, &len)) == SOCKET_ERROR)
		throw IOException();

	return new Socket(socket);
}

int ServerSocket::getPort()
{
	sockaddr_in address;
	int len = sizeof(address);

	if (getsockname(m_socket, (struct sockaddr *) &address, &len) == SOCKET_ERROR)
		return -1;

	return address.sin_port;
}

int ServerSocket::getIPAddress()
{
	char host[64];
	hostent *ph;
	sockaddr_in address;

	if (gethostname(host, sizeof(host)) == SOCKET_ERROR)
		return -1;

	if ((ph = gethostbyname(host)) == NULL)
		return -1;

	bcopy(ph->h_addr, (char *) &address.sin_addr, ph->h_length);

	return address.sin_addr.s_addr;
}

sockaddr_in *ServerSocket::getSocketAddress()
{
	sockaddr_in *address = new sockaddr_in;

	address->sin_family = AF_INET;
	address->sin_port = getPort();
	address->sin_addr.s_addr = getIPAddress();

	return address;
}

// END OF SERVERSOCKET.CPP //////////////////////////////////////////////////
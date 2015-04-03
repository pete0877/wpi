/////////////////////////////////////////////////////////////////////////////
// Socket.cpp
//
// 	Version 1.00
//
//	This class implements client sockets. A socket is an endpoint for
//  communication between two machines.
//
/////////////////////////////////////////////////////////////////////////////

#include "Socket.h"

/////////////////////////////////////////////////////////////////////////////
// Socket Implementation

Socket::Socket(SOCKET socket)
{
	#ifdef _DEBUG
		cout << "Socket: Constructor called.\n";
	#endif

	// Use the specified socket descriptor.
	if ((m_socket = socket) == INVALID_SOCKET)
		throw IOException();
}

Socket::Socket(sockaddr_in &address)
{
	#ifdef _DEBUG
		cout << "Socket: Constructor called.\n";
	#endif

	// Create a TCP socket
	if ((m_socket = ::socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET)
		throw IOException();

	// Connect to the remote address
	if (::connect(m_socket, (struct sockaddr *) &address, sizeof(address)) == SOCKET_ERROR)
		throw IOException();
}

Socket::Socket(const char *hostname, int port, int type)
{
	#ifdef _DEBUG
		cout << "Socket: Constructor called.\n";
	#endif

	sockaddr_in address;

	// Create a TCP socket
	if ((m_socket = ::socket(AF_INET, type, 0)) == INVALID_SOCKET)
		throw IOException();


	// Get the IP address of the host
	hostent *host;

	if ((host = gethostbyname(hostname)) == NULL) {
		throw IOException();
	} else {
		address.sin_family = host->h_addrtype;
		bcopy(host->h_addr, (char *) &address.sin_addr, host->h_length);
	}

	address.sin_port = port;

	// Connect to the remote address
	if (::connect(m_socket, (struct sockaddr *) &address, sizeof(address)) == SOCKET_ERROR)
		throw IOException();
}

Socket::~Socket()
{
	#ifdef _DEBUG
		cout << "Socket: Deconstructor called.\n";
	#endif

	if (m_socket != INVALID_SOCKET)
		close();
}

void Socket::close()
{
	#ifdef _DEBUG
		cout << "Socket: close() called.\n";
	#endif

	if (::close(m_socket) == SOCKET_ERROR)
		throw IOException();
	else {
		#ifdef _DEBUG
			cout << "Socket: close() successful.\n";
		#endif
	}
}

void Socket::recv(void *buffer, int length)
{
	if (::recv(m_socket, (char *) buffer, length, 0) == SOCKET_ERROR)
		throw IOException();
}

void Socket::send(void *buffer, int length)
{
	if (::send(m_socket, (char *) buffer, length, 0) == SOCKET_ERROR)
		throw IOException();
}

char *Socket::getINetName()
{
	hostent *ph;
	sockaddr_in address;
	int len = sizeof(address);

	if (getpeername(m_socket, (struct sockaddr *) &address, &len) == SOCKET_ERROR)
		return NULL;

	if ((ph = gethostbyaddr((char *) &address.sin_addr, sizeof(address.sin_addr), 0)) == NULL)
		return NULL;

	return ph->h_name;
}

// END OF SOCKET.CPP ////////////////////////////////////////////////////////
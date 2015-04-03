/////////////////////////////////////////////////////////////////////////////
// DLLServerSocket.h
//
//	Version 1.10
//
//	This class implements server sockets.
//
//		12.09.98 - m_crcerrorpct member variable added
//
/////////////////////////////////////////////////////////////////////////////

#ifndef DLLSERVERSOCKET_H
#define DLLSERVERSOCKET_H

#include "Common.h"
#include "DLLSocket.h"
#include "ServerSocket.h"

/////////////////////////////////////////////////////////////////////////////
// Class DLLServerSocket

class DLLServerSocket : public ServerSocket
{
	int m_errorpct;
	int m_crcerrorpct;

public:
	DLLServerSocket(int port, int errorpct, int crc_errorpct) : ServerSocket(port)
	{
		#ifdef _DEBUG
			cout << "DLLServerSocket: Constructor called.\n";
		#endif

		m_errorpct = errorpct;
		m_crcerrorpct = crc_errorpct;
	}

	~DLLServerSocket()
	{
		#ifdef _DEBUG
			cout << "DLLServerSocket: Deconstructor called.\n";
		#endif
	}

	Socket *accept()
	{
		#ifdef _DEBUG
			cout << "DLLServerSocket: accept() called.\n";
		#endif

		Socket *socket = ServerSocket::accept();

		return new DLLSocket(socket, m_errorpct, m_crcerrorpct);
	}
};

#endif // END OF DDLSERVERSOCKET.H //////////////////////////////////////////

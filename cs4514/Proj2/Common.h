/////////////////////////////////////////////////////////////////////////////
// Common.h
//
//	Version 1.00
//
//	This file contains commonly used definitions.
//
/////////////////////////////////////////////////////////////////////////////

#ifndef COMMON_H
#define COMMON_H

// #define _DEBUG 1
#include <iostream.h>

/////////////////////////////////////////////////////////////////////////////
// Constants

const int		MESSAGE_DATA_SIZE = 100;
#define	HOST  "localhost"
const int		TRANSFER_PORT = 9678;

/////////////////////////////////////////////////////////////////////////////
// Exceptions

class FileException {};
class IOException {};
class WrongArguments {};
class InitException {};

/////////////////////////////////////////////////////////////////////////////
// Type Definitions

// byte
typedef unsigned char byte;

// String
typedef char * String;

// Command
enum Command {
	cmdGet,
	cmdPut,
	cmdData,
	cmdEnd,
	cmdErr,
};

/////////////////////////////////////////////////////////////////////////////
// Win32 Section

#ifdef WIN32

#include <io.h>
#include <winsock2.h>
#include <process.h>

extern void bcopy(char *, char*, int);
extern int close(SOCKET s);

/////////////////////////////////////////////////////////////////////////////
// Unix Section

#else

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <netdb.h>
#include <ctype.h>
#include <unistd.h>
#include <strings.h>
#include <pwd.h>

#define SOCKET			int
#define INVALID_SOCKET	-1
#define SOCKET_ERROR	-1
#define __cdecl

#endif // _WIN32

#endif // COMMON_H /////////////////////////////////////////////////////////

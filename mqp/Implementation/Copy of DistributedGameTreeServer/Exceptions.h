/////////////////////////////////////////////////////////////////////////////
//
// File Name:	Exceptions.h
// Description:	Common exception definitions.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef EXCEPTIONS_INCLUDED
#define EXCEPTIONS_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

/////////////////////////////////////////////////////////////////////////////
// Common exception definitions.

class Exception {
public:
	Exception() { m_message = _T(""); }
	Exception(CString s) { m_message = s; }
	virtual CString getMessage() { return m_message; }
protected:
	CString m_message;
};

class ConnectionException : public Exception {
public:
	ConnectionException() : Exception() {}
	ConnectionException(CString s) : Exception(s) {}
};

class PluginNotFoundException : public Exception {
public:
	PluginNotFoundException() : Exception() {}
	PluginNotFoundException(CString s) : Exception(s) {}
};

class InvalidPluginException : public Exception {
public:
	InvalidPluginException() : Exception() {}
	InvalidPluginException(CString s) : Exception(s) {}
};

/////////////////////////////////////////////////////////////////////////////

#endif // EXCEPTIONS_INCLUDED

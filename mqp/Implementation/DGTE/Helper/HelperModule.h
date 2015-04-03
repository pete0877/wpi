/////////////////////////////////////////////////////////////////////////////
//
// File Name:	HelperModule.h
// Description:	Interface of the HelperModule class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef HELPERMODULE_INCLUDED
#define HELPERMODULE_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

/////////////////////////////////////////////////////////////////////////////
// Class HelperModule

class HelperModule : public CComModule
{
public:
	LONG Unlock();
	LONG Lock();
	LPCTSTR FindOneOf(LPCTSTR p1, LPCTSTR p2);
	DWORD dwThreadID;
};

extern HelperModule _Module;

/////////////////////////////////////////////////////////////////////////////

#endif // HELPERMODULE_INCLUDED


/////////////////////////////////////////////////////////////////////////////
//
// File Name:	HelperModule.cpp
// Description:	Defines the class behaviors for the application.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#include "StdAfx.h"
#include "HelperModule.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// The one and only HelperModule object

HelperModule _Module;

/////////////////////////////////////////////////////////////////////////////
// HelperModule implementation

LONG HelperModule::Unlock()
{
	AfxOleUnlockApp();
	return 0;
}

LONG HelperModule::Lock()
{
	AfxOleLockApp();
	return 1;
}

LPCTSTR HelperModule::FindOneOf(LPCTSTR p1, LPCTSTR p2)
{
	while (*p1 != NULL)
	{
		LPCTSTR p = p2;
		while (*p != NULL)
		{
			if (*p1 == *p)
				return CharNext(p1);
			p = CharNext(p);
		}
		p1++;
	}
	return NULL;
}

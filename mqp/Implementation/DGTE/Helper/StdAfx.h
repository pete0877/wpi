/////////////////////////////////////////////////////////////////////////////
//
// File Name:	StdAfx.h
// Description:	Include file for standard system include files.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef STDAFX_INCLUDED
#define STDAFX_INCLUDED

/////////////////////////////////////////////////////////////////////////////
// Common includes.

#define VC_EXTRALEAN		// Exclude rarely-used stuff from Windows headers
#define _WIN32_DCOM

#include <afxwin.h>         // MFC core and standard components
#include <afxext.h>         // MFC extensions
#include <afxdtctl.h>		// MFC support for Internet Explorer 4 Common Controls
#ifndef _AFX_NO_AFXCMN_SUPPORT
	#include <afxcmn.h>		// MFC support for Windows Common Controls
#endif // _AFX_NO_AFXCMN_SUPPORT
#define	WM_ICON_NOTIFY			WM_USER+10
#define _ATL_APARTMENT_THREADED
#include <atlbase.h>
#include "HelperModule.h"
#include <atlcom.h>

//{{AFX_INSERT_LOCATION}}

/////////////////////////////////////////////////////////////////////////////

#endif // STDAFX_INCLUDED

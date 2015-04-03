/////////////////////////////////////////////////////////////////////////////
//
// File Name:	Properties.h
// Description:	Interface of the Properties class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef PROPERTIES_INCLUDED
#define PROPERTIES_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "PropertyPages.h"
#include "Resource.h"

/////////////////////////////////////////////////////////////////////////////
// Properties sheet

class Properties : public CPropertySheet
{
	DECLARE_DYNAMIC(Properties)

// Construction
public:
	Properties(CWnd* pWndParent = NULL);

// Attributes
public:
	GeneralPage m_Page1;
	PluginsPage m_Page2;

// Operations
public:

// Overrides
public:
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(Properties)
	//}}AFX_VIRTUAL

// Generated message map functions
protected:
	//{{AFX_MSG(Properties)
		// NOTE - the ClassWizard will add and remove member functions here.
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

#endif	// PROPERTIES_INCLUDED

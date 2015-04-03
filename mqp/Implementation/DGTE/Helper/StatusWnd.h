/////////////////////////////////////////////////////////////////////////////
//
// File Name:	HelperApp.h
// Description:	Interface of the StatusWnd class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef MAINFRAME_INCLUDED
#define MAINFRAME_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "SystemTray.h"

/////////////////////////////////////////////////////////////////////////////
// Class StatusWnd

class StatusWnd : public CFrameWnd
{
	DECLARE_DYNCREATE(StatusWnd)

// Construction
public:
	StatusWnd();

// Attributes
public:

// Operations
public:

// Overridables
public:

// Implementation
public:

protected:
	CSystemTray m_TrayIcon;
	
	void OnShowStatus();
	void OnRegister();
	void OnAbout();
	void OnProperties();
	void OnClose();
	LONG OnTrayNotification(UINT wParam, LONG lParam);

// Overrides
protected:
	//{{AFX_VIRTUAL(StatusWnd)
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	//}}AFX_VIRTUAL

// Generated message map functions
protected:
	//{{AFX_MSG(StatusWnd)
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnUpdateHelperRegister(CCmdUI* pCmdUI);
	//}}AFX_MSG
	
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

#endif // MAINFRAME_INCLUDED

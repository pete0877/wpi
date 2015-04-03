/////////////////////////////////////////////////////////////////////////////
//
// File Name:	HelperApp.h
// Description:	Interface of the HelperApp class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef HELPERAPP_INCLUDED
#define HELPERAPP_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "Resource.h"
#include "Plugin.h"
#include "StatusDlg.h"
#include "StatusWnd.h"	// Added by ClassView

/////////////////////////////////////////////////////////////////////////////
// Class HelperApp

class HelperApp : public CWinApp
{
// Construction
public:
	HelperApp();

// Attributes
public:
	BOOL m_autoRegister;
	CString m_pluginName;
	CString m_serverAddress;

// Operations
public:
	void Register();
	void Unregister();
	bool IsRegistered();
	Plugin * GetPlugin();
	void SetPlugin(CString pluginName);
	void LoadSettings();
	void SaveSettings();
	void ShowStatus();
	void LogMessage(CString message);

// Overridables
public:

// Implementation
public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);

protected:
	bool m_registered;
	CString m_helperAddress;
	Plugin *m_plugin;
	StatusDlg *m_status;
	StatusWnd *m_window;

// Overrides
public:
	//{{AFX_VIRTUAL(CMyClass)
	virtual BOOL InitInstance();
	virtual int ExitInstance();
	//}}AFX_VIRTUAL

// Generated message map functions
protected:
	//{{AFX_MSG(HelperApp)
	//}}AFX_MSG

	DECLARE_MESSAGE_MAP()
private:
	BOOL m_bATLInited;
private:
	BOOL InitATL();
};

//{{AFX_INSERT_LOCATION}}

extern HelperApp theApp;

/////////////////////////////////////////////////////////////////////////////

#endif // HELPERAPP_INCLUDED

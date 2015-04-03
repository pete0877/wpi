/////////////////////////////////////////////////////////////////////////////
//
// File Name:	StatusWnd.cpp
// Description:	Defines the class behaviors for the application.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#include "StdAfx.h"
#include "AboutDlg.h"
#include "Exceptions.h"
#include "Helper.h"
#include "Properties.h"
#include "Resource.h"
#include "Splash.h"
#include "StatusWnd.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// StatusWnd message map

IMPLEMENT_DYNCREATE(StatusWnd, CFrameWnd)

BEGIN_MESSAGE_MAP(StatusWnd, CFrameWnd)
	//{{AFX_MSG_MAP(StatusWnd)
	ON_WM_CREATE()
	ON_COMMAND(ID_HELPER_SHOWSTATUS, OnShowStatus)
	ON_COMMAND(ID_HELPER_REGISTER, OnRegister)
	ON_COMMAND(ID_HELPER_ABOUT, OnAbout)
	ON_COMMAND(ID_HELPER_PROPERTIES, OnProperties)
	ON_COMMAND(ID_HELPER_CLOSE, OnClose)
	ON_MESSAGE(WM_ICON_NOTIFY, OnTrayNotification)
	ON_UPDATE_COMMAND_UI(ID_HELPER_REGISTER, OnUpdateHelperRegister)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// StatusWnd implementation

StatusWnd::StatusWnd() {}

BOOL StatusWnd::PreCreateWindow(CREATESTRUCT& cs) 
{	
	return CFrameWnd::PreCreateWindow(cs);
}

int StatusWnd::OnCreate(LPCREATESTRUCT lpCreateStruct) 
{
	// CG: The following line was added by the Splash Screen component.
	Splash::ShowSplashScreen(this);

	if (CFrameWnd::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	if (!m_TrayIcon.Create(this, WM_ICON_NOTIFY, "DGTE Helper", theApp.LoadIcon(IDR_MAINFRAME), IDR_POPUP_MENU))
		return -1;
	
	return 0;
}

void StatusWnd::OnShowStatus()
{
	theApp.ShowStatus();
}

void StatusWnd::OnRegister()
{
	if( !theApp.IsRegistered() ) {
		try {
			theApp.Register();
			AfxMessageBox(_T("The helper has been been registered with the server."));
		}
		catch(ConnectionException e) {
			AfxMessageBox(_T("Failed to register with the server:\n\n" + e.getMessage()));
		}
	}
	else {
		try {
			theApp.Unregister();
			AfxMessageBox(_T("The helper has been been unregistered from the server."));
		}
		catch(ConnectionException e) {
			AfxMessageBox(_T("Failed to unregister from the server:\n\n" + e.getMessage()));
		}
	}
}

void StatusWnd::OnAbout()
{
	AboutDlg aboutDlg;
	aboutDlg.DoModal();
}

void StatusWnd::OnProperties()
{
	Properties propSheet;

	GeneralPage &page1 = propSheet.m_Page1;
	PluginsPage &page2 = propSheet.m_Page2;

	page1.m_serverAddress = theApp.m_serverAddress;
	page1.m_autoRegister = theApp.m_autoRegister;
	page2.m_selectedPlugin = theApp.m_pluginName;

	if( propSheet.DoModal() == IDOK ) {

		theApp.m_serverAddress = page1.m_serverAddress;
		theApp.m_autoRegister = page1.m_autoRegister;

		if( theApp.m_pluginName != page2.m_selectedPlugin ) {
			theApp.m_pluginName = page2.m_selectedPlugin;
			if( theApp.GetPlugin() == NULL )
				theApp.SetPlugin(theApp.m_pluginName);
			else
				AfxMessageBox(_T("Plugin will be loaded the next time you start this application."));
		}
	}
}

void StatusWnd::OnClose()
{
	this->PostMessage( WM_CLOSE, 0, 0 );
}

LRESULT StatusWnd::OnTrayNotification(WPARAM wParam, LPARAM lParam)
{   
   	return m_TrayIcon.OnTrayNotification(wParam, lParam);
}

void StatusWnd::OnUpdateHelperRegister(CCmdUI* pCmdUI) 
{
	if( theApp.IsRegistered() )
		pCmdUI->Enable(FALSE);
	else
		pCmdUI->Enable(TRUE);
}

// DistributedGameTreeServer.h : main header file for the DISTRIBUTEDGAMETREESERVER application
//

#if !defined(AFX_DISTRIBUTEDGAMETREESERVER_H__2CE3C47E_A77B_11D3_90F1_004095100085__INCLUDED_)
#define AFX_DISTRIBUTEDGAMETREESERVER_H__2CE3C47E_A77B_11D3_90F1_004095100085__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "Types.h"
#include "resource.h"       // main symbols
#include "DistributedGameTreeServer_i.h"
#include "GameServer.h"
#include "AboutDlg.h"

/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerApp:
// See DistributedGameTreeServer.cpp for the implementation of this class
//

class CDistributedGameTreeServerApp : public CWinApp
{
public:
	
	GameServer* getGameServer();
	CDistributedGameTreeServerApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDistributedGameTreeServerApp)
	public:
	virtual BOOL InitInstance();
	virtual int ExitInstance();
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	//}}AFX_VIRTUAL

// Implementation
	//{{AFX_MSG(CDistributedGameTreeServerApp)
	afx_msg void OnAppAbout();
	afx_msg void OnToolsOptions();
	afx_msg void OnToolsStatistics();
	afx_msg void OnServerStart();
	afx_msg void OnServerStop();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
private:
	BOOL			m_bATLInited;
	GameServer*		gameserver;	
private:
	BOOL InitATL();
};





/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DISTRIBUTEDGAMETREESERVER_H__2CE3C47E_A77B_11D3_90F1_004095100085__INCLUDED_)

extern CDistributedGameTreeServerApp theApp;


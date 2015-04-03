// DistributedGameTreeServer.cpp : Defines the class behaviors for the application.
//

#include "stdafx.h"
#include "DistributedGameTreeServer.h"

#include "MainFrm.h"
#include "DistributedGameTreeServerDoc.h"
#include "DistributedGameTreeServerView.h"
#include <initguid.h>
#include "DistributedGameTreeServer_i.c"
#include "HelperRegistrar.h"
#include "ServerOptionsDialog.h"
#include "GameServer.h"
#include "Engine.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerApp

BEGIN_MESSAGE_MAP(CDistributedGameTreeServerApp, CWinApp)
	//{{AFX_MSG_MAP(CDistributedGameTreeServerApp)
	ON_COMMAND(ID_APP_ABOUT, OnAppAbout)
	ON_COMMAND(ID_TOOLS_OPTIONS, OnToolsOptions)
	ON_COMMAND(ID_TOOLS_STATISTICS, OnToolsStatistics)
	ON_COMMAND(ID_SERVERSTART, OnServerStart)
	ON_COMMAND(ID_SERVERSTOP, OnServerStop)
	//}}AFX_MSG_MAP
	// Standard file based document commands
	ON_COMMAND(ID_FILE_NEW, CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, CWinApp::OnFileOpen)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerApp construction

CDistributedGameTreeServerApp::CDistributedGameTreeServerApp()
{
	gameserver = NULL;
}

/////////////////////////////////////////////////////////////////////////////
// The one and only CDistributedGameTreeServerApp object

CDistributedGameTreeServerApp theApp;



/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerApp initialization

BOOL CDistributedGameTreeServerApp::InitInstance()
{
	
	if (!AfxSocketInit())
	{
		AfxMessageBox(IDP_SOCKETS_INIT_FAILED);
		return FALSE;
	}

	if (!InitATL())
		return FALSE;

	AfxEnableControlContainer();

	// Standard initialization
	// If you are not using these features and wish to reduce the size
	//  of your final executable, you should remove from the following
	//  the specific initialization routines you do not need.

#ifdef _AFXDLL
	Enable3dControls();			// Call this when using MFC in a shared DLL
#else
	Enable3dControlsStatic();	// Call this when linking to MFC statically
#endif

	// Change the registry key under which our settings are stored.
	// TODO: You should modify this string to be something appropriate
	// such as the name of your company or organization.
	SetRegistryKey(_T("Distributed Game Tree Server"));

	LoadStdProfileSettings();  // Load standard INI file options (including MRU)

	// Register the application's document templates.  Document templates
	//  serve as the connection between documents, frame windows and views.

	CSingleDocTemplate* pDocTemplate;
	pDocTemplate = new CSingleDocTemplate(
		IDR_MAINFRAME,
		RUNTIME_CLASS(CDistributedGameTreeServerDoc),
		RUNTIME_CLASS(CMainFrame),       // main SDI frame window
		RUNTIME_CLASS(CDistributedGameTreeServerView));
	AddDocTemplate(pDocTemplate);

	// Parse command line for standard shell commands, DDE, file open
	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);

	if (cmdInfo.m_bRunEmbedded || cmdInfo.m_bRunAutomated) return TRUE;


	// Dispatch commands specified on the command line
	if (!ProcessShellCommand(cmdInfo))
		return FALSE;

	// The one and only window has been initialized, so show and update it.
	m_pMainWnd->ShowWindow(SW_SHOW);
	m_pMainWnd->UpdateWindow();


	////////////////////////////////////////////////////////////////////////
	// Initiate the configurations and the log files:


	gameserver = new GameServer();			

	CMainFrame* tmpFrame = (CMainFrame*) GetMainWnd(); 
	CDistributedGameTreeServerDoc* tmpDoc = (CDistributedGameTreeServerDoc*) tmpFrame->GetActiveDocument();
	gameserver->setLogContext((LogContext*) tmpDoc);	
	

	return TRUE;
}




/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	//{{AFX_DATA(CAboutDlg)
	enum { IDD = IDD_ABOUTBOX };
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CAboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	//{{AFX_MSG(CAboutDlg)
		// No message handlers
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
	//{{AFX_DATA_INIT(CAboutDlg)
	//}}AFX_DATA_INIT
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
		// No message handlers
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

// App command to run the dialog
void CDistributedGameTreeServerApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}

/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerApp message handlers


	
CDistributedGameTreeServerModule _Module;

BEGIN_OBJECT_MAP(ObjectMap)
OBJECT_ENTRY(CLSID_HelperRegistrar, CHelperRegistrar)
END_OBJECT_MAP()

LONG CDistributedGameTreeServerModule::Unlock()
{
	AfxOleUnlockApp();
	return 0;
}

LONG CDistributedGameTreeServerModule::Lock()
{
	AfxOleLockApp();
	return 1;
}
LPCTSTR CDistributedGameTreeServerModule::FindOneOf(LPCTSTR p1, LPCTSTR p2)
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


int CDistributedGameTreeServerApp::ExitInstance()
{
	if (m_bATLInited)
	{
		_Module.RevokeClassObjects();
		_Module.Term();
		CoUninitialize();
	}

	// Stop and delete the game server:
	gameserver->stop();
	gameserver->setLogContext(NULL);
	delete gameserver;

	return CWinApp::ExitInstance();

}

BOOL CDistributedGameTreeServerApp::InitATL()
{
	m_bATLInited = TRUE;

#if _WIN32_WINNT >= 0x0400
	HRESULT hRes = CoInitializeEx(NULL, COINIT_MULTITHREADED);
#else
	HRESULT hRes = CoInitialize(NULL);
#endif

	if (FAILED(hRes))
	{
		m_bATLInited = FALSE;
		return FALSE;
	}

	_Module.Init(ObjectMap, AfxGetInstanceHandle());
	_Module.dwThreadID = GetCurrentThreadId();

	LPTSTR lpCmdLine = GetCommandLine(); //this line necessary for _ATL_MIN_CRT
	TCHAR szTokens[] = _T("-/");

	BOOL bRun = TRUE;
	LPCTSTR lpszToken = _Module.FindOneOf(lpCmdLine, szTokens);
	while (lpszToken != NULL)
	{
		if (lstrcmpi(lpszToken, _T("UnregServer"))==0)
		{
			_Module.UpdateRegistryFromResource(IDR_DISTRIBUTEDGAMETREESERVER, FALSE);
			_Module.UnregisterServer(TRUE); //TRUE means typelib is unreg'd
			bRun = FALSE;
			break;
		}
		if (lstrcmpi(lpszToken, _T("RegServer"))==0)
		{
			_Module.UpdateRegistryFromResource(IDR_DISTRIBUTEDGAMETREESERVER, TRUE);
			_Module.RegisterServer(TRUE);
			bRun = FALSE;
			break;
		}
		lpszToken = _Module.FindOneOf(lpszToken, szTokens);
	}

	if (!bRun)
	{
		m_bATLInited = FALSE;
		_Module.Term();
		CoUninitialize();
		return FALSE;
	}

	hRes = _Module.RegisterClassObjects(CLSCTX_LOCAL_SERVER, 
		REGCLS_MULTIPLEUSE);
	if (FAILED(hRes))
	{
		m_bATLInited = FALSE;
		CoUninitialize();
		return FALSE;
	}	

	return TRUE;

}

void CDistributedGameTreeServerApp::OnToolsOptions() 
{
	ServerOptionsDialog dlg;
	dlg.setOptions(gameserver->getConfiguration());
	int result = dlg.DoModal();
	if (result == IDOK) {
		gameserver->setConfiguration(dlg.getOptions());
	}
}

void CDistributedGameTreeServerApp::OnToolsStatistics() 
{
	this->DoMessageBox("Sorry, this feature is not available yet.",0,0);
}

void CDistributedGameTreeServerApp::OnServerStart() 
{		
	gameserver->start();
}

void CDistributedGameTreeServerApp::OnServerStop() 
{
	gameserver->stop(); 
}

GameServer* CDistributedGameTreeServerApp::getGameServer()
{
	return this->gameserver;
}




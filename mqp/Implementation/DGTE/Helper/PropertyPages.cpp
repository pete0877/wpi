/////////////////////////////////////////////////////////////////////////////
//
// File Name:	PropertyPages.cpp
// Description:	Defines the class behaviors for the application.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#include "StdAfx.h"
#include "Exceptions.h"
#include "PluginProperties.h"
#include "PropertyPages.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

IMPLEMENT_DYNCREATE(GeneralPage, CPropertyPage)
IMPLEMENT_DYNCREATE(PluginsPage, CPropertyPage)

/////////////////////////////////////////////////////////////////////////////
// GeneralPage message map

BEGIN_MESSAGE_MAP(GeneralPage, CPropertyPage)
	//{{AFX_MSG_MAP(GeneralPage)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// GeneralPage property page

GeneralPage::GeneralPage() : CPropertyPage(GeneralPage::IDD)
{
	//{{AFX_DATA_INIT(GeneralPage)
	m_autoRegister = FALSE;
	m_serverAddress = _T("");
	//}}AFX_DATA_INIT
}

void GeneralPage::DoDataExchange(CDataExchange* pDX)
{
	CPropertyPage::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(GeneralPage)
	DDX_Check(pDX, IDC_AUTOREGISTER, m_autoRegister);
	DDX_Text(pDX, IDC_SERVERADDRESS, m_serverAddress);
	//}}AFX_DATA_MAP
}

/////////////////////////////////////////////////////////////////////////////
// PluginsPage message map

BEGIN_MESSAGE_MAP(PluginsPage, CPropertyPage)
	//{{AFX_MSG_MAP(PluginsPage)
	ON_BN_CLICKED(IDC_ABOUT, OnAbout)
	ON_LBN_DBLCLK(IDC_PLUGINS, OnDblclkPlugins)
	ON_LBN_SELCHANGE(IDC_PLUGINS, OnSelchangePlugins)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// PluginsPage property page

PluginsPage::PluginsPage() : CPropertyPage(PluginsPage::IDD)
{
	//{{AFX_DATA_INIT(PluginsPage)
	m_selectedPlugin = _T("");
	m_selectedPluginIndex = -1;
	//}}AFX_DATA_INIT

	// Initialize the plugins vector
	InitPlugins();
}

BOOL PluginsPage::OnInitDialog() 
{
	CDialog::OnInitDialog();

	// Initialize the plugin URL.
	m_hyperLink.SetURL(_T("http://www.wpi.edu/~peterg/MQP/Plugins/"));
	m_hyperLink.SetColours(m_hyperLink.GetLinkColour(), m_hyperLink.GetLinkColour());
	m_hyperLink.SetLinkCursor(AfxGetApp()->LoadCursor(IDC_HAND));
	::SetCursor(::LoadCursor(NULL, IDC_ARROW));
	
	// Initialize the plugins listbox.
	CListBox listBox;
	listBox.Attach(GetDlgItem(IDC_PLUGINS)->m_hWnd);

	for(int i = 0; i < m_plugins.size(); i++)
	{
		Plugin &plugin = m_plugins.at(i);
		CString pluginDll = plugin.m_pluginDll;
		CString gameName = plugin.gameTypeData->gameName;
		if( m_selectedPluginIndex == -1 ) {
			if( m_selectedPlugin == pluginDll )
				m_selectedPluginIndex = i;
		}
		listBox.AddString(gameName);
	}

	if( m_selectedPluginIndex != -1)
		listBox.SetCurSel(m_selectedPluginIndex);

	listBox.Detach();

	return TRUE;
}

void PluginsPage::OnOK() 
{
	CPropertyPage::OnOK();

	if(m_selectedPluginIndex != -1) {
		Plugin &plugin = m_plugins.at(m_selectedPluginIndex);
		m_selectedPlugin = plugin.m_pluginDll;
	}
	else
		m_selectedPlugin = _T("");
}

void PluginsPage::DoDataExchange(CDataExchange* pDX)
{
	CPropertyPage::DoDataExchange(pDX);

	//{{AFX_DATA_MAP(PluginsPage)
	DDX_Control(pDX, IDC_GETPLUGINS, m_hyperLink);
	DDX_LBIndex(pDX, IDC_PLUGINS, m_selectedPluginIndex);
	//}}AFX_DATA_MAP
}

/////////////////////////////////////////////////////////////////////////////
// PluginsPage message handlers

void PluginsPage::OnAbout() 
{
	if( m_selectedPluginIndex == -1 ) {
		AfxMessageBox(_T("Please select one of the plugins first."));
		return;
	}
	
	Plugin &plugin = m_plugins.at(m_selectedPluginIndex);
	GameTypeData data = *(plugin.gameTypeData);

	PluginProperties propSheet(data);
	propSheet.DoModal();
}

void PluginsPage::OnDblclkPlugins() 
{
	OnAbout();
}

void PluginsPage::OnSelchangePlugins() 
{
	CListBox listBox;

	listBox.Attach(GetDlgItem(IDC_PLUGINS)->m_hWnd);
	m_selectedPluginIndex = listBox.GetCurSel();
	listBox.Detach();	
}

/////////////////////////////////////////////////////////////////////////////
// PluginsPage implementation

void PluginsPage::InitPlugins() 
{
	WIN32_FIND_DATA fd;

	HANDLE hFind = ::FindFirstFile(_T("Plugins\\*.dll"), &fd);
	
	if( hFind != INVALID_HANDLE_VALUE ) {
		do {
			if( !(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) ) {
				CString pluginDll = fd.cFileName;
				try {
					Plugin plugin(pluginDll);
					m_plugins.push_back(plugin);
				}
				catch(PluginNotFoundException e) {
					TRACE(e.getMessage());
				}
				catch(InvalidPluginException e) {
					TRACE(e.getMessage());
				}
			}
		} while( ::FindNextFile(hFind, &fd) );
		
		FindClose(hFind);
	}
}

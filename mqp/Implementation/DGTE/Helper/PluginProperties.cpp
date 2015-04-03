/////////////////////////////////////////////////////////////////////////////
//
// File Name:	PluginProperties.cpp
// Description:	Defines the class behaviors for the application.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#include "StdAfx.h"
#include "PluginProperties.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// PluginProperties message map

BEGIN_MESSAGE_MAP(PluginProperties, CDialog)
	//{{AFX_MSG_MAP(PluginProperties)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// PluginProperties dialog

PluginProperties::PluginProperties(GameTypeData data, CWnd* pParent /*=NULL*/)
	: CDialog(PluginProperties::IDD, pParent)
{
	//{{AFX_DATA_INIT(PluginProperties)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT

	m_gameName = data.gameName;
	m_gameVersion = data.gameVersion;
	m_gameDescription = data.gameDescription;
	m_companyName = data.companyName;
	m_legalCopyright = data.legalCopyright;
	m_legalTrademarks = data.legalTrademarks;
}

void PluginProperties::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(PluginProperties)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}

/////////////////////////////////////////////////////////////////////////////
// PluginProperties message handlers

BOOL PluginProperties::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	GetDlgItem(IDC_GAMENAME)->SetWindowText(m_gameName);
	GetDlgItem(IDC_GAMEVERSION)->SetWindowText(m_gameVersion);
	GetDlgItem(IDC_GAMEDESCRIPTION)->SetWindowText(m_gameDescription);
	GetDlgItem(IDC_COMPANYNAME)->SetWindowText(m_companyName);
	GetDlgItem(IDC_LEGALCOPYRIGHT)->SetWindowText(m_legalCopyright);
	GetDlgItem(IDC_LEGALTRADEMARKS)->SetWindowText(m_legalTrademarks);
	
	return TRUE;
}

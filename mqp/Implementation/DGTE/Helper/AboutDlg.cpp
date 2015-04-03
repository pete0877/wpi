/////////////////////////////////////////////////////////////////////////////
//
// File Name:	AboutDlg.cpp
// Description:	Defines the class behaviors for the application.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#include "StdAfx.h"
#include "AboutDlg.h"
#include "AutoBuildCount.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// AboutDlg message map

BEGIN_MESSAGE_MAP(AboutDlg, CDialog)
	//{{AFX_MSG_MAP(AboutDlg)
		// NOTE: the ClassWizard will add message map macros here
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// AboutDlg dialog

AboutDlg::AboutDlg(CWnd* pParent /*=NULL*/)
	: CDialog(AboutDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(AboutDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}

BOOL AboutDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();

	m_hyperLink.SetURL(_T("http://www.wpi.edu/~peterg/MQP"));
	m_hyperLink.SetColours(m_hyperLink.GetLinkColour(), m_hyperLink.GetLinkColour());
	m_hyperLink.SetLinkCursor(AfxGetApp()->LoadCursor(IDC_HAND));
	::SetCursor(::LoadCursor(NULL, IDC_ARROW));

	CString version;
	CStatic versionLabel;
	versionLabel.Attach(GetDlgItem(IDC_VERSION)->m_hWnd);
	versionLabel.GetWindowText(version);
	CString str;
	str.Format(_T("%s (Build %d)"), version, BUILDCOUNT_NUM);
	versionLabel.SetWindowText(str);
	
	versionLabel.Detach();
	return TRUE;
}

void AboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(AboutDlg)
	DDX_Control(pDX, IDC_ABOUT_HYPERLINK, m_hyperLink);
	//}}AFX_DATA_MAP
}

/////////////////////////////////////////////////////////////////////////////
// AboutDlg message handlers

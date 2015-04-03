/////////////////////////////////////////////////////////////////////////////
//
// File Name:	StatusDlg.cpp
// Description:	Defines the class behaviors for the application.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#include "StdAfx.h"
#include "StatusDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// AboutDlg message map

BEGIN_MESSAGE_MAP(StatusDlg, CDialog)
	//{{AFX_MSG_MAP(StatusDlg)
		// NOTE: the ClassWizard will add message map macros here
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()


/////////////////////////////////////////////////////////////////////////////
// StatusDlg dialog

StatusDlg::StatusDlg(CWnd* pParent /*=NULL*/)
	: CDialog(StatusDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(StatusDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}

void StatusDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(StatusDlg)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}

void StatusDlg::AddMessage(CString message)
{
	CString str;
	CEdit output;	

	output.Attach(GetDlgItem(IDC_OUTPUT)->m_hWnd);
	output.GetWindowText(str);
	
	str += message;
	str += _T("\x00D\x00A");
	
	output.SetWindowText(str);
	output.Detach();
}

/////////////////////////////////////////////////////////////////////////////
// StatusDlg message handlers

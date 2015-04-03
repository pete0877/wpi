/////////////////////////////////////////////////////////////////////////////
//
// File Name:	StatusDlg.h
// Description:	Interface of the StatusDlg class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef STATUSDLG_INCLUDED
#define STATUSDLG_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "Resource.h"

/////////////////////////////////////////////////////////////////////////////
// StatusDlg dialog

class StatusDlg : public CDialog
{
// Construction
public:
	StatusDlg(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(StatusDlg)
	enum { IDD = IDD_STATUSDLG_DIALOG };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

// Operations
public:
	void AddMessage(CString message);

// Implementation
protected:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(StatusDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Generated message map functions
protected:
	//{{AFX_MSG(StatusDlg)
		// NOTE: the ClassWizard will add member functions here
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}

/////////////////////////////////////////////////////////////////////////////

#endif // STATUSDLG_INCLUDED

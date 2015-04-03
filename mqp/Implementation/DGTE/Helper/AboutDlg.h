/////////////////////////////////////////////////////////////////////////////
//
// File Name:	AboutDlg.h
// Description:	Interface of the AboutDlg class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef ABOUTDLG_INCLUDED
#define ABOUTDLG_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "Resource.h"
#include "HyperLink.h"

/////////////////////////////////////////////////////////////////////////////
// AboutDlg dialog

class AboutDlg : public CDialog
{
// Construction
public:
	AboutDlg(CWnd* pParent = NULL);

// Dialog Data
	//{{AFX_DATA(AboutDlg)
	enum { IDD = IDD_ABOUT_DIALOG };
	CHyperLink m_hyperLink;
	//}}AFX_DATA

// Implementation
protected:

// Overrides
public:
	//{{AFX_VIRTUAL(AboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual BOOL OnInitDialog();
	//}}AFX_VIRTUAL

// Generated message map functions
protected:
	//{{AFX_MSG(AboutDlg)
	//}}AFX_MSG

	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}

/////////////////////////////////////////////////////////////////////////////

#endif // ABOUTDLG_INCLUDED

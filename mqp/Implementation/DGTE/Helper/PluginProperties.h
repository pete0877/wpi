/////////////////////////////////////////////////////////////////////////////
//
// File Name:	PluginProperties.h
// Description:	Interface of the PluginProperties class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef PLUGINPROPERTIES_INCLUDED
#define PLUGINPROPERTIES_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "Helper_i.h"
#include "Resource.h"

/////////////////////////////////////////////////////////////////////////////
// PluginProperties dialog

class PluginProperties : public CDialog
{
// Construction
public:
	PluginProperties(GameTypeData data, CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(PluginProperties)
	enum { IDD = IDD_GAMEPROPERTIES_DIALOG };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

// Implementation
protected:
	CString m_gameName;
	CString m_gameVersion;
	CString m_gameDescription;
	CString m_companyName;
	CString m_legalCopyright;
	CString m_legalTrademarks;

// Overrides
public:
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(PluginProperties)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Generated message map functions
protected:
	//{{AFX_MSG(PluginProperties)
	virtual BOOL OnInitDialog();
	//}}AFX_MSG

	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}

/////////////////////////////////////////////////////////////////////////////

#endif // PLUGINPROPERTIES_INCLUDED

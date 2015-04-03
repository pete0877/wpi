/////////////////////////////////////////////////////////////////////////////
//
// File Name:	PropertyPages.h
// Description:	Interface of the GeneralPage and PluginsPage classes.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef PROPERTYPAGES_INCLUDED
#define PROPERTYPAGES_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include <vector>
#include "Helper_i.h"
#include "HyperLink.h"
#include "Plugin.h"
#include "Resource.h"

using namespace std;

/////////////////////////////////////////////////////////////////////////////
// GeneralPage dialog

class GeneralPage : public CPropertyPage
{
	DECLARE_DYNCREATE(GeneralPage)

// Construction
public:
	GeneralPage();

// Dialog Data
	//{{AFX_DATA(GeneralPage)
	enum { IDD = IDD_GENERAL_PAGE };
	BOOL	m_autoRegister;
	CString m_serverAddress;
	//}}AFX_DATA

// Implementation
public:

protected:

// Overrides
public:
	// ClassWizard generate virtual function overrides
	//{{AFX_VIRTUAL(GeneralPage)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Generated message map functions
protected:
	//{{AFX_MSG(GeneralPage)
		// NOTE: the ClassWizard will add member functions here
	//}}AFX_MSG
	
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////
// PluginsPage dialog

class PluginsPage : public CPropertyPage
{
	DECLARE_DYNCREATE(PluginsPage)

// Construction
public:
	PluginsPage();

// Dialog Data
	//{{AFX_DATA(PluginsPage)
	enum { IDD = IDD_PLUGINS_PAGE };
	CHyperLink m_hyperLink;
	CString m_selectedPlugin;
	int	m_selectedPluginIndex;
	//}}AFX_DATA

// Implementation
public:

protected:
	vector<Plugin> m_plugins;

	void InitPlugins();

// Overrides
public:
	// ClassWizard generate virtual function overrides
	//{{AFX_VIRTUAL(PluginsPage)
	public:
	virtual void OnOK();
	protected:
	virtual BOOL OnInitDialog();
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Generated message map functions
protected:
	//{{AFX_MSG(PluginsPage)
	afx_msg void OnAbout();
	afx_msg void OnDblclkPlugins();
	afx_msg void OnSelchangePlugins();
	//}}AFX_MSG
	
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

#endif // PROPERTYPAGES_INCLUDED

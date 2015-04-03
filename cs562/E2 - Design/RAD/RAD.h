// RAD.h : main header file for the RAD application
//

#if !defined(AFX_RAD_H__D91A1967_FE23_11D3_94B0_004095100085__INCLUDED_)
#define AFX_RAD_H__D91A1967_FE23_11D3_94B0_004095100085__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CRADApp:
// See RAD.cpp for the implementation of this class
//

class CRADApp : public CWinApp
{
public:
	CRADApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CRADApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

	//{{AFX_MSG(CRADApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_RAD_H__D91A1967_FE23_11D3_94B0_004095100085__INCLUDED_)

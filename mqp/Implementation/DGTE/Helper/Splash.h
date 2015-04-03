/////////////////////////////////////////////////////////////////////////////
//
// File Name:	Splash.h
// Description:	Interface of the Splash class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// CG: This file was added by the Splash Screen component.

#ifndef SPLASH_INCLUDED
#define SPLASH_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "Resource.h"

/////////////////////////////////////////////////////////////////////////////
// Class Splash

class Splash : public CWnd
{
// Construction
protected:
	Splash();

// Attributes:
public:
	CBitmap m_bitmap;

// Operations
public:
	static void EnableSplashScreen(BOOL bEnable = TRUE);
	static void ShowSplashScreen(CWnd* pParentWnd = NULL);
	static BOOL PreTranslateAppMessage(MSG* pMsg);

// Implementation
public:
	~Splash();
	virtual void PostNcDestroy();

protected:
	BOOL Create(CWnd* pParentWnd = NULL);
	void HideSplashScreen();
	static BOOL c_bShowSplashWnd;
	static Splash* c_pSplashWnd;

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(Splash)
	//}}AFX_VIRTUAL

// Generated message map functions
protected:
	//{{AFX_MSG(Splash)
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnPaint();
	afx_msg void OnTimer(UINT nIDEvent);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

#endif // SPLASH_INCLUDED

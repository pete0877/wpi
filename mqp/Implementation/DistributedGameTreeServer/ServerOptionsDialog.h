#if !defined(AFX_SERVEROPTIONSDIALOG_H__2CE3C492_A77B_11D3_90F1_004095100085__INCLUDED_)
#define AFX_SERVEROPTIONSDIALOG_H__2CE3C492_A77B_11D3_90F1_004095100085__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// ServerOptionsDialog.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// ServerOptionsDialog dialog

#include "GameServerConfiguration.h"

class ServerOptionsDialog : public CDialog
{
// Construction
public:
	ServerOptionsDialog(CWnd* pParent = NULL);   // standard constructor
	void setOptions(const GameServerConfiguration& newOptions);
	GameServerConfiguration getOptions();

// Dialog Data
	//{{AFX_DATA(ServerOptionsDialog)
	enum { IDD = IDD_DIALOG2 };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(ServerOptionsDialog)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	GameServerConfiguration options;
	void update();
	// Generated message map functions
	//{{AFX_MSG(ServerOptionsDialog)
	afx_msg void OnChangeEdit1();
	afx_msg void OnChangeEdit2();
	afx_msg void OnChangeEdit3();
	afx_msg void OnChangeEdit4();
	afx_msg void OnChangeEdit5();
	afx_msg void OnChangeEdit6();
	afx_msg void OnChangeEdit7();
	afx_msg void OnChangeEdit8();
	virtual BOOL OnInitDialog();	
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_SERVEROPTIONSDIALOG_H__2CE3C492_A77B_11D3_90F1_004095100085__INCLUDED_)

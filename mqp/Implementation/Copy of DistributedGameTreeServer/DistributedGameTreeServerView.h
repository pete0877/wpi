// DistributedGameTreeServerView.h : interface of the CDistributedGameTreeServerView class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_DISTRIBUTEDGAMETREESERVERVIEW_H__2CE3C486_A77B_11D3_90F1_004095100085__INCLUDED_)
#define AFX_DISTRIBUTEDGAMETREESERVERVIEW_H__2CE3C486_A77B_11D3_90F1_004095100085__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "DistributedGameTreeServerView.h"

class CDistributedGameTreeServerView : public CEditView {

public:		
	CDistributedGameTreeServerDoc* GetDocument();
	

	protected: // create from serialization only
		DECLARE_DYNCREATE(CDistributedGameTreeServerView)

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDistributedGameTreeServerView)
	protected:
	virtual void OnDraw(CDC* pDC);
	//}}AFX_VIRTUAL

	protected:
	//{{AFX_MSG(CDistributedGameTreeServerView)
	afx_msg void OnRefresh();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

#ifndef _DEBUG  // debug version in DistributedGameTreeServerView.cpp
inline CDistributedGameTreeServerDoc* CDistributedGameTreeServerView::GetDocument()
   { return (CDistributedGameTreeServerDoc*)m_pDocument; }
#endif


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DISTRIBUTEDGAMETREESERVERVIEW_H__2CE3C486_A77B_11D3_90F1_004095100085__INCLUDED_)

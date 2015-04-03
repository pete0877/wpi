// DistributedGameTreeServerDoc.h : interface of the CDistributedGameTreeServerDoc class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_DISTRIBUTEDGAMETREESERVERDOC_H__2CE3C484_A77B_11D3_90F1_004095100085__INCLUDED_)
#define AFX_DISTRIBUTEDGAMETREESERVERDOC_H__2CE3C484_A77B_11D3_90F1_004095100085__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "LogContext.h"

class CDistributedGameTreeServerView;

class CDistributedGameTreeServerDoc : public CDocument, public LogContext
{
protected: // create from serialization only
	CDistributedGameTreeServerDoc();
	CString text;
	DECLARE_DYNCREATE(CDistributedGameTreeServerDoc)

// Attributes
public:	
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDistributedGameTreeServerDoc)
	public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);
	//}}AFX_VIRTUAL

	CString getText();
	void addEntry(CString classID, CString objectID, CString event);
	virtual ~CDistributedGameTreeServerDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif


protected:
	//{{AFX_MSG(CDistributedGameTreeServerDoc)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DISTRIBUTEDGAMETREESERVERDOC_H__2CE3C484_A77B_11D3_90F1_004095100085__INCLUDED_)

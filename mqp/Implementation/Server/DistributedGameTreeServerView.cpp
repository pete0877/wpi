// DistributedGameTreeServerView.cpp : implementation of the CDistributedGameTreeServerView class
//

#include "stdafx.h"
#include "DistributedGameTreeServer.h"

#include "DistributedGameTreeServerDoc.h"
#include "DistributedGameTreeServerView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerView

IMPLEMENT_DYNCREATE(CDistributedGameTreeServerView, CEditView)

BEGIN_MESSAGE_MAP(CDistributedGameTreeServerView, CEditView)
	//{{AFX_MSG_MAP(CDistributedGameTreeServerView)
	ON_COMMAND(ID_REFRESH, OnRefresh)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()


#ifdef _DEBUG

CDistributedGameTreeServerDoc* CDistributedGameTreeServerView::GetDocument() // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CDistributedGameTreeServerDoc)));
	return (CDistributedGameTreeServerDoc*)m_pDocument;	
}
#endif //_DEBUG


void CDistributedGameTreeServerView::OnDraw(CDC* pDC) 
{
	/*
	this->ModifyStyle(0,WS_VSCROLL);
	CDistributedGameTreeServerDoc* doc = GetDocument();
	CString messages = doc->getText();	
	int a = 0;	
	int last = 0;
	int h = 0;
	while (a < messages.GetLength()) {
		if (messages.Mid(a,1) == "\n") {
			CString tmp = messages.Mid(last,a - last);
			tmp.Replace(":", "-");
			tmp.Replace("|", ":  ");
			tmp.Replace("\n", "");
			last = a + 1;

			pDC->TextOut(0,h,tmp);
			h += 15;
		}
		a++;
	}	
	*/
	CEditView::OnDraw(pDC);

}

void CDistributedGameTreeServerView::OnRefresh() 
{
	CDistributedGameTreeServerDoc* doc = GetDocument();
	CString messages = doc->getText();
	messages.Replace("\n", "\x00D\x00A");
	messages.Replace(":", "-");
	messages.Replace("|", ":  ");
	this->GetEditCtrl().SetWindowText(messages);
	return;
	
}

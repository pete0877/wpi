// DistributedGameTreeServerDoc.cpp : implementation of the CDistributedGameTreeServerDoc class
//

#include "stdafx.h"
#include "DistributedGameTreeServer.h"
#include "DistributedGameTreeServerDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerDoc

IMPLEMENT_DYNCREATE(CDistributedGameTreeServerDoc, CDocument)

BEGIN_MESSAGE_MAP(CDistributedGameTreeServerDoc, CDocument)
	//{{AFX_MSG_MAP(CDistributedGameTreeServerDoc)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerDoc construction/destruction

CDistributedGameTreeServerDoc::CDistributedGameTreeServerDoc()
{
	// TODO: add one-time construction code here

}

CDistributedGameTreeServerDoc::~CDistributedGameTreeServerDoc()
{
}

BOOL CDistributedGameTreeServerDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}



/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerDoc serialization

void CDistributedGameTreeServerDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
}

/////////////////////////////////////////////////////////////////////////////
// CDistributedGameTreeServerDoc diagnostics

#ifdef _DEBUG
void CDistributedGameTreeServerDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CDistributedGameTreeServerDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

void CDistributedGameTreeServerDoc::addEntry(CString classID, CString objectID, CString event)
{	
	CString line_break = "\n";
	SimpleDateTime t;
	t.setToNow();
	text = text + t.toString() + "|";
	text = text + classID + "|";
	text = text + objectID + "|";
	text = text + event;
	text = text + line_break;
	// UpdateAllViews(NULL);	
}

CString CDistributedGameTreeServerDoc::getText()
{
	return text;
}


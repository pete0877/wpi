// LogContext.h: interface for the LogContext class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_LOGCONTEXT_H__45BA1D83_B052_11D3_9103_004095100085__INCLUDED_)
#define AFX_LOGCONTEXT_H__45BA1D83_B052_11D3_9103_004095100085__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class LogContext  
{
public:
	virtual void addEntry(CString classID, CString objectID, CString event)=0;
};

#endif // !defined(AFX_LOGCONTEXT_H__45BA1D83_B052_11D3_9103_004095100085__INCLUDED_)

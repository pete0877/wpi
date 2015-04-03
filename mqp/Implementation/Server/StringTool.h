//////////////////////////////////////////////////////////////////////
// StringTool.h: interface for the StringTool class.

#if !defined(AFX_STRINGTOOL_H__0A7F1D67_A5F1_11D3_90EF_004095100085__INCLUDED_)
#define AFX_STRINGTOOL_H__0A7F1D67_A5F1_11D3_90EF_004095100085__INCLUDED_

#if _MSC_VER > 1000
	#pragma once
#endif 

class StringTool : public CString  
{
public:
	StringTool();
	long toLong();
	void set(CString str);
};

#endif 

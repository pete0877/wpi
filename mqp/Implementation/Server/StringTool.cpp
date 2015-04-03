// StringTool.cpp: implementation of the StringTool class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "StringTool.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif


long StringTool::toLong() {
	char *arg = this->GetBuffer(0);	
	long result = 0;
	if (strlen(arg)==0) throw new CException();
	int c, len;
	len = strlen(arg);
	for (c=0; c<len; c++) {
		if ((arg[c]<'0') || (arg[c]>'9')) throw new CException();
		result = result * 10 + (arg[c] - '0');
	}
	return result;
}


StringTool::StringTool() {}

void StringTool::set(CString argstr) 
{
	CString* s = (CString*) this;
	s->operator = (argstr);
}




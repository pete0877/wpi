// UnevenDistributionAlgorithm.h: interface for the UnevenDistributionAlgorithm class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_UNEVENDISTRIBUTIONALGORITHM_H__27484E42_AD2A_11D3_90FE_004095100085__INCLUDED_)
#define AFX_UNEVENDISTRIBUTIONALGORITHM_H__27484E42_AD2A_11D3_90FE_004095100085__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "DistributionAlgorithm.h"

class UnevenDistributionAlgorithm : public DistributionAlgorithm  
{
protected:
	LogContext* logContext;
public:
	static UINT run(LPVOID pParam);
	void start(GameTreeNode* node, vector<Helper*> helpers, SimpleDateTime* deadline);
	void setLogContext(LogContext* new_log);
};

#endif // !defined(AFX_UNEVENDISTRIBUTIONALGORITHM_H__27484E42_AD2A_11D3_90FE_004095100085__INCLUDED_)

// DistributionAlgorithm.h: interface for the DistributionAlgorithm class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_DISTRIBUTIONALGORITHM_H__27484E41_AD2A_11D3_90FE_004095100085__INCLUDED_)
#define AFX_DISTRIBUTIONALGORITHM_H__27484E41_AD2A_11D3_90FE_004095100085__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "types.h"
#include "helper.h"
#include "SimpleDateTime.h"
#include "DistributionAlgorithmParameters.h"

class DistributionAlgorithm  
{
public:
	virtual void start(GameTreeNode* node, vector<Helper*> helpers, SimpleDateTime* deadline)=0;
	virtual void setLogContext(LogContext* new_log)=0;
};

#endif // !defined(AFX_DISTRIBUTIONALGORITHM_H__27484E41_AD2A_11D3_90FE_004095100085__INCLUDED_)

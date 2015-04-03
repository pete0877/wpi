///////////////////////////////////////////////////////////////////////////
// DistributionAlgorithmParameters.h
//

#if !defined(DistributionAlgorithmParameters_h)
#define DistributionAlgorithmParameters_h

// DistributionAlgorithmParameters - servers communication between threads in 
//  engine class

typedef struct tagDistributionAlgorithmParameters {
	GameTreeNode*		node;
	vector<Helper*>		helpers;
	SimpleDateTime*		deadline;
} DistributionAlgorithmParameters;

#endif /* DistributionAlgorithmParameters_h */


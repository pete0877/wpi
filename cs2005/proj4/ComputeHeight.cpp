#include "proj4tree.h"

/*
 * ComputeHeight -- recursively compute the height of the tree
 */
int ComputeHeight(TreeNodeType *pTreeRoot)
{
	if (pTreeRoot==NULL) return(0);
 	int a,b;
	a = ComputeHeight (pTreeRoot->pTreeNext);
	b = ComputeHeight (pTreeRoot->pTreeLeft); 
	if (pTreeRoot->pTreeLeft!=NULL) b = b + 1;
	if (a>b) return (a); else return(b);
}

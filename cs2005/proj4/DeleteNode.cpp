#include "proj4tree.h"

/*
 * DeleteNode -- Delete the given node, which must be a leaf.  Return 0 on 
 *      success, ENOTLEAF if the node exists but is not a leaf and 
 *      ENOEXIST if the node does not exist.
 */
int DeleteNode(TreeNodeType * &pTreeRoot, char chNode)
{
   TreeNodeType *temp,*temp2;
   int a;

   if (pTreeRoot->chNode==chNode)
   {
	// the root node:
        if (pTreeRoot->pTreeLeft!=NULL) return(ENOTLEAF);
        // delete
	delete(pTreeRoot);
	pTreeRoot=NULL;
        return(0);
   }

   if (FindNode(pTreeRoot, chNode)==NULL) return(ENOEXIST);
   temp=FindParentNode(pTreeRoot, chNode);
   temp2=temp->pTreeLeft;
   if (temp2->chNode==chNode)
   {
   	// First in the list
	if (temp2->pTreeLeft!=NULL) return(ENOTLEAF);
	temp2=temp2->pTreeNext;
	delete(temp->pTreeLeft);
	temp->pTreeLeft=temp2;
        return(0);
   }
   // Look in the rest of the list:
   temp=temp2->pTreeNext;
   while(temp!=NULL)
   {
	if (temp->chNode==chNode)
	{
		// Found it !
		if (temp->pTreeLeft!=NULL) return(ENOTLEAF);
		// Delete it !
		temp=temp->pTreeNext;
		delete(temp2->pTreeNext);
		temp2->pTreeNext=temp;
		return(0);	 
        }
	temp2=temp;
	temp=temp->pTreeNext;
   }
}

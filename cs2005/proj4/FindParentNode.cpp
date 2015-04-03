#include "proj4tree.h"

/*
 * FindParentNode -- recursively find the parent of the node with the
   given 
 *      value, return NULL on failure or if the node is the root
 */
TreeNodeType *FindParentNode(TreeNodeType *pTreeRoot, char chNode)
{
   
   TreeNodeType *temp;
   if (pTreeRoot==NULL) return(NULL);
   temp=pTreeRoot->pTreeLeft;
   while (temp!=NULL)
   {
      if (temp->chNode==chNode) return(pTreeRoot);
      temp=temp->pTreeNext;
   }
   if ((temp=FindParentNode(pTreeRoot->pTreeNext,chNode))!=NULL)
     return(temp);
   if ((temp=FindParentNode(pTreeRoot->pTreeLeft,chNode))!=NULL)
     return(temp);
   return(NULL);
}

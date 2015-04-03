/*
 * AddNode.cpp
 * adds a node to the tree.  Returns:
 * 0 Success
 * EMULTROOT root already exists
 * EPARENT parent does not exist
 * EEXIST node already exists
 * ENOMEMORY could not create the node, no more memory

 *******************************************************
 *           written by Park Hays, 12-14-96            *
 *******************************************************
 */
#include "proj4tree.h"

int AddNode(TreeNodeType * &pTreeRoot, char chParent, char chNode){
   TreeNodeType *pTreeTemp, *pTreeNew;


   // Case: Trying to add another root.
   if(pTreeRoot != NULL && chParent == ROOT) return(EMULTROOT);
   // Case: Parent does not exist.
   if(FindNode(pTreeRoot, chParent) == NULL) return(EPARENT);
   // Case: Node already exists.
   if(FindNode(pTreeRoot, chNode) != NULL) return(EEXIST);

   // Case: Node is root.
   if(chParent == ROOT){
      pTreeRoot = MakeTreeNode(chNode);
   	pTreeRoot = new TreeNodeType;
      if(pTreeRoot == NULL) return(ENOMEMORY);
      return(0);
   }
   // Case: Normal.
   // find last one in list.
   for(pTreeTemp = FindNode(pTreeRoot, chParent)->pTreeLeft;
   	pTreeTemp != NULL;
      pTreeTemp = pTreeTemp->pTreeNext);
   // create and initialize new node.
   pTreeNew = MakeTreeNode(chNode);
   if(pTreeNew == NULL) return(ENOMEMORY);
	pTreeTemp->pTreeNext = pTreeNew;
	return(0);
}


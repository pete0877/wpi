#include "proj4tree.h"

/*
 * CountLeafNode.cpp
 * recursively counts the number of leaf nodes
 *******************************************************
 *           written by Park Hays, 12-14-96            *
 *******************************************************
 */
int CountLeafNode(TreeNodeType *pTreeRoot){
   int wNumLeaf = 0;
   TreeNodeType *pTreeTemp;

   if(pTreeRoot->pTreeLeft == NULL) return(1);
   for(pTreeTemp=pTreeRoot; pTreeTemp!=NULL; pTreeTemp=pTreeTemp->pTreeNext){
      wNumLeaf += CountLeafNode(pTreeTemp);
   }
   return(wNumLeaf);
}

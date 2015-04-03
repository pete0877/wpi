#include "proj4tree.h"
TreeNodeType *FindNode(TreeNodeType *pTreeRoot, char chNode)
{
  // Create local pointer pTree
  TreeNodeType *pTree = NULL;
                        
  // Checking if there even IS a tree
  if (pTreeRoot == NULL)
    return (NULL);
               
  while (pTreeRoot != NULL && pTree == NULL)
  {
    // IF the node is found, RETURN the results
    if (pTreeRoot->chNode == chNode)
      return pTreeRoot;
 
    // Node not found, traverse further
    pTree = FindNode(pTreeRoot->pTreeLeft, chNode);
    pTreeRoot = pTreeRoot->pTreeNext;
  }
        
  // Return results
  return(pTree);        
}

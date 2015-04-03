#include "proj4tree.h"

/*
 * MakeTreeNode - make a tree node.  Return pointer to node on success and
 *      NULL on failure.
 */
TreeNodeType *MakeTreeNode(char chNode)
{
  TreeNodeType *temp;
  if ((temp=new(TreeNodeType))==NULL) return(NULL);
  temp->chNode=chNode;
  temp->pTreeLeft=NULL;
  temp->pTreeNext=NULL;
  return(temp);
}

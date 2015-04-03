#include "proj4tree.h"

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

int AddNode(TreeNodeType * &pTreeRoot, char chParent, char chNode){
   TreeNodeType *pTreeTemp, *pTreeNew;

   // Case: Node is root.
   if(chParent == ROOT){
      // Case: Trying to add another root.
	   if(pTreeRoot != NULL) return(EMULTROOT);

      pTreeRoot = MakeTreeNode(chNode);
      if(pTreeRoot == NULL) return(ENOMEMORY);
      pTreeRoot->pTreeLeft = NULL;
      pTreeRoot->pTreeNext = NULL;
      return(0);
   }


   // Case: Parent does not exist.
   if(FindNode(pTreeRoot, chParent) == NULL) return(EPARENT);
   // Case: Node already exists.
   if(FindNode(pTreeRoot, chNode) != NULL)
return(EEXIST);
   
   // Case: Normal.
   pTreeTemp = FindNode(pTreeRoot, chParent);
   // create and initialize new node.
   pTreeNew = MakeTreeNode(chNode);
   if(pTreeNew == NULL) return(ENOMEMORY);
   pTreeNew->pTreeLeft = NULL;
   pTreeNew->pTreeNext = pTreeTemp->pTreeLeft;
   pTreeTemp->pTreeLeft = pTreeNew;
   return(0);
}

//
// ComputeHeight -- recursively compute the height of the tree
//
int ComputeHeight(TreeNodeType *pTreeRoot)
{
	if (pTreeRoot==NULL) return(0);
 	int a,b;
	a = ComputeHeight (pTreeRoot->pTreeNext);
	b = ComputeHeight (pTreeRoot->pTreeLeft); 
	if (pTreeRoot->pTreeLeft!=NULL) b = b + 1;
	if (a>b) return (a); else return(b);
}

/*
 * CountLeafNode.cpp
 * recursively counts the number of leaf nodes
 *******************************************************
 *           written by Park Hays, 12-14-96            *
 *******************************************************
 */
int CountLeafNode(TreeNodeType *pTreeRoot)
{
 if (pTreeRoot==NULL) return(0);
 if (pTreeRoot->pTreeLeft==NULL)
return(1+CountLeafNode(pTreeRoot->pTreeNext));

 return(CountLeafNode(pTreeRoot->pTreeNext)+
        CountLeafNode(pTreeRoot->pTreeLeft));
}


// CountNode()
// Counts the number of nodes in the tree passed in pTreeRoot.
// written by Robert Scaramangos */
//


int CountNode( TreeNodeType *pTreeRoot ) {
   if ( pTreeRoot != NULL ) {
if (pTreeRoot==NULL) return(0);
      static int counter=0;
      
      counter++;
      CountNode( pTreeRoot->pTreeLeft );
      CountNode( pTreeRoot->pTreeNext );
        
      return( counter );
   }
}
  
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


/*
 * FindNode -- recursively find the node with the given value, return NULL
 *      on failure
 */
TreeNodeType *FindNode(TreeNodeType *pTreeRoot, char chNode)
{
   TreeNodeType *temp;
   if (pTreeRoot==NULL) return(NULL);
   if (pTreeRoot->chNode==chNode) return(pTreeRoot);
   if ((temp=FindNode(pTreeRoot->pTreeNext,chNode))!=NULL) return(temp);
   if ((temp=FindNode(pTreeRoot->pTreeLeft,chNode))!=NULL) return(temp);
   return(NULL);
}

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


/* written by Robert Scaramangos */

/*
* ShowTree()
** Preconditions: wLevel is set to the number of spaces to be displayed at
first
level.
**
** Prints out the tree with spaces before each left node.
*/
        
void ShowTree( TreeNodeType *pTreeRoot, int wLevel ) {

   if ( pTreeRoot != NULL ) {
      int i;

      for(i=0; i<wLevel; ++i)
         cout << " ";
      cout << pTreeRoot->chNode << "\n";
      ShowTree( pTreeRoot->pTreeLeft, ++wLevel );
      ShowTree( pTreeRoot->pTreeNext, --wLevel );
   }
} 


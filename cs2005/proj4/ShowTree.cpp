#include "proj4tree.h"
#include "iostream.h"

/* written by Robert Scaramangos */

/*
* ShowTree()
** Preconditions: wLevel is set to the number of spaces to be displayed at first
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


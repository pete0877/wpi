#include "proj4tree.h"

/* written by Robert Scaramangos */

/* CountNode()
** Counts the number of nodes in the tree passed in pTreeRoot.
*/


int CountNode( TreeNodeType *pTreeRoot ) {
   if ( pTreeRoot != NULL ) {
      static int counter=0;
      
      ++counter;
      CountNode( pTreeRoot->pTreeLeft );
      CountNode( pTreeRoot->pTreeNext );
        
      return( counter );
   }
}
  


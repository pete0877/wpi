// MoveDecision.h
//

#if !defined(MoveDecision_h)
#define MoveDecision_h

/*
 * MoveDecision Structure encapsulates any informaiton that the
 * system needs to return results back to the game client (e.g. the
 * move string).
 */

#include "resource.h"
#include <afx.h>
#include "MoveStatus.h"


typedef struct tagMoveDecision {
    CString	move;
    float	pctCompleted;
    int		gamestatus;	
} MoveDecision;


#endif /* MoveDecision_h */

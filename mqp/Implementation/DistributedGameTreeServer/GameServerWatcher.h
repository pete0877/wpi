// GameServerWatcher.h
//

#if !defined(GameServerWatcher_h)
#define GameServerWatcher_h

#include "resource.h"

/////////////////////////////////////////////////////////////////////////////
// GameServerWatcher - abstract class used by the GameServer class to 
// send messages to the watchers.

class GameServerWatcher {
public:
	virtual void Notify(CString message) = 0;
};

#endif
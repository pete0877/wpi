// HelperRegistry.h
//

#if !defined(HelperRegistry_h)
#define HelperRegistry_h

/*
 * HelperRegistry Class provides DCom interface for helpers to
 * register and unregister with the server. It stores references to
 * all known helpers.
 */

#include "resource.h"
#include <afxmt.h>
#include "GameType.h"
#include "Helper.h"


class HelperRegistry 
{
public:
	void unregisterHelper(unsigned char *address);
	void registerHelper(unsigned char *address, GameTypeData data);
	HelperRegistry();
    vector<Helper*> getHelpers(GameType gametype);
    vector<GameType> getAllGameTypes();
    boolean isGameImplemented(const GameType& gametype);
    void returnHelpers(vector<Helper*> returnedhelpers);
	void setLogContext(LogContext* new_logContext);
    ~HelperRegistry();

protected:
	LogContext*			logContext;
	void				log(CString event);
    vector<Helper*>		helpers;
    vector<int>			refereceCounts;
    CCriticalSection	helpersListLock;	
    long				nextHelperID;

private:


};

#endif /* HelperRegistry_h */

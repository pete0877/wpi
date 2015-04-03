// GameServerConfiguration.h
//

#if !defined(GameServerConfiguration_h)
#define GameServerConfiguration_h

/*
 * GameServerConfiguration Class is used to store parameters that
 * customize functionality of the GameServer Class.
 */

#include "resource.h"
#include <afx.h>

class CFile;

class GameServerConfiguration
{
public:

    GameServerConfiguration();
    GameServerConfiguration(const GameServerConfiguration& new_gameserverconfiguration);
    int getPort();
    void setPort(int new_port);
    CString getServerName();
    void setServerName(const CString& new_servername);
    CString getServerDescription();
    void setServerDescription(const CString& new_serverdescription);
    CString getServerOwner();
    void setServerOwner(const CString& new_serverowner);
	int getMaxClients();
    void setMaxClients(int new_max);
	int getMaxClientsPerGameType();
    void setMaxClientsPerGameType(int new_max);
	long getMaxDecisionTime();
    void setMaxDecisionTime(long new_max);
	int getMaxGamePly();
    void setMaxGamePly(int new_max);
    CString getWelcomeMessage();
    void save(const CFile& file);
    void load(const CFile& filepath);
    const GameServerConfiguration& operator=(const GameServerConfiguration& new_gameserverconfiguration);
    
protected:

    int					port;
    CString				serverName;
    CString				serverDescription;
    CString				serverOwner;
	int					maxClients;
	int					maxClientsPerGameType;
	long				maxDecisionTime;
	int					maxGamePly;
};

#endif /* GameServerConfiguration_h */

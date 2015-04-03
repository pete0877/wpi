// EngineConfiguration.h
//

#if !defined(EngineConfiguration_h)
#define EngineConfiguration_h

/*
 * EngineConfiguration Class stores customized settings for the
 * functionality of the Engine Class.
 */

#include "resource.h"
#include <afx.h>


class CFile;

class EngineConfiguration
{
public:

    EngineConfiguration();
    EngineConfiguration(const EngineConfiguration& new_engineconfiguration);
    void save(const CFile& file);
    void load(const CFile& file);
    const EngineConfiguration& operator=(const EngineConfiguration& new_engineconfiguration);

protected:

    CDumpContext*	logContext;

private:


};

#endif /* EngineConfiguration_h */

// EngineConfiguration.cpp
//

#include "stdafx.h"
#include "EngineConfiguration.h"
#include <afx.h>

EngineConfiguration::EngineConfiguration()
{
	logContext = NULL;
}

EngineConfiguration::EngineConfiguration(const EngineConfiguration& new_engineconfiguration)
{		
	logContext = new_engineconfiguration.logContext;
}

void EngineConfiguration::save(const CFile& file)
{
}

void EngineConfiguration::load(const CFile& file)
{
}

const EngineConfiguration& EngineConfiguration::operator=(const EngineConfiguration& new_engineconfiguration)
{
	logContext = new_engineconfiguration.logContext;
	return *this;
}

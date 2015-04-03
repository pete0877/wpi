#ifndef HLPPRIMENO_INCLUDED
#define HLPPRIMENO_INCLUDED

#include "Types.h"

#ifdef HLPPRIMENO_EXPORTS
	#define HLPPRIMENO_API __declspec(dllexport)
#else
	#define HLPPRIMENO_API __declspec(dllimport)
#endif


/////////////////////////////////////////////////////////////////////////////
// HlpPrimeNo API

extern "C" HLPPRIMENO_API GameTypeData gameTypeData;

extern "C" HLPPRIMENO_API void evaluate (/*[in]*/ GameState gs, /*[in]*/ LevelType level, /*[in]*/ int ply, /*[in]*/ int timelimit, /*[out]*/ int *quality, /*[out]*/ HelperPerformanceData *data);

extern "C" HLPPRIMENO_API void split(/*[in]*/ GameState gs, /*[in]*/ LevelType level,/*[in]*/ int maxSize,/*[out]*/ int *actualSize,/*[out]*/ Move *moves);

extern "C" HLPPRIMENO_API void getQuickMove(/*[in]*/ GameState gs, /*[in]*/ LevelType level,/*[in]*/ int maxSize,/*[out]*/ int *actualSize,/*[out]*/ Move *moves);

/////////////////////////////////////////////////////////////////////////////

#endif // HLPPRIMENO_INCLUDED

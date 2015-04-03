/////////////////////////////////////////////////////////////////////////////
//
// File Name:	Plugin.h
// Description:	Interface of the Plugin class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef PLUGIN_INCLUDED
#define PLUGIN_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "Helper_i.h"

typedef void (*fxEvaluate)(
				GameState,
				LevelType,
				int,
				int,
				int *,
				HelperPerformanceData *
			);
typedef void (*fxSplit)(
				GameState,
				LevelType,
				int,
				int *,
				Move *
			);
typedef void (*fxGetQuickMove)(
				GameState,
				LevelType,
				int,
				int *,
				Move *
			);

/////////////////////////////////////////////////////////////////////////////
// Class Plugin

class Plugin  
{
// Construction
public:
	Plugin(CString pluginDll);

// Attributes
public:
	CString m_pluginDll;

	GameTypeData *gameTypeData;
	fxEvaluate evaluate;
	fxSplit split;
	fxGetQuickMove getQuickMove;

// Implementation
public:

protected:
	HMODULE m_module;
};

/////////////////////////////////////////////////////////////////////////////

#endif // PLUGIN_INCLUDED

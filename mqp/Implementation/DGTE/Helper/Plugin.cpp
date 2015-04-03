/////////////////////////////////////////////////////////////////////////////
//
// File Name:	Plugin.cpp
// Description:	Defines the class behaviors for the application.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#include "StdAfx.h"
#include "Exceptions.h"
#include "Plugin.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

//////////////////////////////////////////////////////////////////////
// Plugin implementation

Plugin::Plugin(CString pluginDll)
{
	CString pluginPath = "Plugins\\" + pluginDll;
	
	m_pluginDll = pluginDll;
	m_module = ::LoadLibrary(pluginPath);
	
	if( m_module == NULL )
		throw PluginNotFoundException("Could not load " + pluginDll + ".");

	if( (gameTypeData = (GameTypeData *) ::GetProcAddress(m_module, "gameTypeData")) == NULL )
		throw InvalidPluginException("Could not find structure GameTypeData in " + pluginDll + ".");

	if( (evaluate = (fxEvaluate) ::GetProcAddress(m_module, "evaluate")) == NULL )
		throw InvalidPluginException("Could not find method evaluate() in " + pluginDll + ".");

	if( (split = (fxSplit) ::GetProcAddress(m_module, "split")) == NULL )
		throw InvalidPluginException("Could not find method split() in " + pluginDll + ".");

	if( (getQuickMove = (fxGetQuickMove) ::GetProcAddress(m_module, "getQuickMove")) == NULL )
		throw InvalidPluginException("Could not find method getQuickMove() in " + pluginDll + ".");
}

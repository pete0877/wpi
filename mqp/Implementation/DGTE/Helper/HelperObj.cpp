/////////////////////////////////////////////////////////////////////////////
//
// File Name:	HelperObj.cpp
// Description:	Defines the class behaviors for the application.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#include "StdAfx.h"
#include "Helper.h"
#include "HelperObj.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// HelperObj implementation

HelperObj::HelperObj()
{
	// Note:
	//
	// this constructor is never called unless the Helper has a valid plugin
	// and has been registered with the HelperRegistry
	// therefore, when the object is constructed it will always get a valid
	// plugin

	// This should actually be passed in the constructor, however since 
	// at this point we are using the standard class factory, then we have 
	// to obtain it ourselves in the constructor.

	theApp.LogMessage(_T("Server requested a helper object."));

	m_plugin = theApp.GetPlugin();
}

STDMETHODIMP HelperObj::evaluate(/*[in]*/ GameState gs, /*[in]*/ LevelType level, /*[in]*/ int ply, /*[in]*/ int timelimit, /*[out]*/ int *quality, /*[out]*/ HelperPerformanceData *data)
{
	theApp.LogMessage(_T("Helper evaluate() method invoked."));

	m_plugin->evaluate(gs, level, ply, timelimit, quality, data);

	return S_OK;
}

STDMETHODIMP HelperObj::split(/*[in]*/ GameState gs, /*[in]*/ LevelType level,/*[in]*/ int maxSize,/*[out]*/ int *actualSize,/*[out]*/ Move *moves)
{
	//AFX_MANAGE_STATE(AfxGetStaticModuleState())
	theApp.LogMessage(_T("Helper split() method invoked."));

	m_plugin->split(gs, level, maxSize, actualSize, moves);

	return S_OK;
}

STDMETHODIMP HelperObj::getQuickMove(/*[in]*/ GameState gs, /*[in]*/ LevelType level,/*[in]*/ int maxSize,/*[out]*/ int *actualSize,/*[out]*/ Move *moves)
{
	theApp.LogMessage(_T("Helper getQuickMove() method invoked."));

	m_plugin->getQuickMove(gs, level, maxSize, actualSize, moves);

	return S_OK;
}


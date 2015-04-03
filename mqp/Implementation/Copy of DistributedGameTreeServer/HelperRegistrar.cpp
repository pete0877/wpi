// HelperRegistrar.cpp : Implementation of CHelperRegistrar
#include "stdafx.h"
#include "DistributedGameTreeServer.h"
#include "HelperRegistrar.h"
#include "GameServer.h"
#include "Engine.h"
#include "Types.h"

/////////////////////////////////////////////////////////////////////////////
// CHelperRegistrar


STDMETHODIMP CHelperRegistrar::registerHelper(unsigned char *address, GameTypeData data)
{
	AFX_MANAGE_STATE(AfxGetStaticModuleState())
	this->engineHelperRegistry->registerHelper(address, data);
	return S_OK;
}

STDMETHODIMP CHelperRegistrar::unregisterHelper(unsigned char *address)
{
	AFX_MANAGE_STATE(AfxGetStaticModuleState())
	this->engineHelperRegistry->unregisterHelper(address);
	return S_OK;
}

CHelperRegistrar::CHelperRegistrar() {
	engineHelperRegistry = theApp.getGameServer()->getEngine()->getHelperRegistry();
}

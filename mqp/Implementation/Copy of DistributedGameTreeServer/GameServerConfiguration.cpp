// GameServerConfiguration.cpp
//
// TODOS:
//   save() & load()

#include "stdafx.h"
#include "GameServerConfiguration.h"
#include <afx.h>

GameServerConfiguration::GameServerConfiguration()
{
	port					= 0;
    serverName				= "";
    serverDescription		= "";
	serverOwner				= "";
	maxClients				= 0;
	maxClientsPerGameType	= 0;
	maxDecisionTime			= 0;
	maxGamePly				= 0;
}

GameServerConfiguration::GameServerConfiguration(const GameServerConfiguration& new_gameserverconfiguration)
{
	port					= new_gameserverconfiguration.port;
    serverName				= new_gameserverconfiguration.serverName;
    serverDescription		= new_gameserverconfiguration.serverDescription;
	serverOwner				= new_gameserverconfiguration.serverOwner;
	maxClients				= new_gameserverconfiguration.maxClients;
	maxClientsPerGameType	= new_gameserverconfiguration.maxClientsPerGameType;
	maxDecisionTime			= new_gameserverconfiguration.maxDecisionTime;
	maxGamePly				= new_gameserverconfiguration.maxGamePly;
}

int GameServerConfiguration::getPort()
{
    return port;
}

void GameServerConfiguration::setPort(int new_port)
{
	port = new_port;
}

CString GameServerConfiguration::getServerName()
{
	return this->serverName;
}

void GameServerConfiguration::setServerName(const CString& new_servername)
{
	this->serverName = new_servername;
}

CString GameServerConfiguration::getServerDescription()
{	
	return this->serverDescription;
}

void GameServerConfiguration::setServerDescription(const CString& new_serverdescription)
{
	this->serverDescription = new_serverdescription;
}

CString GameServerConfiguration::getServerOwner()
{
	return this->serverOwner;
}

void GameServerConfiguration::setServerOwner(const CString& new_serverowner)
{
	this->serverOwner = new_serverowner;
}

int GameServerConfiguration::getMaxClients() 
{
	return this->maxClients;
}

void GameServerConfiguration::setMaxClients(int new_max) 
{
	this->maxClients = new_max;
}

int GameServerConfiguration::getMaxClientsPerGameType() 
{
	return this->maxClientsPerGameType;
}

void GameServerConfiguration::setMaxClientsPerGameType(int new_max) 
{
	this->maxClientsPerGameType = new_max;
}

long GameServerConfiguration::getMaxDecisionTime() 
{
	return this->maxDecisionTime;
}

void GameServerConfiguration::setMaxDecisionTime(long new_max) 
{
	this->maxDecisionTime = new_max;
}

int GameServerConfiguration::getMaxGamePly() 
{
	return this->maxGamePly;
}

void GameServerConfiguration::setMaxGamePly(int new_max) 
{
	this->maxGamePly = new_max;
}

CString GameServerConfiguration::getWelcomeMessage()
{
	CString result = "";
	result = result + "SERVER NAME: " + this->serverName + "\n";
	result = result + "OWNER      : " + this->serverOwner + "\n";
	result = result + "DESCRIPTION: " + this->serverDescription + "\n";
	return result;
}

void GameServerConfiguration::save(const CFile& file)
{
}

void GameServerConfiguration::load(const CFile& filepath)
{
}

const GameServerConfiguration& GameServerConfiguration::operator=(const GameServerConfiguration& new_gameserverconfiguration) 
{
	port					= new_gameserverconfiguration.port;
    serverName				= new_gameserverconfiguration.serverName;
    serverDescription		= new_gameserverconfiguration.serverDescription;
	serverOwner				= new_gameserverconfiguration.serverOwner;
	maxClients				= new_gameserverconfiguration.maxClients;
	maxClientsPerGameType	= new_gameserverconfiguration.maxClientsPerGameType;
	maxDecisionTime			= new_gameserverconfiguration.maxDecisionTime;
	maxGamePly				= new_gameserverconfiguration.maxGamePly;
	return *this;
}

// GameType.cpp
//

#include "stdafx.h"
#include "GameType.h"
#include <string.h>

GameType::GameType()
{
	strcpy((char*) this->gameTypeData.gameName,"");
	strcpy((char*) this->gameTypeData.gameVersion,"");
	strcpy((char*) this->gameTypeData.gameDescription,"");
	strcpy((char*) this->gameTypeData.companyName,"");
	strcpy((char*) this->gameTypeData.legalCopyright,"");
	strcpy((char*) this->gameTypeData.legalTrademarks,"");
	strcpy((char*) this->gameTypeData.gameEncoding,"");
	this->gameTypeData.moveListSize=0;
	this->gameTypeData.moveSize = 0;
	this->gameTypeData.gameStateSize = 0;
}

GameType::GameType(const GameType& gametype)
{
	gameTypeData = gametype.gameTypeData;
}

GameType::GameType(const GameTypeData& new_gametypedata)
{
	gameTypeData = new_gametypedata;
}

CString GameType::getGameName()
{
	CString result = this->gameTypeData.gameName;
	return result;
}

void GameType::setGameName(CString new_gamename)
{	
	strcpy ((char*) this->gameTypeData.gameName, new_gamename.Left(63).GetBuffer(0));	
}

CString GameType::getCompanyName()
{
	CString result = this->gameTypeData.companyName;
	return result;
}

void GameType::setCompanyName(CString new_companyname)
{
	strcpy ((char*) this->gameTypeData.companyName, new_companyname.Left(127).GetBuffer(0));
}

CString GameType::getGameVersion()
{
	CString result = this->gameTypeData.gameVersion;
	return result;
}

void GameType::setGameVersion(CString new_gameversion)
{	
	strcpy ((char*) this->gameTypeData.gameVersion, new_gameversion.Left(63).GetBuffer(0));
}

CString GameType::getLegalCopyright()
{
	CString result = this->gameTypeData.legalCopyright;
	return result;
}

void GameType::setLegalCopyright(CString new_legalcopyright)
{	
	strcpy ((char*) this->gameTypeData.legalCopyright, new_legalcopyright.Left(255).GetBuffer(0));
}

CString GameType::getLegalTrademarks()
{
	CString result = this->gameTypeData.legalTrademarks;
	return result;
}

void GameType::setLegalTrademarks(CString new_legaltrademarks)
{
	strcpy ((char*) this->gameTypeData.legalTrademarks, new_legaltrademarks.Left(255).GetBuffer(0));
}

CString GameType::getGameDescription()
{
	CString result = this->gameTypeData.gameDescription;
	return result;
}

void GameType::setGameDescription(CString new_gamedescription)
{	
	strcpy ((char*) this->gameTypeData.gameDescription, new_gamedescription.Left(255).GetBuffer(0));
}

CString GameType::getGameEncoding()
{
	CString result = this->gameTypeData.gameEncoding;
	return result;
}

void GameType::setGameEncoding(CString new_gameencoding)
{
	strcpy ((char*) this->gameTypeData.gameEncoding, new_gameencoding.Left(255).GetBuffer(0));
}

CString GameType::toString()
{
	CString result = "";
	result = result + "NAME" + "\n" + this->getGameName() + "\n";
	result = result + "VERSION"  + "\n" +  this->getGameVersion() + "\n";
	result = result + "DESCRIPTION"  + "\n" +  this->getGameDescription() + "\n";
	result = result + "ENCODING"  + "\n" +  this->getGameEncoding() + "\n";
	result = result + "COMPANY"  + "\n" +  this->getCompanyName() + "\n";
	result = result + "COPYRIGHT"  + "\n" +  this->getLegalCopyright() + "\n";
	result = result + "TRADEMARKS"  + "\n" +  this->getLegalTrademarks() + "\n";
	return result;
}

const GameType& GameType::operator=(const GameType& gametype)
{
	strcpy((char*) this->gameTypeData.gameName,(char*) gametype.gameTypeData.gameName);
	strcpy((char*) this->gameTypeData.gameVersion,(char*) gametype.gameTypeData.gameVersion);
	strcpy((char*) this->gameTypeData.gameDescription,(char*) gametype.gameTypeData.gameDescription);
	strcpy((char*) this->gameTypeData.companyName,(char*) gametype.gameTypeData.companyName);
	strcpy((char*) this->gameTypeData.legalCopyright,(char*) gametype.gameTypeData.legalCopyright);
	strcpy((char*) this->gameTypeData.legalTrademarks,(char*) gametype.gameTypeData.legalTrademarks);
	strcpy((char*) this->gameTypeData.gameEncoding,(char*) gametype.gameTypeData.gameEncoding);
	this->gameTypeData.moveListSize		= gametype.gameTypeData.moveListSize;
	this->gameTypeData.moveSize			= gametype.gameTypeData.moveSize;
	this->gameTypeData.gameStateSize	= gametype.gameTypeData.gameStateSize;
	return *this;
}

boolean GameType::operator==(const GameType& gametype)
{
	if (strcmp((char*) this->gameTypeData.gameName, (char*) gametype.gameTypeData.gameName))				return false;
	if (strcmp((char*) this->gameTypeData.gameVersion,(char*) gametype.gameTypeData.gameVersion))			return false;
	if (strcmp((char*) this->gameTypeData.gameDescription,(char*) gametype.gameTypeData.gameDescription))	return false;
	if (strcmp((char*) this->gameTypeData.gameEncoding,(char*) gametype.gameTypeData.gameEncoding))			return false;
	if (strcmp((char*) this->gameTypeData.companyName,(char*) gametype.gameTypeData.companyName))			return false;
	if (strcmp((char*) this->gameTypeData.legalCopyright,(char*) gametype.gameTypeData.legalCopyright))		return false;
	if (strcmp((char*) this->gameTypeData.legalTrademarks,(char*) gametype.gameTypeData.legalTrademarks))	return false;		
	return true;
}

GameType::~GameType()
{}

int GameType::getGameStateSize()
{	
	return this->gameTypeData.gameStateSize;
}

void GameType::setGameStateSize(int new_gamestatesize)
{
	new_gamestatesize = new_gamestatesize;
}

int GameType::getMoveSize()
{
	return this->gameTypeData.moveSize;	
}

void GameType::setMoveSize(int new_movesize)
{
	this->gameTypeData.moveSize = new_movesize;	
}

int GameType::getMoveListSize()
{
	return this->gameTypeData.moveListSize;	
}

void GameType::setMoveListSize(int new_movelistsize)
{
	this->gameTypeData.moveListSize = new_movelistsize;
}



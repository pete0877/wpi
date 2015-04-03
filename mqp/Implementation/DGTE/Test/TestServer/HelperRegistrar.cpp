// HelperRegistrar.cpp : Implementation of CHelperRegistrar
#include "stdafx.h"
#include "TestServer.h"
#include "HelperRegistrar.h"
#include "..\..\Helper\Helper_i.h"
#include "..\..\Helper\Helper_i.c"
#include <comdef.h>

/////////////////////////////////////////////////////////////////////////////
// CHelperRegistrar

CHelperRegistrar::CHelperRegistrar()
{
	log << _T("RegisterHelper object instantiated.") << endl;
}

CHelperRegistrar::~CHelperRegistrar() 
{
	log << _T("RegisterHelper object deleted.") << endl;	
}

STDMETHODIMP CHelperRegistrar::registerHelper(unsigned char *address, GameTypeData data)
{
	log << _T("registerHelper() invoked with:") << endl;
	log << _T("   address              = ") << address << endl;
	log << _T("   data.gameName        = ") << data.gameName << endl;
	log << _T("   data.gameVersion     = ") << data.gameVersion << endl;
	log << _T("   data.gameDescription = ") << data.gameDescription << endl;
	log << _T("   data.companyName     = ") << data.companyName << endl;
	log << _T("   data.legalCopyright  = ") << data.legalCopyright << endl;
	log << _T("   data.legalTrademarks = ") << data.legalTrademarks << endl;
	log << _T("   data.gameEncoding    = ") << data.gameEncoding << endl;

	// this is kinda illegal because the helper is being registered at this point
	// but we don't really care
	 goPlayWithHelper(address, data);

	return S_OK;
}

STDMETHODIMP CHelperRegistrar::unregisterHelper(unsigned char *address)
{
	log << _T("unregisterHelper() invoked with:") << endl;
	log << _T("   address              = ") << address << endl;

	return S_OK;
}

void CHelperRegistrar::goPlayWithHelper(unsigned char *address, GameTypeData data)
{
	// Connect to the helper.
	//
	IHelper *pHelper;
	HRESULT hr;

    COSERVERINFO cs;
    memset(&cs, 0, sizeof(cs));
    cs.pwszName = _bstr_t(_T("LETHAL")); // hardcoded, fuck it

    MULTI_QI qi[1];
    memset(qi, 0, sizeof(qi)); 

    qi[0].pIID = &IID_IHelper;

	hr = CoCreateInstanceEx(CLSID_Helper, NULL, CLSCTX_SERVER, &cs, 1, qi);

	if( FAILED(hr) ) {
		_com_error e(hr);
		log << e.ErrorMessage();
	}

	// Whoo hoo, we are in the business.
	//
	pHelper = (IHelper *) qi[0].pItf;

	// First, play with evaluate.
	//
	int gameStateSize = 100;
	int moveSize = 100;

	GameState gs;
	gs.maxSize = gameStateSize;
	gs.actualSize = 0;
	gs.gamestate = new unsigned char[gameStateSize];
	
	strcpy((char *) gs.gamestate, "sebby");
	gs.actualSize = 5;

	LevelType level = MINIMAZING;

	int ply = 7;

	int timelimit = 300;

	int quality;
	int *pQuality = &quality;

	HelperPerformanceData perfData;

	hr = pHelper->evaluate(gs, level, ply, timelimit, pQuality, &perfData);

	if( FAILED(hr) ) {
		_com_error e(hr);
		log << e.ErrorMessage();
	}

	// Nice, lemme play with getQuickMove now.
	//
	Move move;
	move.maxSize = moveSize;
	move.actualSize = 0;
	move.move = new unsigned char[moveSize];
	move.gamestate.maxSize = gameStateSize;
	move.gamestate.actualSize = 0;
	move.gamestate.gamestate = new unsigned char[gameStateSize];
	int actualSize1 = 1;
	hr = pHelper->getQuickMove(gs, level, 1, &actualSize1, &move);

	if( FAILED(hr) ) {
		_com_error e(hr);
		log << e.ErrorMessage();
	}

	// Sweet...
	//
	int moveListSize = 100;
	Move *moves = new Move[moveListSize];
	for(int i = 0; i < moveListSize; i++) {
		moves[i].maxSize = moveSize;
		moves[i].actualSize = 0;
		moves[i].move = new unsigned char[moveSize];
		moves[i].gamestate.maxSize = gameStateSize;
		moves[i].gamestate.actualSize = 0;
		moves[i].gamestate.gamestate = new unsigned char[gameStateSize];	
	}

	int actualSize = moveListSize;

	hr = pHelper->split(gs, level, moveListSize, &actualSize, moves);

	if( FAILED(hr) ) {
		_com_error e(hr);
		log << e.ErrorMessage();
	}
	
	// Alright, enough play, release the interface.
	//
	pHelper->Release();

	// Be a good boy and clean up after yourself.
	//
	delete [] gs.gamestate;
	delete [] move.move;
	delete [] move.gamestate.gamestate;
	for(int j = 0; j < moveListSize; j++) {
		delete [] moves[j].move;
		delete [] moves[j].gamestate.gamestate;
	}
}

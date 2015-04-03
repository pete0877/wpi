/////////////////////////////////////////////////////////////////////////////////////////
// PrimeNumbers.cpp : Defines the entry point for the DLL application.
//

#include "stdafx.h"
#include "PrimeNumbers.h"
#include <string.h>
#include <stdio.h>

/////////////////////////////////////////////////////////////////////////////////////////
BOOL APIENTRY DllMain( HANDLE hModule, DWORD  ul_reason_for_call, LPVOID lpReserved )
{
    switch (ul_reason_for_call)
	{
		case DLL_PROCESS_ATTACH:
		case DLL_THREAD_ATTACH:
		case DLL_THREAD_DETACH:
		case DLL_PROCESS_DETACH:
			break;
    }
    return TRUE;
}


/////////////////////////////////////////////////////////////////////////////////////////
GameTypeData gameTypeData =
{
	"Prime Number Checker",
	"1.0",
	"Checks if a number is a prime number.",
	"Worcester Polytechnic Institute",
	"Copyright © 1999-2000 Worcester Polytechnic Institute",
	"", 
	"ASCII",
	100, 
	100, 
	100
};


/////////////////////////////////////////////////////////////////////////////////////////
bool isPrime(int number, int loBound, int hiBound)
{
	int reminder;
	if (number < 0) number *= -1;
	if ((number == 1) && (number == 2)) return true;
	if (loBound<1) loBound = 2;
	if (hiBound>=number) hiBound = number - 1;
	for(int divider = loBound; divider <= hiBound; divider++) {		
		reminder = number % divider;
		if( reminder == 0 ) return false;
	}
	return true;
}	

/////////////////////////////////////////////////////////////////////////////////////////
bool string2number(char* string, long& number)
{
	long a = strlen(string);
	long sum = 0;
	long tmp;
	for (int b = 0; b < a; b++) {
		sum *= 10;
		tmp = (long) (string[b] - '0');
		if (tmp<0 || tmp>9) return false;
		sum += tmp;
	}
	number = sum;
	return true;
}	


/////////////////////////////////////////////////////////////////////////////////////////
void evaluate(/*[in]*/ GameState gs, /*[in]*/ LevelType level, /*[in]*/ int ply, /*[in]*/ int timelimit, /*[out]*/ int *quality, /*[out]*/ HelperPerformanceData *data)
{
	int YES, NO;
	if (level==MAXIMAXING) {
		YES = 1;
		NO  = 0;
	} else {
		YES = 0;
		NO  = 1;
	}

	*quality = NO;
	char* string = (char*) gs.gamestate;
	if (strlen(string)==0) return;

	int a;
	int b = strlen(string);
	int commas[10];
	int commas_idx=0;
	// find any commas:
	for (a=0; a < b; a++) 
		if ((string[a]==',') && (commas_idx<10)) commas[commas_idx++]=a;

	long loBound, hiBound;
	long number;
	bool result;
	if (commas_idx==0) {
		// no commas
		result = string2number(string, number);
		if (!result) return;
		if (number==0) {
			// this is a special case that we use only for 
			// indicating that the number is prime. It's 
			// a work-around the interface.
			*quality = YES;
			return;
		}
		loBound = 2;
		hiBound = number - 1;
	} else {
		// commas found: number,lo,hi -> number,lo\0hi
		if (commas_idx!=2) return;
		string[commas[0]] = 0;
		string[commas[1]] = 0;

		result = string2number(string, number);
		if (!result) return;

		result = string2number(&string[commas[0]+1], loBound);
		if (!result) return;

		result = string2number(&string[commas[1]+1], hiBound);
		if (!result) return;
	}

	if (isPrime(number, loBound, hiBound)) { *quality = YES; } // yes, it is prime
	return;
}

/////////////////////////////////////////////////////////////////////////////////////////
void split(/*[in]*/ GameState gs, /*[in]*/ LevelType level,/*[in]*/ int maxSize,/*[out]*/ int *actualSize,/*[out]*/ Move *moves)
{
	int branching = 11;
	if (branching>maxSize) branching = maxSize;
	*actualSize = 0;

	int newLevel = MAXIMAXING;
	if (level==MAXIMAXING) newLevel = MINIMAZING;
	int YES, NO;
	if (newLevel==MAXIMAXING) {
		YES = 1;
		NO  = 0;
	} else {
		YES = 0;
		NO  = 1;
	}

	// find out the game current game state range
	char* string = (char*) gs.gamestate;
	if (strlen(string)==0) return;

	int a;
	int b = strlen(string);
	int commas[10];
	int commas_idx=0;
	// find any commas:
	for (a=0; a < b; a++) 
		if ((string[a]==',') && (commas_idx<10)) commas[commas_idx++]=a;

	long loBound, hiBound;
	long number;
	bool result;
	if (commas_idx==0) {
		// no commas
		result = string2number(string, number);
		if (!result) return;
		loBound = 2;
		hiBound = number - 1;
	} else {
		// commas found: number,lo,hi -> number,lo\0hi
		if (commas_idx!=2) return;
		string[commas[0]] = 0;
		string[commas[1]] = 0;

		result = string2number(string, number);
		if (!result) return;

		result = string2number(&string[commas[0]+1], loBound);
		if (!result) return;

		result = string2number(&string[commas[1]+1], hiBound);
		if (!result) return;
	}

	// now we know that we have following variables set:
	//   loBound
	//   hiBound
	//   number

	// first we need to mark the special move (first one)
	// it will get picked if no other moves are picked 
	// which would mean that the number is prime in that range:
	moves[0].quality = YES;
	moves[0].status = 0; // continue
	strcpy((char*) moves[0].move, "YES");
	moves[0].actualSize = 4;
	strcpy((char*) moves[0].gamestate.gamestate, "0");
	moves[0].gamestate.actualSize = 2;	

	// now we make the moves:
	char tmp[500];
	for (a = 1; a < branching ; a++) {
		int chunk = (hiBound  - loBound) / (branching - 1);
		int new_loBound = (a - 1) * chunk + loBound;
		int new_hiBound = (a - 0) * chunk + loBound;
		if (a==branching-1) new_hiBound = hiBound; // fix round-off errors
		sprintf(tmp,"%d,%d,%d",number, new_loBound, new_hiBound);
		moves[a].quality = NO;
		moves[a].status = 0; // continue
		strcpy((char*) moves[a].move, "NO");
		moves[a].actualSize = 3;
		strcpy((char*) moves[a].gamestate.gamestate, tmp);
		moves[a].gamestate.actualSize = strlen(tmp) + 1;		
	}	
	*actualSize = branching;
	return;
}

/////////////////////////////////////////////////////////////////////////////////////////
void getQuickMove(/*[in]*/ GameState gs, /*[in]*/ LevelType level,/*[in]*/ int maxSize,/*[out]*/ int *actualSize,/*[out]*/ Move *moves)
{
	*actualSize = 0; // no rapid moves
}


/* this ALWAYS GENERATED file contains the definitions for the interfaces */


/* File created by MIDL compiler version 5.01.0164 */
/* at Thu Dec 09 20:30:01 1999
 */
/* Compiler settings for D:\Dev\Dgte\Include\Types.idl:
    Os (OptLev=s), W1, Zp8, env=Win32, ms_ext, c_ext
    error checks: allocation ref bounds_check enum stub_data 
*/
//@@MIDL_FILE_HEADING(  )


/* verify that the <rpcndr.h> version is high enough to compile this file*/
#ifndef __REQUIRED_RPCNDR_H_VERSION__
#define __REQUIRED_RPCNDR_H_VERSION__ 440
#endif

#include "rpc.h"
#include "rpcndr.h"

#ifndef __Types_h__
#define __Types_h__

#ifdef __cplusplus
extern "C"{
#endif 

/* Forward Declarations */ 

void __RPC_FAR * __RPC_USER MIDL_user_allocate(size_t);
void __RPC_USER MIDL_user_free( void __RPC_FAR * ); 

/* interface __MIDL_itf_Types_0000 */
/* [local] */ 

typedef struct  tagGameState
    {
    int actualSize;
    int maxSize;
    /* [length_is][size_is][ref] */ unsigned char __RPC_FAR *gamestate;
    }	GameState;

typedef struct  tagMove
    {
    int actualSize;
    int maxSize;
    /* [length_is][size_is][ref] */ unsigned char __RPC_FAR *move;
    int quality;
    int status;
    GameState gamestate;
    }	Move;

typedef 
enum tagLevelType
    {	MAXIMAXING	= 1,
	MINIMAZING	= 2
    }	LevelType;

typedef struct  tagGameTypeData
    {
    unsigned char gameName[ 64 ];
    unsigned char gameVersion[ 64 ];
    unsigned char gameDescription[ 256 ];
    unsigned char companyName[ 128 ];
    unsigned char legalCopyright[ 256 ];
    unsigned char legalTrademarks[ 256 ];
    unsigned char gameEncoding[ 256 ];
    int gameStateSize;
    int moveSize;
    int moveListSize;
    }	GameTypeData;

typedef struct  tagHelperPerformanceData
    {
    float alphaBetaGain;
    float branchingFactor;
    float evaluationTime;
    float pctCompleted;
    float splitTime;
    float totalWorkTime;
    }	HelperPerformanceData;



extern RPC_IF_HANDLE __MIDL_itf_Types_0000_v0_0_c_ifspec;
extern RPC_IF_HANDLE __MIDL_itf_Types_0000_v0_0_s_ifspec;

/* Additional Prototypes for ALL interfaces */

/* end of Additional Prototypes */

#ifdef __cplusplus
}
#endif

#endif

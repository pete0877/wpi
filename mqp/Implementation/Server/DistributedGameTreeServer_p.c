/* this ALWAYS GENERATED file contains the proxy stub code */


/* File created by MIDL compiler version 5.01.0164 */
/* at Fri Dec 17 16:46:44 1999
 */
/* Compiler settings for E:\MQP\Implementation\Server\DistributedGameTreeServer.idl:
    Oicf (OptLev=i2), W1, Zp8, env=Win32, ms_ext, c_ext
    error checks: allocation ref bounds_check enum stub_data 
*/
//@@MIDL_FILE_HEADING(  )

#define USE_STUBLESS_PROXY


/* verify that the <rpcproxy.h> version is high enough to compile this file*/
#ifndef __REDQ_RPCPROXY_H_VERSION__
#define __REQUIRED_RPCPROXY_H_VERSION__ 440
#endif


#include "rpcproxy.h"
#ifndef __RPCPROXY_H_VERSION__
#error this stub requires an updated version of <rpcproxy.h>
#endif // __RPCPROXY_H_VERSION__


#include "DistributedGameTreeServer_i.h"

#define TYPE_FORMAT_STRING_SIZE   61                                
#define PROC_FORMAT_STRING_SIZE   63                                

typedef struct _MIDL_TYPE_FORMAT_STRING
    {
    short          Pad;
    unsigned char  Format[ TYPE_FORMAT_STRING_SIZE ];
    } MIDL_TYPE_FORMAT_STRING;

typedef struct _MIDL_PROC_FORMAT_STRING
    {
    short          Pad;
    unsigned char  Format[ PROC_FORMAT_STRING_SIZE ];
    } MIDL_PROC_FORMAT_STRING;


extern const MIDL_TYPE_FORMAT_STRING __MIDL_TypeFormatString;
extern const MIDL_PROC_FORMAT_STRING __MIDL_ProcFormatString;


/* Object interface: IUnknown, ver. 0.0,
   GUID={0x00000000,0x0000,0x0000,{0xC0,0x00,0x00,0x00,0x00,0x00,0x00,0x46}} */


/* Object interface: IHelperRegistrar, ver. 0.0,
   GUID={0xB5D4EBC0,0xC7D9,0x4C14,{0xBF,0xCF,0xC9,0xAB,0x4C,0xBC,0xAB,0xB5}} */


extern const MIDL_STUB_DESC Object_StubDesc;


extern const MIDL_SERVER_INFO IHelperRegistrar_ServerInfo;

#pragma code_seg(".orpc")

static const MIDL_STUB_DESC Object_StubDesc = 
    {
    0,
    NdrOleAllocate,
    NdrOleFree,
    0,
    0,
    0,
    0,
    0,
    __MIDL_TypeFormatString.Format,
    1, /* -error bounds_check flag */
    0x20000, /* Ndr library version */
    0,
    0x50100a4, /* MIDL Version 5.1.164 */
    0,
    0,
    0,  /* notify & notify_flag routine table */
    1,  /* Flags */
    0,  /* Reserved3 */
    0,  /* Reserved4 */
    0   /* Reserved5 */
    };

static const unsigned short IHelperRegistrar_FormatStringOffsetTable[] = 
    {
    0,
    34
    };

static const MIDL_SERVER_INFO IHelperRegistrar_ServerInfo = 
    {
    &Object_StubDesc,
    0,
    __MIDL_ProcFormatString.Format,
    &IHelperRegistrar_FormatStringOffsetTable[-3],
    0,
    0,
    0,
    0
    };

static const MIDL_STUBLESS_PROXY_INFO IHelperRegistrar_ProxyInfo =
    {
    &Object_StubDesc,
    __MIDL_ProcFormatString.Format,
    &IHelperRegistrar_FormatStringOffsetTable[-3],
    0,
    0,
    0
    };

CINTERFACE_PROXY_VTABLE(5) _IHelperRegistrarProxyVtbl = 
{
    &IHelperRegistrar_ProxyInfo,
    &IID_IHelperRegistrar,
    IUnknown_QueryInterface_Proxy,
    IUnknown_AddRef_Proxy,
    IUnknown_Release_Proxy ,
    (void *)-1 /* IHelperRegistrar::registerHelper */ ,
    (void *)-1 /* IHelperRegistrar::unregisterHelper */
};

const CInterfaceStubVtbl _IHelperRegistrarStubVtbl =
{
    &IID_IHelperRegistrar,
    &IHelperRegistrar_ServerInfo,
    5,
    0, /* pure interpreted */
    CStdStubBuffer_METHODS
};

#pragma data_seg(".rdata")

#if !defined(__RPC_WIN32__)
#error  Invalid build platform for this stub.
#endif

#if !(TARGET_IS_NT40_OR_LATER)
#error You need a Windows NT 4.0 or later to run this stub because it uses these features:
#error   -Oif or -Oicf, more than 32 methods in the interface.
#error However, your C/C++ compilation flags indicate you intend to run this app on earlier systems.
#error This app will die there with the RPC_X_WRONG_STUB_VERSION error.
#endif


static const MIDL_PROC_FORMAT_STRING __MIDL_ProcFormatString =
    {
        0,
        {

	/* Procedure registerHelper */

			0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/*  2 */	NdrFcLong( 0x0 ),	/* 0 */
/*  6 */	NdrFcShort( 0x3 ),	/* 3 */
#ifndef _ALPHA_
/*  8 */	NdrFcShort( 0x518 ),	/* x86, MIPS, PPC Stack size/offset = 1304 */
#else
			NdrFcShort( 0x528 ),	/* Alpha Stack size/offset = 1320 */
#endif
/* 10 */	NdrFcShort( 0x54c ),	/* 1356 */
/* 12 */	NdrFcShort( 0x8 ),	/* 8 */
/* 14 */	0x6,		/* Oi2 Flags:  clt must size, has return, */
			0x3,		/* 3 */

	/* Parameter address */

/* 16 */	NdrFcShort( 0x10b ),	/* Flags:  must size, must free, in, simple ref, */
#ifndef _ALPHA_
/* 18 */	NdrFcShort( 0x4 ),	/* x86, MIPS, PPC Stack size/offset = 4 */
#else
			NdrFcShort( 0x8 ),	/* Alpha Stack size/offset = 8 */
#endif
/* 20 */	NdrFcShort( 0x4 ),	/* Type Offset=4 */

	/* Parameter data */

/* 22 */	NdrFcShort( 0x8a ),	/* Flags:  must free, in, by val, */
#ifndef _ALPHA_
/* 24 */	NdrFcShort( 0x8 ),	/* x86, MIPS, PPC Stack size/offset = 8 */
#else
			NdrFcShort( 0x10 ),	/* Alpha Stack size/offset = 16 */
#endif
/* 26 */	NdrFcShort( 0x18 ),	/* Type Offset=24 */

	/* Return value */

/* 28 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
#ifndef _ALPHA_
/* 30 */	NdrFcShort( 0x514 ),	/* x86, MIPS, PPC Stack size/offset = 1300 */
#else
			NdrFcShort( 0x520 ),	/* Alpha Stack size/offset = 1312 */
#endif
/* 32 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure unregisterHelper */

/* 34 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 36 */	NdrFcLong( 0x0 ),	/* 0 */
/* 40 */	NdrFcShort( 0x4 ),	/* 4 */
#ifndef _ALPHA_
/* 42 */	NdrFcShort( 0xc ),	/* x86, MIPS, PPC Stack size/offset = 12 */
#else
			NdrFcShort( 0x18 ),	/* Alpha Stack size/offset = 24 */
#endif
/* 44 */	NdrFcShort( 0x0 ),	/* 0 */
/* 46 */	NdrFcShort( 0x8 ),	/* 8 */
/* 48 */	0x6,		/* Oi2 Flags:  clt must size, has return, */
			0x2,		/* 2 */

	/* Parameter address */

/* 50 */	NdrFcShort( 0x10b ),	/* Flags:  must size, must free, in, simple ref, */
#ifndef _ALPHA_
/* 52 */	NdrFcShort( 0x4 ),	/* x86, MIPS, PPC Stack size/offset = 4 */
#else
			NdrFcShort( 0x8 ),	/* Alpha Stack size/offset = 8 */
#endif
/* 54 */	NdrFcShort( 0x4 ),	/* Type Offset=4 */

	/* Return value */

/* 56 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
#ifndef _ALPHA_
/* 58 */	NdrFcShort( 0x8 ),	/* x86, MIPS, PPC Stack size/offset = 8 */
#else
			NdrFcShort( 0x10 ),	/* Alpha Stack size/offset = 16 */
#endif
/* 60 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

			0x0
        }
    };

static const MIDL_TYPE_FORMAT_STRING __MIDL_TypeFormatString =
    {
        0,
        {
			NdrFcShort( 0x0 ),	/* 0 */
/*  2 */	
			0x11, 0x8,	/* FC_RP [simple_pointer] */
/*  4 */	
			0x22,		/* FC_C_CSTRING */
			0x5c,		/* FC_PAD */
/*  6 */	
			0x1d,		/* FC_SMFARRAY */
			0x0,		/* 0 */
/*  8 */	NdrFcShort( 0x40 ),	/* 64 */
/* 10 */	0x2,		/* FC_CHAR */
			0x5b,		/* FC_END */
/* 12 */	
			0x1d,		/* FC_SMFARRAY */
			0x0,		/* 0 */
/* 14 */	NdrFcShort( 0x100 ),	/* 256 */
/* 16 */	0x2,		/* FC_CHAR */
			0x5b,		/* FC_END */
/* 18 */	
			0x1d,		/* FC_SMFARRAY */
			0x0,		/* 0 */
/* 20 */	NdrFcShort( 0x80 ),	/* 128 */
/* 22 */	0x2,		/* FC_CHAR */
			0x5b,		/* FC_END */
/* 24 */	
			0x15,		/* FC_STRUCT */
			0x3,		/* 3 */
/* 26 */	NdrFcShort( 0x50c ),	/* 1292 */
/* 28 */	0x4c,		/* FC_EMBEDDED_COMPLEX */
			0x0,		/* 0 */
/* 30 */	NdrFcShort( 0xffffffe8 ),	/* Offset= -24 (6) */
/* 32 */	0x4c,		/* FC_EMBEDDED_COMPLEX */
			0x0,		/* 0 */
/* 34 */	NdrFcShort( 0xffffffe4 ),	/* Offset= -28 (6) */
/* 36 */	0x4c,		/* FC_EMBEDDED_COMPLEX */
			0x0,		/* 0 */
/* 38 */	NdrFcShort( 0xffffffe6 ),	/* Offset= -26 (12) */
/* 40 */	0x4c,		/* FC_EMBEDDED_COMPLEX */
			0x0,		/* 0 */
/* 42 */	NdrFcShort( 0xffffffe8 ),	/* Offset= -24 (18) */
/* 44 */	0x4c,		/* FC_EMBEDDED_COMPLEX */
			0x0,		/* 0 */
/* 46 */	NdrFcShort( 0xffffffde ),	/* Offset= -34 (12) */
/* 48 */	0x4c,		/* FC_EMBEDDED_COMPLEX */
			0x0,		/* 0 */
/* 50 */	NdrFcShort( 0xffffffda ),	/* Offset= -38 (12) */
/* 52 */	0x4c,		/* FC_EMBEDDED_COMPLEX */
			0x0,		/* 0 */
/* 54 */	NdrFcShort( 0xffffffd6 ),	/* Offset= -42 (12) */
/* 56 */	0x8,		/* FC_LONG */
			0x8,		/* FC_LONG */
/* 58 */	0x8,		/* FC_LONG */
			0x5b,		/* FC_END */

			0x0
        }
    };

const CInterfaceProxyVtbl * _DistributedGameTreeServer_ProxyVtblList[] = 
{
    ( CInterfaceProxyVtbl *) &_IHelperRegistrarProxyVtbl,
    0
};

const CInterfaceStubVtbl * _DistributedGameTreeServer_StubVtblList[] = 
{
    ( CInterfaceStubVtbl *) &_IHelperRegistrarStubVtbl,
    0
};

PCInterfaceName const _DistributedGameTreeServer_InterfaceNamesList[] = 
{
    "IHelperRegistrar",
    0
};


#define _DistributedGameTreeServer_CHECK_IID(n)	IID_GENERIC_CHECK_IID( _DistributedGameTreeServer, pIID, n)

int __stdcall _DistributedGameTreeServer_IID_Lookup( const IID * pIID, int * pIndex )
{
    
    if(!_DistributedGameTreeServer_CHECK_IID(0))
        {
        *pIndex = 0;
        return 1;
        }

    return 0;
}

const ExtendedProxyFileInfo DistributedGameTreeServer_ProxyFileInfo = 
{
    (PCInterfaceProxyVtblList *) & _DistributedGameTreeServer_ProxyVtblList,
    (PCInterfaceStubVtblList *) & _DistributedGameTreeServer_StubVtblList,
    (const PCInterfaceName * ) & _DistributedGameTreeServer_InterfaceNamesList,
    0, // no delegation
    & _DistributedGameTreeServer_IID_Lookup, 
    1,
    2,
    0, /* table of [async_uuid] interfaces */
    0, /* Filler1 */
    0, /* Filler2 */
    0  /* Filler3 */
};

/* this ALWAYS GENERATED file contains the proxy stub code */


/* File created by MIDL compiler version 5.01.0164 */
/* at Thu Dec 16 13:12:04 1999
 */
/* Compiler settings for E:\MQP\Implementation\DGTE\Helper\Helper.idl:
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


#include "Helper_i.h"

#define TYPE_FORMAT_STRING_SIZE   161                               
#define PROC_FORMAT_STRING_SIZE   163                               

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


/* Object interface: IHelper, ver. 0.0,
   GUID={0xC3F69A0C,0x23EF,0x47C6,{0xA2,0xCD,0xDC,0x4C,0xA6,0x4C,0xCA,0x22}} */


extern const MIDL_STUB_DESC Object_StubDesc;


extern const MIDL_SERVER_INFO IHelper_ServerInfo;

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

static const unsigned short IHelper_FormatStringOffsetTable[] = 
    {
    0,
    58,
    110
    };

static const MIDL_SERVER_INFO IHelper_ServerInfo = 
    {
    &Object_StubDesc,
    0,
    __MIDL_ProcFormatString.Format,
    &IHelper_FormatStringOffsetTable[-3],
    0,
    0,
    0,
    0
    };

static const MIDL_STUBLESS_PROXY_INFO IHelper_ProxyInfo =
    {
    &Object_StubDesc,
    __MIDL_ProcFormatString.Format,
    &IHelper_FormatStringOffsetTable[-3],
    0,
    0,
    0
    };

CINTERFACE_PROXY_VTABLE(6) _IHelperProxyVtbl = 
{
    &IHelper_ProxyInfo,
    &IID_IHelper,
    IUnknown_QueryInterface_Proxy,
    IUnknown_AddRef_Proxy,
    IUnknown_Release_Proxy ,
    (void *)-1 /* IHelper::evaluate */ ,
    (void *)-1 /* IHelper::split */ ,
    (void *)-1 /* IHelper::getQuickMove */
};

const CInterfaceStubVtbl _IHelperStubVtbl =
{
    &IID_IHelper,
    &IHelper_ServerInfo,
    6,
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

	/* Procedure evaluate */

			0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/*  2 */	NdrFcLong( 0x0 ),	/* 0 */
/*  6 */	NdrFcShort( 0x3 ),	/* 3 */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/*  8 */	NdrFcShort( 0x28 ),	/* x86, MIPS Stack size/offset = 40 */
#else
			NdrFcShort( 0x2c ),	/* PPC Stack size/offset = 44 */
#endif
#else
			NdrFcShort( 0x28 ),	/* MIPS Stack size/offset = 40 */
#endif
#else
			NdrFcShort( 0x48 ),	/* Alpha Stack size/offset = 72 */
#endif
/* 10 */	NdrFcShort( 0x16 ),	/* 22 */
/* 12 */	NdrFcShort( 0x30 ),	/* 48 */
/* 14 */	0x6,		/* Oi2 Flags:  clt must size, has return, */
			0x7,		/* 7 */

	/* Parameter gs */

/* 16 */	NdrFcShort( 0x8b ),	/* Flags:  must size, must free, in, by val, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 18 */	NdrFcShort( 0x4 ),	/* x86, MIPS Stack size/offset = 4 */
#else
			NdrFcShort( 0x8 ),	/* PPC Stack size/offset = 8 */
#endif
#else
			NdrFcShort( 0x4 ),	/* MIPS Stack size/offset = 4 */
#endif
#else
			NdrFcShort( 0x8 ),	/* Alpha Stack size/offset = 8 */
#endif
/* 20 */	NdrFcShort( 0x10 ),	/* Type Offset=16 */

	/* Parameter level */

/* 22 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 24 */	NdrFcShort( 0x10 ),	/* x86, MIPS Stack size/offset = 16 */
#else
			NdrFcShort( 0x14 ),	/* PPC Stack size/offset = 20 */
#endif
#else
			NdrFcShort( 0x10 ),	/* MIPS Stack size/offset = 16 */
#endif
#else
			NdrFcShort( 0x18 ),	/* Alpha Stack size/offset = 24 */
#endif
/* 26 */	0xd,		/* FC_ENUM16 */
			0x0,		/* 0 */

	/* Parameter ply */

/* 28 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 30 */	NdrFcShort( 0x14 ),	/* x86, MIPS Stack size/offset = 20 */
#else
			NdrFcShort( 0x18 ),	/* PPC Stack size/offset = 24 */
#endif
#else
			NdrFcShort( 0x14 ),	/* MIPS Stack size/offset = 20 */
#endif
#else
			NdrFcShort( 0x20 ),	/* Alpha Stack size/offset = 32 */
#endif
/* 32 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter timelimit */

/* 34 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 36 */	NdrFcShort( 0x18 ),	/* x86, MIPS Stack size/offset = 24 */
#else
			NdrFcShort( 0x1c ),	/* PPC Stack size/offset = 28 */
#endif
#else
			NdrFcShort( 0x18 ),	/* MIPS Stack size/offset = 24 */
#endif
#else
			NdrFcShort( 0x28 ),	/* Alpha Stack size/offset = 40 */
#endif
/* 38 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter quality */

/* 40 */	NdrFcShort( 0x2150 ),	/* Flags:  out, base type, simple ref, srv alloc size=8 */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 42 */	NdrFcShort( 0x1c ),	/* x86, MIPS Stack size/offset = 28 */
#else
			NdrFcShort( 0x20 ),	/* PPC Stack size/offset = 32 */
#endif
#else
			NdrFcShort( 0x1c ),	/* MIPS Stack size/offset = 28 */
#endif
#else
			NdrFcShort( 0x30 ),	/* Alpha Stack size/offset = 48 */
#endif
/* 44 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter data */

/* 46 */	NdrFcShort( 0x6112 ),	/* Flags:  must free, out, simple ref, srv alloc size=24 */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 48 */	NdrFcShort( 0x20 ),	/* x86, MIPS Stack size/offset = 32 */
#else
			NdrFcShort( 0x24 ),	/* PPC Stack size/offset = 36 */
#endif
#else
			NdrFcShort( 0x20 ),	/* MIPS Stack size/offset = 32 */
#endif
#else
			NdrFcShort( 0x38 ),	/* Alpha Stack size/offset = 56 */
#endif
/* 50 */	NdrFcShort( 0x2e ),	/* Type Offset=46 */

	/* Return value */

/* 52 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 54 */	NdrFcShort( 0x24 ),	/* x86, MIPS Stack size/offset = 36 */
#else
			NdrFcShort( 0x28 ),	/* PPC Stack size/offset = 40 */
#endif
#else
			NdrFcShort( 0x24 ),	/* MIPS Stack size/offset = 36 */
#endif
#else
			NdrFcShort( 0x40 ),	/* Alpha Stack size/offset = 64 */
#endif
/* 56 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure split */

/* 58 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 60 */	NdrFcLong( 0x0 ),	/* 0 */
/* 64 */	NdrFcShort( 0x4 ),	/* 4 */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 66 */	NdrFcShort( 0x24 ),	/* x86, MIPS Stack size/offset = 36 */
#else
			NdrFcShort( 0x28 ),	/* PPC Stack size/offset = 40 */
#endif
#else
			NdrFcShort( 0x24 ),	/* MIPS Stack size/offset = 36 */
#endif
#else
			NdrFcShort( 0x40 ),	/* Alpha Stack size/offset = 64 */
#endif
/* 68 */	NdrFcShort( 0x16 ),	/* 22 */
/* 70 */	NdrFcShort( 0x10 ),	/* 16 */
/* 72 */	0x7,		/* Oi2 Flags:  srv must size, clt must size, has return, */
			0x6,		/* 6 */

	/* Parameter gs */

/* 74 */	NdrFcShort( 0x8b ),	/* Flags:  must size, must free, in, by val, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 76 */	NdrFcShort( 0x4 ),	/* x86, MIPS Stack size/offset = 4 */
#else
			NdrFcShort( 0x8 ),	/* PPC Stack size/offset = 8 */
#endif
#else
			NdrFcShort( 0x4 ),	/* MIPS Stack size/offset = 4 */
#endif
#else
			NdrFcShort( 0x8 ),	/* Alpha Stack size/offset = 8 */
#endif
/* 78 */	NdrFcShort( 0x10 ),	/* Type Offset=16 */

	/* Parameter level */

/* 80 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 82 */	NdrFcShort( 0x10 ),	/* x86, MIPS Stack size/offset = 16 */
#else
			NdrFcShort( 0x14 ),	/* PPC Stack size/offset = 20 */
#endif
#else
			NdrFcShort( 0x10 ),	/* MIPS Stack size/offset = 16 */
#endif
#else
			NdrFcShort( 0x18 ),	/* Alpha Stack size/offset = 24 */
#endif
/* 84 */	0xd,		/* FC_ENUM16 */
			0x0,		/* 0 */

	/* Parameter maxSize */

/* 86 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 88 */	NdrFcShort( 0x14 ),	/* x86, MIPS Stack size/offset = 20 */
#else
			NdrFcShort( 0x18 ),	/* PPC Stack size/offset = 24 */
#endif
#else
			NdrFcShort( 0x14 ),	/* MIPS Stack size/offset = 20 */
#endif
#else
			NdrFcShort( 0x20 ),	/* Alpha Stack size/offset = 32 */
#endif
/* 90 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter actualSize */

/* 92 */	NdrFcShort( 0x158 ),	/* Flags:  in, out, base type, simple ref, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 94 */	NdrFcShort( 0x18 ),	/* x86, MIPS Stack size/offset = 24 */
#else
			NdrFcShort( 0x1c ),	/* PPC Stack size/offset = 28 */
#endif
#else
			NdrFcShort( 0x18 ),	/* MIPS Stack size/offset = 24 */
#endif
#else
			NdrFcShort( 0x28 ),	/* Alpha Stack size/offset = 40 */
#endif
/* 96 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter moves */

/* 98 */	NdrFcShort( 0x11b ),	/* Flags:  must size, must free, in, out, simple ref, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 100 */	NdrFcShort( 0x1c ),	/* x86, MIPS Stack size/offset = 28 */
#else
			NdrFcShort( 0x20 ),	/* PPC Stack size/offset = 32 */
#endif
#else
			NdrFcShort( 0x1c ),	/* MIPS Stack size/offset = 28 */
#endif
#else
			NdrFcShort( 0x30 ),	/* Alpha Stack size/offset = 48 */
#endif
/* 102 */	NdrFcShort( 0x74 ),	/* Type Offset=116 */

	/* Return value */

/* 104 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 106 */	NdrFcShort( 0x20 ),	/* x86, MIPS Stack size/offset = 32 */
#else
			NdrFcShort( 0x24 ),	/* PPC Stack size/offset = 36 */
#endif
#else
			NdrFcShort( 0x20 ),	/* MIPS Stack size/offset = 32 */
#endif
#else
			NdrFcShort( 0x38 ),	/* Alpha Stack size/offset = 56 */
#endif
/* 108 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure getQuickMove */

/* 110 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 112 */	NdrFcLong( 0x0 ),	/* 0 */
/* 116 */	NdrFcShort( 0x5 ),	/* 5 */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 118 */	NdrFcShort( 0x24 ),	/* x86, MIPS Stack size/offset = 36 */
#else
			NdrFcShort( 0x28 ),	/* PPC Stack size/offset = 40 */
#endif
#else
			NdrFcShort( 0x24 ),	/* MIPS Stack size/offset = 36 */
#endif
#else
			NdrFcShort( 0x40 ),	/* Alpha Stack size/offset = 64 */
#endif
/* 120 */	NdrFcShort( 0x16 ),	/* 22 */
/* 122 */	NdrFcShort( 0x10 ),	/* 16 */
/* 124 */	0x7,		/* Oi2 Flags:  srv must size, clt must size, has return, */
			0x6,		/* 6 */

	/* Parameter gs */

/* 126 */	NdrFcShort( 0x8b ),	/* Flags:  must size, must free, in, by val, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 128 */	NdrFcShort( 0x4 ),	/* x86, MIPS Stack size/offset = 4 */
#else
			NdrFcShort( 0x8 ),	/* PPC Stack size/offset = 8 */
#endif
#else
			NdrFcShort( 0x4 ),	/* MIPS Stack size/offset = 4 */
#endif
#else
			NdrFcShort( 0x8 ),	/* Alpha Stack size/offset = 8 */
#endif
/* 130 */	NdrFcShort( 0x10 ),	/* Type Offset=16 */

	/* Parameter level */

/* 132 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 134 */	NdrFcShort( 0x10 ),	/* x86, MIPS Stack size/offset = 16 */
#else
			NdrFcShort( 0x14 ),	/* PPC Stack size/offset = 20 */
#endif
#else
			NdrFcShort( 0x10 ),	/* MIPS Stack size/offset = 16 */
#endif
#else
			NdrFcShort( 0x18 ),	/* Alpha Stack size/offset = 24 */
#endif
/* 136 */	0xd,		/* FC_ENUM16 */
			0x0,		/* 0 */

	/* Parameter maxSize */

/* 138 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 140 */	NdrFcShort( 0x14 ),	/* x86, MIPS Stack size/offset = 20 */
#else
			NdrFcShort( 0x18 ),	/* PPC Stack size/offset = 24 */
#endif
#else
			NdrFcShort( 0x14 ),	/* MIPS Stack size/offset = 20 */
#endif
#else
			NdrFcShort( 0x20 ),	/* Alpha Stack size/offset = 32 */
#endif
/* 142 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter actualSize */

/* 144 */	NdrFcShort( 0x158 ),	/* Flags:  in, out, base type, simple ref, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 146 */	NdrFcShort( 0x18 ),	/* x86, MIPS Stack size/offset = 24 */
#else
			NdrFcShort( 0x1c ),	/* PPC Stack size/offset = 28 */
#endif
#else
			NdrFcShort( 0x18 ),	/* MIPS Stack size/offset = 24 */
#endif
#else
			NdrFcShort( 0x28 ),	/* Alpha Stack size/offset = 40 */
#endif
/* 148 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter move */

/* 150 */	NdrFcShort( 0x11b ),	/* Flags:  must size, must free, in, out, simple ref, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 152 */	NdrFcShort( 0x1c ),	/* x86, MIPS Stack size/offset = 28 */
#else
			NdrFcShort( 0x20 ),	/* PPC Stack size/offset = 32 */
#endif
#else
			NdrFcShort( 0x1c ),	/* MIPS Stack size/offset = 28 */
#endif
#else
			NdrFcShort( 0x30 ),	/* Alpha Stack size/offset = 48 */
#endif
/* 154 */	NdrFcShort( 0x74 ),	/* Type Offset=116 */

	/* Return value */

/* 156 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 158 */	NdrFcShort( 0x20 ),	/* x86, MIPS Stack size/offset = 32 */
#else
			NdrFcShort( 0x24 ),	/* PPC Stack size/offset = 36 */
#endif
#else
			NdrFcShort( 0x20 ),	/* MIPS Stack size/offset = 32 */
#endif
#else
			NdrFcShort( 0x38 ),	/* Alpha Stack size/offset = 56 */
#endif
/* 160 */	0x8,		/* FC_LONG */
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
			0x1c,		/* FC_CVARRAY */
			0x0,		/* 0 */
/*  4 */	NdrFcShort( 0x1 ),	/* 1 */
/*  6 */	0x18,		/* Corr desc:  field pointer, FC_LONG */
			0x0,		/*  */
/*  8 */	NdrFcShort( 0x4 ),	/* 4 */
/* 10 */	0x18,		/* Corr desc:  field pointer, FC_LONG */
			0x0,		/*  */
/* 12 */	NdrFcShort( 0x0 ),	/* 0 */
/* 14 */	0x2,		/* FC_CHAR */
			0x5b,		/* FC_END */
/* 16 */	
			0x16,		/* FC_PSTRUCT */
			0x3,		/* 3 */
/* 18 */	NdrFcShort( 0xc ),	/* 12 */
/* 20 */	
			0x4b,		/* FC_PP */
			0x5c,		/* FC_PAD */
/* 22 */	
			0x46,		/* FC_NO_REPEAT */
			0x5c,		/* FC_PAD */
/* 24 */	NdrFcShort( 0x8 ),	/* 8 */
/* 26 */	NdrFcShort( 0x8 ),	/* 8 */
/* 28 */	0x11, 0x0,	/* FC_RP */
/* 30 */	NdrFcShort( 0xffffffe4 ),	/* Offset= -28 (2) */
/* 32 */	
			0x5b,		/* FC_END */

			0x8,		/* FC_LONG */
/* 34 */	0x8,		/* FC_LONG */
			0x8,		/* FC_LONG */
/* 36 */	0x5c,		/* FC_PAD */
			0x5b,		/* FC_END */
/* 38 */	
			0x11, 0xc,	/* FC_RP [alloced_on_stack] [simple_pointer] */
/* 40 */	0x8,		/* FC_LONG */
			0x5c,		/* FC_PAD */
/* 42 */	
			0x11, 0x4,	/* FC_RP [alloced_on_stack] */
/* 44 */	NdrFcShort( 0x2 ),	/* Offset= 2 (46) */
/* 46 */	
			0x15,		/* FC_STRUCT */
			0x3,		/* 3 */
/* 48 */	NdrFcShort( 0x18 ),	/* 24 */
/* 50 */	0xa,		/* FC_FLOAT */
			0xa,		/* FC_FLOAT */
/* 52 */	0xa,		/* FC_FLOAT */
			0xa,		/* FC_FLOAT */
/* 54 */	0xa,		/* FC_FLOAT */
			0xa,		/* FC_FLOAT */
/* 56 */	0x5c,		/* FC_PAD */
			0x5b,		/* FC_END */
/* 58 */	
			0x11, 0x8,	/* FC_RP [simple_pointer] */
/* 60 */	0x8,		/* FC_LONG */
			0x5c,		/* FC_PAD */
/* 62 */	
			0x11, 0x0,	/* FC_RP */
/* 64 */	NdrFcShort( 0x34 ),	/* Offset= 52 (116) */
/* 66 */	
			0x1c,		/* FC_CVARRAY */
			0x0,		/* 0 */
/* 68 */	NdrFcShort( 0x1 ),	/* 1 */
/* 70 */	0x18,		/* Corr desc:  field pointer, FC_LONG */
			0x0,		/*  */
/* 72 */	NdrFcShort( 0x18 ),	/* 24 */
/* 74 */	0x18,		/* Corr desc:  field pointer, FC_LONG */
			0x0,		/*  */
/* 76 */	NdrFcShort( 0x14 ),	/* 20 */
/* 78 */	0x2,		/* FC_CHAR */
			0x5b,		/* FC_END */
/* 80 */	
			0x16,		/* FC_PSTRUCT */
			0x3,		/* 3 */
/* 82 */	NdrFcShort( 0x20 ),	/* 32 */
/* 84 */	
			0x4b,		/* FC_PP */
			0x5c,		/* FC_PAD */
/* 86 */	
			0x46,		/* FC_NO_REPEAT */
			0x5c,		/* FC_PAD */
/* 88 */	NdrFcShort( 0x8 ),	/* 8 */
/* 90 */	NdrFcShort( 0x8 ),	/* 8 */
/* 92 */	0x11, 0x0,	/* FC_RP */
/* 94 */	NdrFcShort( 0xffffffa4 ),	/* Offset= -92 (2) */
/* 96 */	
			0x46,		/* FC_NO_REPEAT */
			0x5c,		/* FC_PAD */
/* 98 */	NdrFcShort( 0x1c ),	/* 28 */
/* 100 */	NdrFcShort( 0x1c ),	/* 28 */
/* 102 */	0x11, 0x0,	/* FC_RP */
/* 104 */	NdrFcShort( 0xffffffda ),	/* Offset= -38 (66) */
/* 106 */	
			0x5b,		/* FC_END */

			0x8,		/* FC_LONG */
/* 108 */	0x8,		/* FC_LONG */
			0x8,		/* FC_LONG */
/* 110 */	0x8,		/* FC_LONG */
			0x8,		/* FC_LONG */
/* 112 */	0x8,		/* FC_LONG */
			0x8,		/* FC_LONG */
/* 114 */	0x8,		/* FC_LONG */
			0x5b,		/* FC_END */
/* 116 */	
			0x1c,		/* FC_CVARRAY */
			0x3,		/* 3 */
/* 118 */	NdrFcShort( 0x20 ),	/* 32 */
/* 120 */	0x28,		/* Corr desc:  parameter, FC_LONG */
			0x0,		/*  */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 122 */	NdrFcShort( 0x14 ),	/* x86, MIPS Stack size/offset = 20 */
#else
			NdrFcShort( 0x18 ),	/* PPC Stack size/offset = 24 */
#endif
#else
			NdrFcShort( 0x14 ),	/* MIPS Stack size/offset = 20 */
#endif
#else
			NdrFcShort( 0x20 ),	/* Alpha Stack size/offset = 32 */
#endif
/* 124 */	0x28,		/* Corr desc:  parameter, FC_LONG */
			0x54,		/* FC_DEREFERENCE */
#ifndef _ALPHA_
#if !defined(_MIPS_)
#ifndef _PPC_
/* 126 */	NdrFcShort( 0x18 ),	/* x86, MIPS Stack size/offset = 24 */
#else
			NdrFcShort( 0x1c ),	/* PPC Stack size/offset = 28 */
#endif
#else
			NdrFcShort( 0x18 ),	/* MIPS Stack size/offset = 24 */
#endif
#else
			NdrFcShort( 0x28 ),	/* Alpha Stack size/offset = 40 */
#endif
/* 128 */	
			0x4b,		/* FC_PP */
			0x5c,		/* FC_PAD */
/* 130 */	
			0x48,		/* FC_VARIABLE_REPEAT */
			0x4a,		/* FC_VARIABLE_OFFSET */
/* 132 */	NdrFcShort( 0x20 ),	/* 32 */
/* 134 */	NdrFcShort( 0x0 ),	/* 0 */
/* 136 */	NdrFcShort( 0x2 ),	/* 2 */
/* 138 */	NdrFcShort( 0x8 ),	/* 8 */
/* 140 */	NdrFcShort( 0x8 ),	/* 8 */
/* 142 */	0x11, 0x0,	/* FC_RP */
/* 144 */	NdrFcShort( 0xffffff72 ),	/* Offset= -142 (2) */
/* 146 */	NdrFcShort( 0x1c ),	/* 28 */
/* 148 */	NdrFcShort( 0x1c ),	/* 28 */
/* 150 */	0x11, 0x0,	/* FC_RP */
/* 152 */	NdrFcShort( 0xffffffaa ),	/* Offset= -86 (66) */
/* 154 */	
			0x5b,		/* FC_END */

			0x4c,		/* FC_EMBEDDED_COMPLEX */
/* 156 */	0x0,		/* 0 */
			NdrFcShort( 0xffffffb3 ),	/* Offset= -77 (80) */
			0x5b,		/* FC_END */

			0x0
        }
    };

const CInterfaceProxyVtbl * _Helper_ProxyVtblList[] = 
{
    ( CInterfaceProxyVtbl *) &_IHelperProxyVtbl,
    0
};

const CInterfaceStubVtbl * _Helper_StubVtblList[] = 
{
    ( CInterfaceStubVtbl *) &_IHelperStubVtbl,
    0
};

PCInterfaceName const _Helper_InterfaceNamesList[] = 
{
    "IHelper",
    0
};


#define _Helper_CHECK_IID(n)	IID_GENERIC_CHECK_IID( _Helper, pIID, n)

int __stdcall _Helper_IID_Lookup( const IID * pIID, int * pIndex )
{
    
    if(!_Helper_CHECK_IID(0))
        {
        *pIndex = 0;
        return 1;
        }

    return 0;
}

const ExtendedProxyFileInfo Helper_ProxyFileInfo = 
{
    (PCInterfaceProxyVtblList *) & _Helper_ProxyVtblList,
    (PCInterfaceStubVtblList *) & _Helper_StubVtblList,
    (const PCInterfaceName * ) & _Helper_InterfaceNamesList,
    0, // no delegation
    & _Helper_IID_Lookup, 
    1,
    2,
    0, /* table of [async_uuid] interfaces */
    0, /* Filler1 */
    0, /* Filler2 */
    0  /* Filler3 */
};

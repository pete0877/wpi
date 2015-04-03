/* this ALWAYS GENERATED file contains the definitions for the interfaces */


/* File created by MIDL compiler version 5.01.0164 */
/* at Sat Dec 11 14:09:17 1999
 */
/* Compiler settings for D:\Dev\Dgte\Test\TestServer\TestServer.idl:
    Oicf (OptLev=i2), W1, Zp8, env=Win32, ms_ext, c_ext
    error checks: allocation ref bounds_check enum stub_data 
*/
//@@MIDL_FILE_HEADING(  )


/* verify that the <rpcndr.h> version is high enough to compile this file*/
#ifndef __REQUIRED_RPCNDR_H_VERSION__
#define __REQUIRED_RPCNDR_H_VERSION__ 440
#endif

#include "rpc.h"
#include "rpcndr.h"

#ifndef __RPCNDR_H_VERSION__
#error this stub requires an updated version of <rpcndr.h>
#endif // __RPCNDR_H_VERSION__

#ifndef COM_NO_WINDOWS_H
#include "windows.h"
#include "ole2.h"
#endif /*COM_NO_WINDOWS_H*/

#ifndef __TestServer_h__
#define __TestServer_h__

#ifdef __cplusplus
extern "C"{
#endif 

/* Forward Declarations */ 

#ifndef __IHelperRegistrar_FWD_DEFINED__
#define __IHelperRegistrar_FWD_DEFINED__
typedef interface IHelperRegistrar IHelperRegistrar;
#endif 	/* __IHelperRegistrar_FWD_DEFINED__ */


#ifndef __HelperRegistrar_FWD_DEFINED__
#define __HelperRegistrar_FWD_DEFINED__

#ifdef __cplusplus
typedef class HelperRegistrar HelperRegistrar;
#else
typedef struct HelperRegistrar HelperRegistrar;
#endif /* __cplusplus */

#endif 	/* __HelperRegistrar_FWD_DEFINED__ */


/* header files for imported files */
#include "oaidl.h"
#include "ocidl.h"
#include "Types.h"

void __RPC_FAR * __RPC_USER MIDL_user_allocate(size_t);
void __RPC_USER MIDL_user_free( void __RPC_FAR * ); 

#ifndef __IHelperRegistrar_INTERFACE_DEFINED__
#define __IHelperRegistrar_INTERFACE_DEFINED__

/* interface IHelperRegistrar */
/* [unique][helpstring][uuid][object] */ 


EXTERN_C const IID IID_IHelperRegistrar;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("B5D4EBC0-C7D9-4C14-BFCF-C9AB4CBCABB5")
    IHelperRegistrar : public IUnknown
    {
    public:
        virtual /* [helpstring] */ HRESULT STDMETHODCALLTYPE registerHelper( 
            /* [string][in] */ unsigned char __RPC_FAR *address,
            /* [in] */ GameTypeData data) = 0;
        
        virtual /* [helpstring] */ HRESULT STDMETHODCALLTYPE unregisterHelper( 
            /* [string][in] */ unsigned char __RPC_FAR *address) = 0;
        
    };
    
#else 	/* C style interface */

    typedef struct IHelperRegistrarVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE __RPC_FAR *QueryInterface )( 
            IHelperRegistrar __RPC_FAR * This,
            /* [in] */ REFIID riid,
            /* [iid_is][out] */ void __RPC_FAR *__RPC_FAR *ppvObject);
        
        ULONG ( STDMETHODCALLTYPE __RPC_FAR *AddRef )( 
            IHelperRegistrar __RPC_FAR * This);
        
        ULONG ( STDMETHODCALLTYPE __RPC_FAR *Release )( 
            IHelperRegistrar __RPC_FAR * This);
        
        /* [helpstring] */ HRESULT ( STDMETHODCALLTYPE __RPC_FAR *registerHelper )( 
            IHelperRegistrar __RPC_FAR * This,
            /* [string][in] */ unsigned char __RPC_FAR *address,
            /* [in] */ GameTypeData data);
        
        /* [helpstring] */ HRESULT ( STDMETHODCALLTYPE __RPC_FAR *unregisterHelper )( 
            IHelperRegistrar __RPC_FAR * This,
            /* [string][in] */ unsigned char __RPC_FAR *address);
        
        END_INTERFACE
    } IHelperRegistrarVtbl;

    interface IHelperRegistrar
    {
        CONST_VTBL struct IHelperRegistrarVtbl __RPC_FAR *lpVtbl;
    };

    

#ifdef COBJMACROS


#define IHelperRegistrar_QueryInterface(This,riid,ppvObject)	\
    (This)->lpVtbl -> QueryInterface(This,riid,ppvObject)

#define IHelperRegistrar_AddRef(This)	\
    (This)->lpVtbl -> AddRef(This)

#define IHelperRegistrar_Release(This)	\
    (This)->lpVtbl -> Release(This)


#define IHelperRegistrar_registerHelper(This,address,data)	\
    (This)->lpVtbl -> registerHelper(This,address,data)

#define IHelperRegistrar_unregisterHelper(This,address)	\
    (This)->lpVtbl -> unregisterHelper(This,address)

#endif /* COBJMACROS */


#endif 	/* C style interface */



/* [helpstring] */ HRESULT STDMETHODCALLTYPE IHelperRegistrar_registerHelper_Proxy( 
    IHelperRegistrar __RPC_FAR * This,
    /* [string][in] */ unsigned char __RPC_FAR *address,
    /* [in] */ GameTypeData data);


void __RPC_STUB IHelperRegistrar_registerHelper_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


/* [helpstring] */ HRESULT STDMETHODCALLTYPE IHelperRegistrar_unregisterHelper_Proxy( 
    IHelperRegistrar __RPC_FAR * This,
    /* [string][in] */ unsigned char __RPC_FAR *address);


void __RPC_STUB IHelperRegistrar_unregisterHelper_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);



#endif 	/* __IHelperRegistrar_INTERFACE_DEFINED__ */



#ifndef __TESTSERVERLib_LIBRARY_DEFINED__
#define __TESTSERVERLib_LIBRARY_DEFINED__

/* library TESTSERVERLib */
/* [helpstring][version][uuid] */ 


EXTERN_C const IID LIBID_TESTSERVERLib;

EXTERN_C const CLSID CLSID_HelperRegistrar;

#ifdef __cplusplus

class DECLSPEC_UUID("F721574E-DC47-44B9-954E-84D31170C388")
HelperRegistrar;
#endif
#endif /* __TESTSERVERLib_LIBRARY_DEFINED__ */

/* Additional Prototypes for ALL interfaces */

/* end of Additional Prototypes */

#ifdef __cplusplus
}
#endif

#endif

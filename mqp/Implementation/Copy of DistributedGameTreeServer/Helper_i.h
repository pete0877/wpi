/* this ALWAYS GENERATED file contains the definitions for the interfaces */


/* File created by MIDL compiler version 5.01.0164 */
/* at Sat Dec 11 16:31:52 1999
 */
/* Compiler settings for E:\MQP\Implementation\DGTE\Helper\Helper.idl:
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

#ifndef __Helper_i_h__
#define __Helper_i_h__

#ifdef __cplusplus
extern "C"{
#endif 

/* Forward Declarations */ 

#ifndef __IHelper_FWD_DEFINED__
#define __IHelper_FWD_DEFINED__
typedef interface IHelper IHelper;
#endif 	/* __IHelper_FWD_DEFINED__ */


#ifndef __Helper_FWD_DEFINED__
#define __Helper_FWD_DEFINED__

#ifdef __cplusplus
typedef class Helper Helper;
#else
typedef struct Helper Helper;
#endif /* __cplusplus */

#endif 	/* __Helper_FWD_DEFINED__ */


/* header files for imported files */
#include "oaidl.h"
#include "ocidl.h"
#include "Types.h"

void __RPC_FAR * __RPC_USER MIDL_user_allocate(size_t);
void __RPC_USER MIDL_user_free( void __RPC_FAR * ); 

#ifndef __IHelper_INTERFACE_DEFINED__
#define __IHelper_INTERFACE_DEFINED__

/* interface IHelper */
/* [unique][uuid][object] */ 


EXTERN_C const IID IID_IHelper;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("C3F69A0C-23EF-47C6-A2CD-DC4CA64CCA22")
    IHelper : public IUnknown
    {
    public:
        virtual HRESULT STDMETHODCALLTYPE evaluate( 
            /* [in] */ GameState gs,
            /* [in] */ LevelType level,
            /* [in] */ int ply,
            /* [in] */ int timelimit,
            /* [out] */ int __RPC_FAR *quality,
            /* [out] */ HelperPerformanceData __RPC_FAR *data) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE split( 
            /* [in] */ GameState gs,
            /* [in] */ LevelType level,
            /* [in] */ int maxSize,
            /* [out][in] */ int __RPC_FAR *actualSize,
            /* [length_is][size_is][out][in] */ Move __RPC_FAR *moves) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE getQuickMove( 
            /* [in] */ GameState gs,
            /* [in] */ LevelType level,
            /* [in] */ int maxSize,
            /* [out][in] */ int __RPC_FAR *actualSize,
            /* [length_is][size_is][out][in] */ Move __RPC_FAR *move) = 0;
        
    };
    
#else 	/* C style interface */

    typedef struct IHelperVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE __RPC_FAR *QueryInterface )( 
            IHelper __RPC_FAR * This,
            /* [in] */ REFIID riid,
            /* [iid_is][out] */ void __RPC_FAR *__RPC_FAR *ppvObject);
        
        ULONG ( STDMETHODCALLTYPE __RPC_FAR *AddRef )( 
            IHelper __RPC_FAR * This);
        
        ULONG ( STDMETHODCALLTYPE __RPC_FAR *Release )( 
            IHelper __RPC_FAR * This);
        
        HRESULT ( STDMETHODCALLTYPE __RPC_FAR *evaluate )( 
            IHelper __RPC_FAR * This,
            /* [in] */ GameState gs,
            /* [in] */ LevelType level,
            /* [in] */ int ply,
            /* [in] */ int timelimit,
            /* [out] */ int __RPC_FAR *quality,
            /* [out] */ HelperPerformanceData __RPC_FAR *data);
        
        HRESULT ( STDMETHODCALLTYPE __RPC_FAR *split )( 
            IHelper __RPC_FAR * This,
            /* [in] */ GameState gs,
            /* [in] */ LevelType level,
            /* [in] */ int maxSize,
            /* [out][in] */ int __RPC_FAR *actualSize,
            /* [length_is][size_is][out][in] */ Move __RPC_FAR *moves);
        
        HRESULT ( STDMETHODCALLTYPE __RPC_FAR *getQuickMove )( 
            IHelper __RPC_FAR * This,
            /* [in] */ GameState gs,
            /* [in] */ LevelType level,
            /* [in] */ int maxSize,
            /* [out][in] */ int __RPC_FAR *actualSize,
            /* [length_is][size_is][out][in] */ Move __RPC_FAR *move);
        
        END_INTERFACE
    } IHelperVtbl;

    interface IHelper
    {
        CONST_VTBL struct IHelperVtbl __RPC_FAR *lpVtbl;
    };

    

#ifdef COBJMACROS


#define IHelper_QueryInterface(This,riid,ppvObject)	\
    (This)->lpVtbl -> QueryInterface(This,riid,ppvObject)

#define IHelper_AddRef(This)	\
    (This)->lpVtbl -> AddRef(This)

#define IHelper_Release(This)	\
    (This)->lpVtbl -> Release(This)


#define IHelper_evaluate(This,gs,level,ply,timelimit,quality,data)	\
    (This)->lpVtbl -> evaluate(This,gs,level,ply,timelimit,quality,data)

#define IHelper_split(This,gs,level,maxSize,actualSize,moves)	\
    (This)->lpVtbl -> split(This,gs,level,maxSize,actualSize,moves)

#define IHelper_getQuickMove(This,gs,level,maxSize,actualSize,move)	\
    (This)->lpVtbl -> getQuickMove(This,gs,level,maxSize,actualSize,move)

#endif /* COBJMACROS */


#endif 	/* C style interface */



HRESULT STDMETHODCALLTYPE IHelper_evaluate_Proxy( 
    IHelper __RPC_FAR * This,
    /* [in] */ GameState gs,
    /* [in] */ LevelType level,
    /* [in] */ int ply,
    /* [in] */ int timelimit,
    /* [out] */ int __RPC_FAR *quality,
    /* [out] */ HelperPerformanceData __RPC_FAR *data);


void __RPC_STUB IHelper_evaluate_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE IHelper_split_Proxy( 
    IHelper __RPC_FAR * This,
    /* [in] */ GameState gs,
    /* [in] */ LevelType level,
    /* [in] */ int maxSize,
    /* [out][in] */ int __RPC_FAR *actualSize,
    /* [length_is][size_is][out][in] */ Move __RPC_FAR *moves);


void __RPC_STUB IHelper_split_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE IHelper_getQuickMove_Proxy( 
    IHelper __RPC_FAR * This,
    /* [in] */ GameState gs,
    /* [in] */ LevelType level,
    /* [in] */ int maxSize,
    /* [out][in] */ int __RPC_FAR *actualSize,
    /* [length_is][size_is][out][in] */ Move __RPC_FAR *move);


void __RPC_STUB IHelper_getQuickMove_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);



#endif 	/* __IHelper_INTERFACE_DEFINED__ */



#ifndef __HelperLib_LIBRARY_DEFINED__
#define __HelperLib_LIBRARY_DEFINED__

/* library HelperLib */
/* [version][uuid] */ 


EXTERN_C const IID LIBID_HelperLib;

EXTERN_C const CLSID CLSID_Helper;

#ifdef __cplusplus

class DECLSPEC_UUID("E729EB35-6A87-498F-B115-57C99B551044")
Helper;
#endif
#endif /* __HelperLib_LIBRARY_DEFINED__ */

/* Additional Prototypes for ALL interfaces */

/* end of Additional Prototypes */

#ifdef __cplusplus
}
#endif

#endif

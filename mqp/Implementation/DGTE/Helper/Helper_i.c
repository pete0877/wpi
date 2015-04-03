/* this file contains the actual definitions of */
/* the IIDs and CLSIDs */

/* link this file in with the server and any clients */


/* File created by MIDL compiler version 5.01.0164 */
/* at Thu Dec 16 13:12:04 1999
 */
/* Compiler settings for E:\MQP\Implementation\DGTE\Helper\Helper.idl:
    Oicf (OptLev=i2), W1, Zp8, env=Win32, ms_ext, c_ext
    error checks: allocation ref bounds_check enum stub_data 
*/
//@@MIDL_FILE_HEADING(  )
#ifdef __cplusplus
extern "C"{
#endif 


#ifndef __IID_DEFINED__
#define __IID_DEFINED__

typedef struct _IID
{
    unsigned long x;
    unsigned short s1;
    unsigned short s2;
    unsigned char  c[8];
} IID;

#endif // __IID_DEFINED__

#ifndef CLSID_DEFINED
#define CLSID_DEFINED
typedef IID CLSID;
#endif // CLSID_DEFINED

const IID IID_IHelper = {0xC3F69A0C,0x23EF,0x47C6,{0xA2,0xCD,0xDC,0x4C,0xA6,0x4C,0xCA,0x22}};


const IID LIBID_HelperLib = {0xE8659191,0x1A0F,0x4A86,{0x86,0xBC,0xFB,0xF9,0x2F,0xA6,0xF4,0x7E}};


const CLSID CLSID_Helper = {0xE729EB35,0x6A87,0x498F,{0xB1,0x15,0x57,0xC9,0x9B,0x55,0x10,0x44}};


#ifdef __cplusplus
}
#endif


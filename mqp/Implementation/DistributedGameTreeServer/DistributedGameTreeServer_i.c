/* this file contains the actual definitions of */
/* the IIDs and CLSIDs */

/* link this file in with the server and any clients */


/* File created by MIDL compiler version 5.01.0164 */
/* at Thu Dec 16 10:58:10 1999
 */
/* Compiler settings for E:\MQP\Implementation\DistributedGameTreeServer\DistributedGameTreeServer.idl:
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

const IID IID_IHelperRegistrar = {0xB5D4EBC0,0xC7D9,0x4C14,{0xBF,0xCF,0xC9,0xAB,0x4C,0xBC,0xAB,0xB5}};


const IID LIBID_DistributedGameTreeServerLib = {0x2D48855F,0x2B71,0x4AAB,{0xA3,0x82,0x54,0x7E,0x29,0x46,0xF7,0xB4}};


const CLSID CLSID_HelperRegistrar = {0xF721574E,0xDC47,0x44B9,{0x95,0x4E,0x84,0xD3,0x11,0x70,0xC3,0x88}};


#ifdef __cplusplus
}
#endif


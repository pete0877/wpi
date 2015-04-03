// HelperRegistrar.h : Declaration of the CHelperRegistrar

#ifndef __HELPERREGISTRAR_H_
#define __HELPERREGISTRAR_H_

#include "resource.h"       // main symbols
#include "Types.h"

/////////////////////////////////////////////////////////////////////////////
// CHelperRegistrar
class ATL_NO_VTABLE CHelperRegistrar : 
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<CHelperRegistrar, &CLSID_HelperRegistrar>,
	public IHelperRegistrar
{
protected:
	HelperRegistry* engineHelperRegistry;

public:
	CHelperRegistrar();

DECLARE_REGISTRY_RESOURCEID(IDR_HELPERREGISTRAR)
DECLARE_NOT_AGGREGATABLE(CHelperRegistrar)

DECLARE_PROTECT_FINAL_CONSTRUCT()

BEGIN_COM_MAP(CHelperRegistrar)
	COM_INTERFACE_ENTRY(IHelperRegistrar)
END_COM_MAP()

// IHelperRegistrar
public:
	STDMETHOD(unregisterHelper)(/*[in, string]*/ unsigned char *address);
	STDMETHOD(registerHelper)(/*[in, string]*/ unsigned char *address, /*[in]*/ GameTypeData data);
};

#endif //__HELPERREGISTRAR_H_

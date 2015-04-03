/////////////////////////////////////////////////////////////////////////////
//
// File Name:	HelperObj.h
// Description:	Interface of the HelperObj class.
// Date:		11/15/99
// Author:		sjastrzebski
//
// Copyright © 1999 Worcester Polytechnic Institute, All Rights Reserved.

#ifndef HELPEROBJ_INCLUDED
#define HELPEROBJ_INCLUDED

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "Helper_i.h"
#include "Plugin.h"
#include "Resource.h"

/////////////////////////////////////////////////////////////////////////////
// Class HelperObj

class ATL_NO_VTABLE HelperObj : 
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<HelperObj, &CLSID_Helper>,
	public IHelper
{
public:
	HelperObj();

	DECLARE_REGISTRY_RESOURCEID(IDR_HELPEROBJ)
	DECLARE_NOT_AGGREGATABLE(HelperObj)

	DECLARE_PROTECT_FINAL_CONSTRUCT()

	BEGIN_COM_MAP(HelperObj)
		COM_INTERFACE_ENTRY(IHelper)
	END_COM_MAP()

// Implementation
public:

protected:
	Plugin *m_plugin;

// IHelper
public:
	STDMETHOD(split)(/*[in]*/ GameState gs, /*[in]*/ LevelType level, /*[in]*/ int maxSize, /*[out]*/ int *actualSize, /*[in, out, size_is(maxSize)]*/ Move *moves);
	STDMETHOD(evaluate)(/*[in]*/ GameState gs, /*[in]*/ LevelType level, /*[in]*/ int ply, /*[in]*/ int timelimit, /*[out]*/ int *quality, /*[out]*/ HelperPerformanceData *data);
	STDMETHOD(getQuickMove)(/*[in]*/ GameState gs, /*[in]*/ LevelType level, /*[in]*/ int maxSize, /*[out]*/ int *actualSize, /*[in, out, size_is(maxSize)]*/ Move *moves);
};

/////////////////////////////////////////////////////////////////////////////

#endif // HELPEROBJ_INCLUDED

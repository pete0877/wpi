; CLW file contains information for the MFC ClassWizard

[General Info]
Version=1
LastClass=StatusDlg
LastTemplate=CDialog
NewFileInclude1=#include "stdafx.h"
NewFileInclude2=#include "helper.h"
LastPage=0

ClassCount=10
Class1=AboutDlg
Class2=StatusWnd
Class3=CHyperLink
Class4=PluginProperties
Class5=Properties
Class6=ConnectionPage
Class7=PluginsPage
Class8=Splash
Class9=CSystemTray

ResourceCount=7
Resource1=IDD_GAMEPROPERTIES_DIALOG
Resource2=IDR_POPUP_MENU
Resource3=IDR_MAINFRAME
Resource4=IDD_ABOUT_DIALOG
Resource5=IDD_GENERAL_PAGE
Resource6=IDD_PLUGINS_PAGE
Class10=StatusDlg
Resource7=IDD_STATUSDLG_DIALOG

[CLS:AboutDlg]
Type=0
BaseClass=CDialog
HeaderFile=AboutDlg.h
ImplementationFile=AboutDlg.cpp
LastObject=AboutDlg

[CLS:StatusWnd]
Type=0
BaseClass=CFrameWnd
HeaderFile=StatusWnd.h
ImplementationFile=Helper.cpp
LastObject=ID_APP_EXIT

[CLS:CHyperLink]
Type=0
BaseClass=CStatic
HeaderFile=HyperLink.h
ImplementationFile=HyperLink.cpp

[CLS:PluginProperties]
Type=0
BaseClass=CDialog
HeaderFile=PluginProperties.h
ImplementationFile=PluginProperties.cpp

[CLS:Properties]
Type=0
BaseClass=CPropertySheet
HeaderFile=Properties.h
ImplementationFile=Properties.cpp
LastObject=ID_APP_EXIT

[CLS:ConnectionPage]
Type=0
BaseClass=CPropertyPage
HeaderFile=PropertyPages.h
ImplementationFile=PropertyPages.cpp
Filter=D
VirtualFilter=idWC
LastObject=ConnectionPage

[CLS:PluginsPage]
Type=0
BaseClass=CPropertyPage
HeaderFile=PropertyPages.h
ImplementationFile=PropertyPages.cpp

[CLS:Splash]
Type=0
BaseClass=CWnd
HeaderFile=Splash.h
ImplementationFile=Splash.cpp

[CLS:CSystemTray]
Type=0
BaseClass=CWnd
HeaderFile=SystemTray.h
ImplementationFile=SystemTray.cpp

[DLG:IDD_ABOUT_DIALOG]
Type=1
Class=AboutDlg
ControlCount=9
Control1=IDOK,button,1342373889
Control2=IDC_STATIC,static,1342177294
Control3=IDC_STATIC,static,1342181380
Control4=IDC_STATIC,static,1342308352
Control5=IDC_STATIC,static,1342308352
Control6=IDC_VERSION,static,1342308352
Control7=IDC_STATIC,static,1342308352
Control8=IDC_ABOUT_HYPERLINK,static,1342308352
Control9=IDC_STATIC,static,1342308352

[DLG:IDD_GAMEPROPERTIES_DIALOG]
Type=1
Class=PluginProperties
ControlCount=16
Control1=IDOK,button,1342242817
Control2=IDC_STATIC,button,1342177287
Control3=IDC_STATIC,static,1342308352
Control4=IDC_GAMENAME,edit,1350631552
Control5=IDC_STATIC,static,1342308352
Control6=IDC_GAMEVERSION,edit,1350631552
Control7=IDC_STATIC,static,1342308352
Control8=IDC_GAMEDESCRIPTION,edit,1350631552
Control9=IDC_STATIC,static,1342308352
Control10=IDC_COMPANYNAME,edit,1350631552
Control11=IDC_STATIC,static,1342308352
Control12=IDC_LEGALCOPYRIGHT,edit,1350631552
Control13=IDC_STATIC,static,1342308352
Control14=IDC_LEGALTRADEMARKS,edit,1350631552
Control15=IDC_AVGALPHABETAGAIN,static,1342308352
Control16=IDC_AVGBRANCHINGFACTOR,static,1342308352

[DLG:IDD_PLUGINS_PAGE]
Type=1
Class=PluginsPage
ControlCount=5
Control1=IDC_STATIC,static,1342308352
Control2=IDC_STATIC,static,1342181380
Control3=IDC_PLUGINS,listbox,1352728835
Control4=IDC_ABOUT,button,1342242816
Control5=IDC_GETPLUGINS,static,1342308352

[MNU:IDR_MAINFRAME]
Type=1
Class=?
Command1=ID_APP_EXIT
CommandCount=1

[MNU:IDR_POPUP_MENU]
Type=1
Class=?
Command1=ID_HELPER_SHOWSTATUS
Command2=ID_HELPER_REGISTER
Command3=ID_HELPER_ABOUT
Command4=ID_HELPER_PROPERTIES
Command5=ID_HELPER_CLOSE
CommandCount=5

[DLG:IDD_GENERAL_PAGE]
Type=1
Class=ConnectionPage
ControlCount=8
Control1=IDC_STATIC,static,1342308352
Control2=IDC_STATIC,static,1342181380
Control3=IDC_STATIC,static,1342308352
Control4=IDC_SERVERADDRESS,edit,1350631552
Control5=IDC_STATIC,static,1342308352
Control6=IDC_STATIC,static,1342181380
Control7=IDC_AUTOREGISTER,button,1342242819
Control8=IDC_STATIC,static,1342308352

[DLG:IDD_STATUSDLG_DIALOG]
Type=1
Class=StatusDlg
ControlCount=2
Control1=IDOK,button,1342242817
Control2=IDC_OUTPUT,edit,1352730820

[CLS:StatusDlg]
Type=0
HeaderFile=StatusDlg.h
ImplementationFile=StatusDlg.cpp
BaseClass=CDialog
Filter=D


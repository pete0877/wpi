; CLW file contains information for the MFC ClassWizard

[General Info]
Version=1
LastClass=CDistributedGameTreeServerView
LastTemplate=CDialog
NewFileInclude1=#include "stdafx.h"
NewFileInclude2=#include "DistributedGameTreeServer.h"
LastPage=0

ClassCount=7
Class1=CDistributedGameTreeServerApp
Class2=CDistributedGameTreeServerDoc
Class3=CDistributedGameTreeServerView
Class4=CMainFrame

ResourceCount=3
Resource1=IDD_ABOUTBOX
Resource2=IDD_DIALOG2
Class5=CAboutDlg
Class6=MainDialog
Class7=ServerOptionsDialog
Resource3=IDR_MAINFRAME

[CLS:CDistributedGameTreeServerApp]
Type=0
HeaderFile=DistributedGameTreeServer.h
ImplementationFile=DistributedGameTreeServer.cpp
Filter=N
BaseClass=CWinApp
VirtualFilter=AC
LastObject=ID_SERVERSTART

[CLS:CDistributedGameTreeServerDoc]
Type=0
HeaderFile=DistributedGameTreeServerDoc.h
ImplementationFile=DistributedGameTreeServerDoc.cpp
Filter=N
LastObject=CDistributedGameTreeServerDoc

[CLS:CDistributedGameTreeServerView]
Type=0
HeaderFile=DistributedGameTreeServerView.h
ImplementationFile=DistributedGameTreeServerView.cpp
Filter=C
BaseClass=CEditView
VirtualFilter=VWC
LastObject=CDistributedGameTreeServerView


[CLS:CMainFrame]
Type=0
HeaderFile=MainFrm.h
ImplementationFile=MainFrm.cpp
Filter=T
BaseClass=CFrameWnd
VirtualFilter=fWC
LastObject=CMainFrame




[CLS:CAboutDlg]
Type=0
HeaderFile=DistributedGameTreeServer.cpp
ImplementationFile=DistributedGameTreeServer.cpp
Filter=D
LastObject=CAboutDlg

[DLG:IDD_ABOUTBOX]
Type=1
Class=CAboutDlg
ControlCount=4
Control1=IDC_STATIC,static,1342177283
Control2=IDC_STATIC,static,1342308480
Control3=IDC_STATIC,static,1342308352
Control4=IDOK,button,1342373889

[MNU:IDR_MAINFRAME]
Type=1
Class=CMainFrame
Command1=ID_APP_EXIT
Command2=ID_TOOLS_OPTIONS
Command3=ID_TOOLS_STATISTICS
Command4=ID_APP_ABOUT
CommandCount=4

[CLS:MainDialog]
Type=0
HeaderFile=MainDialog.h
ImplementationFile=MainDialog.cpp
BaseClass=CDialog
Filter=D
LastObject=ID_APP_ABOUT
VirtualFilter=dWC

[DLG:IDD_DIALOG2]
Type=1
Class=ServerOptionsDialog
ControlCount=18
Control1=IDOK,button,1342242817
Control2=IDCANCEL,button,1342242816
Control3=IDC_STATIC,static,1342308352
Control4=IDC_EDIT1,edit,1350631552
Control5=IDC_STATIC,static,1342308352
Control6=IDC_EDIT2,edit,1350631552
Control7=IDC_STATIC,static,1342308352
Control8=IDC_EDIT3,edit,1350631552
Control9=IDC_STATIC,static,1342308352
Control10=IDC_EDIT4,edit,1350631552
Control11=IDC_STATIC,static,1342308352
Control12=IDC_EDIT5,edit,1350631552
Control13=IDC_STATIC,static,1342308352
Control14=IDC_EDIT6,edit,1350631552
Control15=IDC_STATIC,static,1342308352
Control16=IDC_EDIT7,edit,1350631552
Control17=IDC_STATIC,static,1342308352
Control18=IDC_EDIT8,edit,1350631552

[CLS:ServerOptionsDialog]
Type=0
HeaderFile=ServerOptionsDialog.h
ImplementationFile=ServerOptionsDialog.cpp
BaseClass=CDialog
Filter=D
VirtualFilter=dWC
LastObject=ID_APP_ABOUT

[TB:IDR_MAINFRAME]
Type=1
Class=?
Command1=ID_SERVERSTART
Command2=ID_SERVERSTOP
Command3=ID_TOOLS_OPTIONS
Command4=ID_TOOLS_STATISTICS
Command5=ID_REFRESH
CommandCount=5


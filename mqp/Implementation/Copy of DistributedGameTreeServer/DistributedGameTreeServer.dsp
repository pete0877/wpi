# Microsoft Developer Studio Project File - Name="DistributedGameTreeServer" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Application" 0x0101

CFG=DistributedGameTreeServer - Win32 Debug
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "DistributedGameTreeServer.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "DistributedGameTreeServer.mak" CFG="DistributedGameTreeServer - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "DistributedGameTreeServer - Win32 Release" (based on "Win32 (x86) Application")
!MESSAGE "DistributedGameTreeServer - Win32 Debug" (based on "Win32 (x86) Application")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "DistributedGameTreeServer - Win32 Release"

# PROP BASE Use_MFC 6
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 6
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MD /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_AFXDLL" /Yu"stdafx.h" /FD /c
# ADD CPP /nologo /MD /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_AFXDLL" /D "_MBCS" /Yu"stdafx.h" /FD /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "NDEBUG" /win32
# ADD BASE RSC /l 0x409 /d "NDEBUG" /d "_AFXDLL"
# ADD RSC /l 0x409 /d "NDEBUG" /d "_AFXDLL"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 /nologo /subsystem:windows /machine:I386
# ADD LINK32 /nologo /subsystem:windows /machine:I386

!ELSEIF  "$(CFG)" == "DistributedGameTreeServer - Win32 Debug"

# PROP BASE Use_MFC 6
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 6
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MDd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_AFXDLL" /Yu"stdafx.h" /FD /GZ /c
# ADD CPP /nologo /MDd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_AFXDLL" /D "_MBCS" /Yu"stdafx.h" /FD /GZ /c
# SUBTRACT CPP /Fr
# ADD BASE MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "_DEBUG" /win32
# ADD BASE RSC /l 0x409 /d "_DEBUG" /d "_AFXDLL"
# ADD RSC /l 0x409 /d "_DEBUG" /d "_AFXDLL"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 /nologo /subsystem:windows /debug /machine:I386 /pdbtype:sept
# ADD LINK32 /nologo /subsystem:windows /debug /machine:I386 /pdbtype:sept
# Begin Custom Build
TargetPath=.\Debug\DistributedGameTreeServer.exe
TargetName=DistributedGameTreeServer
InputPath=.\Debug\DistributedGameTreeServer.exe
SOURCE="$(InputPath)"

"$(TargetPath)output.txt" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	nmake -f "$(TargetName)"ps.mk 
	regsvr32 /s /c "$(TargetName)"ps.dll 
	echo Server Proxy/Stub registration done! 
	
# End Custom Build

!ENDIF 

# Begin Target

# Name "DistributedGameTreeServer - Win32 Release"
# Name "DistributedGameTreeServer - Win32 Debug"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\DistributedGameTreeServer.cpp
# End Source File
# Begin Source File

SOURCE=.\DistributedGameTreeServer.idl
# ADD MTL /h "DistributedGameTreeServer_i.h" /iid "DistributedGameTreeServer_i.c" /Oicf
# End Source File
# Begin Source File

SOURCE=.\DistributedGameTreeServer.rc
# End Source File
# Begin Source File

SOURCE=.\DistributedGameTreeServerDoc.cpp
# End Source File
# Begin Source File

SOURCE=.\DistributedGameTreeServerView.cpp
# End Source File
# Begin Source File

SOURCE=.\Engine.cpp
# End Source File
# Begin Source File

SOURCE=.\EngineConfiguration.cpp
# End Source File
# Begin Source File

SOURCE=.\GameServer.cpp
# End Source File
# Begin Source File

SOURCE=.\GameServerConfiguration.cpp
# End Source File
# Begin Source File

SOURCE=.\GameTreeNode.cpp
# End Source File
# Begin Source File

SOURCE=.\GameType.cpp
# End Source File
# Begin Source File

SOURCE=.\Helper.cpp
# End Source File
# Begin Source File

SOURCE=.\HelperPerformance.cpp
# End Source File
# Begin Source File

SOURCE=.\HelperRegistrar.cpp
# End Source File
# Begin Source File

SOURCE=.\HelperRegistry.cpp
# End Source File
# Begin Source File

SOURCE=.\JobManager.cpp
# End Source File
# Begin Source File

SOURCE=.\MainFrm.cpp
# End Source File
# Begin Source File

SOURCE=.\ServerOptionsDialog.cpp
# End Source File
# Begin Source File

SOURCE=.\SimpleDateTime.cpp
# End Source File
# Begin Source File

SOURCE=.\StdAfx.cpp
# ADD CPP /Yc"stdafx.h"
# End Source File
# Begin Source File

SOURCE=.\StringTool.cpp
# End Source File
# Begin Source File

SOURCE=.\Types.idl
# End Source File
# Begin Source File

SOURCE=.\UnevenDistributionAlgorithm.cpp
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=.\DistributedGameTreeServer.h
# End Source File
# Begin Source File

SOURCE=.\DistributedGameTreeServerDoc.h
# End Source File
# Begin Source File

SOURCE=.\DistributedGameTreeServerView.h
# End Source File
# Begin Source File

SOURCE=.\DistributionAlgorithm.h
# End Source File
# Begin Source File

SOURCE=.\DistributionAlgorithmParameters.h
# End Source File
# Begin Source File

SOURCE=.\Engine.h
# End Source File
# Begin Source File

SOURCE=.\EngineConfiguration.h
# End Source File
# Begin Source File

SOURCE=.\Exceptions.h
# End Source File
# Begin Source File

SOURCE=.\GameServer.h
# End Source File
# Begin Source File

SOURCE=.\GameServerConfiguration.h
# End Source File
# Begin Source File

SOURCE=.\GameServerExceptions.h
# End Source File
# Begin Source File

SOURCE=.\GameTreeNode.h
# End Source File
# Begin Source File

SOURCE=.\GameType.h
# End Source File
# Begin Source File

SOURCE=.\Helper.h
# End Source File
# Begin Source File

SOURCE=.\HelperPerformance.h
# End Source File
# Begin Source File

SOURCE=.\HelperRegistrar.h
# End Source File
# Begin Source File

SOURCE=.\HelperRegistry.h
# End Source File
# Begin Source File

SOURCE=.\InternetAddress.h
# End Source File
# Begin Source File

SOURCE=.\Job.h
# End Source File
# Begin Source File

SOURCE=.\JobManager.h
# End Source File
# Begin Source File

SOURCE=.\LogContext.h
# End Source File
# Begin Source File

SOURCE=.\MainFrm.h
# End Source File
# Begin Source File

SOURCE=.\MoveDecision.h
# End Source File
# Begin Source File

SOURCE=.\MoveStatus.h
# End Source File
# Begin Source File

SOURCE=.\Resource.h
# End Source File
# Begin Source File

SOURCE=.\ServerOptionsDialog.h
# End Source File
# Begin Source File

SOURCE=.\ServerThreadMessage.h
# End Source File
# Begin Source File

SOURCE=.\SimpleDateTime.h
# End Source File
# Begin Source File

SOURCE=.\StdAfx.h
# End Source File
# Begin Source File

SOURCE=.\StringTool.h
# End Source File
# Begin Source File

SOURCE=.\Types.h
# End Source File
# Begin Source File

SOURCE=.\UnevenDistributionAlgorithm.h
# End Source File
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# Begin Source File

SOURCE=.\res\DistributedGameTreeServer.ico
# End Source File
# Begin Source File

SOURCE=.\res\DistributedGameTreeServer.rc2
# End Source File
# Begin Source File

SOURCE=.\DistributedGameTreeServer.rgs
# End Source File
# Begin Source File

SOURCE=.\res\DistributedGameTreeServerDoc.ico
# End Source File
# Begin Source File

SOURCE=.\HelperRegistrar.rgs
# End Source File
# Begin Source File

SOURCE=.\res\toolbar1.bmp
# End Source File
# End Group
# End Target
# End Project
# Section DistributedGameTreeServer : {00000000-0000-0000-8000-000003000000}
# 	1:29:IDR_DISTRIBUTEDGAMETREESERVER:102
# End Section


DistributedGameTreeServerps.dll: dlldata.obj DistributedGameTreeServer_p.obj DistributedGameTreeServer_i.obj
	link /dll /out:DistributedGameTreeServerps.dll /def:DistributedGameTreeServerps.def /entry:DllMain dlldata.obj DistributedGameTreeServer_p.obj DistributedGameTreeServer_i.obj \
		kernel32.lib rpcndr.lib rpcns4.lib rpcrt4.lib oleaut32.lib uuid.lib \

.c.obj:
	cl /c /Ox /DWIN32 /D_WIN32_WINNT=0x0400 /DREGISTER_PROXY_DLL \
		$<

clean:
	@del DistributedGameTreeServerps.dll
	@del DistributedGameTreeServerps.lib
	@del DistributedGameTreeServerps.exp
	@del dlldata.obj
	@del DistributedGameTreeServer_p.obj
	@del DistributedGameTreeServer_i.obj

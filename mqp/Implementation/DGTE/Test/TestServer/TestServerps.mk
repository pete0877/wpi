
TestServerps.dll: dlldata.obj TestServer_p.obj TestServer_i.obj
	link /dll /out:TestServerps.dll /def:TestServerps.def /entry:DllMain dlldata.obj TestServer_p.obj TestServer_i.obj \
		kernel32.lib rpcndr.lib rpcns4.lib rpcrt4.lib oleaut32.lib uuid.lib \

.c.obj:
	cl /c /Ox /DWIN32 /D_WIN32_WINNT=0x0400 /DREGISTER_PROXY_DLL \
		$<

clean:
	@del TestServerps.dll
	@del TestServerps.lib
	@del TestServerps.exp
	@del dlldata.obj
	@del TestServer_p.obj
	@del TestServer_i.obj

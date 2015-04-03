# 
# makefile for threaded Hey Client and Hey Server (using pthreads)
#

LIB = -lpthread
CC = cc

all: hey-server hey bench

bench: bench.c bench.h
	$(CC) -o bench bench.c $(LIB)

hey-server: hey-server.c sock.o hey.h
	$(CC) -o hey-server hey-server.c sock.o $(LIB)

hey: hey.c sock.o hey.h
	$(CC) -o hey hey.c sock.o $(LIB)

sock.o: sock.c sock.h
	$(CC) -c sock.c

clean:
	/bin/rm -f hey hey-server bench *.o

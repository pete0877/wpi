all: hey_server hey

hey_server: hey_server.c sock.o shm.o
	gcc -o hey_server hey_server.c sock.o shm.o

hey: hey.c sock.o
	gcc -o hey hey.c sock.o

sock.o: sock.c sock.h
	gcc -c sock.c 

shm.o: shm.c shm.h
	gcc -c shm.c   

# 
# makefile for chat program
#

CFLAGS = -g

all: hey
	chmod u+s hey

hey: hey.c shm.o
	gcc $(CFLAGS) hey.c shm.o -o hey

shm.o: shm.c shm.h
	gcc -c shm.c

clean:
	/bin/rm -rf *.o core hey


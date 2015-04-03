###
###   Project2 Makefile
###   Author: Sebastian Jastrzebsi (sebby@wpi.edu)
###	  Last updated on 11/06/98.
###
# -lpthread

CC = g++
CFLAGS = -g

all: client server

client: Client.o FileTransferProtocol.o DLLSocket.o Socket.o CRC.o Common.o
	$(CC) $(CFLAGS) Client.o FileTransferProtocol.o DLLSocket.o Socket.o CRC.o Common.o -o client

server: Server.o FileTransferProtocol.o DLLServerSocket.o DLLSocket.o ServerSocket.o Socket.o CRC.o Common.o
	$(CC) $(CFLAGS) Server.o FileTransferProtocol.o DLLServerSocket.o DLLSocket.o ServerSocket.o Socket.o CRC.o Common.o -o server

Client.o: Client.h FileTransferProtocol.h DLLSocket.h Socket.h Common.h
	$(CC) $(CFLAGS) -c Client.cpp

Server.o: Server.h FileTransferProtocol.h DLLServerSocket.h DLLSocket.h ServerSocket.h Socket.h Common.h
	$(CC) $(CFLAGS) -c Server.cpp

FileTransferProtocol.o: FileTransferProtocol.h Socket.h Common.h
	$(CC) $(CFLAGS) -c FileTransferProtocol.cpp

DLLSocket.o: DLLSocket.h Socket.h CRC.h Common.h
	$(CC) $(CFLAGS) -c DLLSocket.cpp

DLLServerSocket.o: DLLServerSocket.h ServerSocket.h Socket.h Common.h
	$(CC) $(CFLAGS) -c DLLServerSocket.cpp

Socket.o: Socket.h Common.h
	$(CC) $(CFLAGS) -c Socket.cpp

ServerSocket.o: ServerSocket.h Socket.h Common.h
	$(CC) $(CFLAGS) -c ServerSocket.cpp

CRC.o: CRC.h
	$(CC) $(CFLAGS) -c CRC.cpp

Common.o: Common.h
	$(CC) $(CFLAGS) -c Common.cpp

release:
	$(MAKE) CFLAGS=-O2 all
	strip client
	strip server

clean:
	rm -f *.o client server



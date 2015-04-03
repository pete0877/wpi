CC = gcc
CFLAGS = -g

OBJ = turtle.o

squares: squares.o $(OBJ)
	$(CC) $(CFLAGS) squares.o $(OBJ) -lm -lX11 -o $@ 

tree: tree.o $(OBJ)
	$(CC) $(CFLAGS) tree.o $(OBJ) -lm -lX11 -o $@

snow: snow.o $(OBJ)
	$(CC) $(CFLAGS) snow.o $(OBJ) -lm -lX11 -o $@

maze:   maze.o fix.o dir.o gdir.o goal.o best.o map.o stack.o
	g++ -o maze maze.o fix.o dir.o gdir.o goal.o best.o map.o stack.o

maze.o:  maze.C maze.h
	 g++ -c maze.C

map.o:   map.C maze.h
	 g++ -c map.C

stack.o: stack.C maze.h
	 g++ -c stack.C

fix.o:   fix.C maze.h
	 g++ -c fix.C

dir.o:   dir.C maze.h
	 g++ -c dir.C

goal.o:  goal.C maze.h
	 g++ -c goal.C

gdir.o:  gdir.C maze.h
	 g++ -c gdir.C

best.o:  best.C maze.h
	 g++ -c best.C

clean: 
	 rm *.o maze


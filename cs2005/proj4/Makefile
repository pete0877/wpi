
proj4tree: proj4tree.o proj4treeaux.o
	g++ -o proj4tree proj4tree.o proj4treeaux.o 

proj4treeaux.o: proj4treeaux.cpp proj4tree.h
	g++ -c proj4treeaux.cpp

proj4tree.o: proj4tree.cpp proj4tree.h
	g++ -c proj4tree.cpp

clean:
	 rm -f *.o proj4tree

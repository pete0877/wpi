include libsx_defs

project : renderer modeler

renderer : renderer.cpp definitions.h matrix.o
	g++ -o renderer renderer.cpp matrix.o $(PETERG)

modeler : modeler.cpp definitions.h matrix.o
	g++ -o modeler modeler.cpp matrix.o $(PETERG)

matrix.o : matrix.cpp matrix.h
	g++ -c matrix.cpp

ok : matrix.o test.cpp
	g++ test.cpp matrix.o

clean :
	rm -f renderer modeler








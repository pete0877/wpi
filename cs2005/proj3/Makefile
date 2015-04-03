airline: airline.o flight.o reserve.o airline.h
	 g++ -o airline airline.o reserve.o flight.o
airline.o: airline.cpp airline.h 
	   g++ -c airline.cpp
flight.o: flight.cpp airline.h
	  g++ -c flight.cpp
reserve.o: reserve.cpp airline.h
	   g++ -c reserve.cpp
clean:
	rm -f *.o airline

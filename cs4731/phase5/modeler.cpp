///////////////////////////////////////////////////////////////////////
// Modeler program version 1.0
// Peter Golaszewski
// CS4831 C98
///////////////////////////////////////////////////////////////////////

#include "definitions.h"

//////////////////////////////////////////////////////////////////////

void main()
{
   cout << "Modeler program: Started\n";

   my_model.Read_In("model.in"); // read in data from the definition file
   my_model.Check();
   cout << "Modeler program: Do you want to list aquired data ? (y/n): ";
   char in;
   cin >> in;
   if (in == 'y') { my_model.Info(); }
   my_model.Write_Out("model.format"); // write data out to the model format file

   cout << "Modeler program: Finished\n";
}

///////////////////////////////////////////////////////////////////////

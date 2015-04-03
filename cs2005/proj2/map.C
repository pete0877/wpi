#include <iostream.h>
#include "maze.h"
                  
int Map::Init(Point &ptStart, Point &ptGoal)
// Initializes map (load file find Start and Goal points
{
	int a,b;
   ifstream mazefile(FILENAME);

	for (a=0; a<MAXROWSIZE; a++)
   	for (b=0; b<MAXCOLSIZE; b++)
   		grid[a][b]=FREE;
   if (!mazefile)
   {
   	cout<<"Could not open "<<FILENAME<<" for reading.\n";
        return(1);
   }
   a=0;
   maxrow=0;
   maxcol=0;
   while ((mazefile.getline(grid[a], MAXCOLSIZE)) && (!mazefile.eof()))
   {
   	if (strlen(grid[a])>maxcol) maxcol=strlen(grid[a]);
   	cout << grid[a] << "\n";
      a++;
      maxrow++;
   }
   mazefile.close();
   ptStart.row=-1;
   ptGoal.row=-1;
   for (a=0; a<MAXROWSIZE; a++)
   	for (b=0; b<MAXCOLSIZE; b++)
   	{
			if (grid[a][b]==START)
         {
         	ptStart.row=a;
            ptStart.col=b;
         }
         if (grid[a][b]==GOAL)
         {
         	ptGoal.row=a;
            ptGoal.col=b;
         }
      }
   if (ptStart.row==-1 || ptGoal.row==-1) return(1);
   cout<<"Maze contains "<<maxrow<<" rows, "<<maxcol<<" columns, ";
   cout<<"start:("<<ptStart.row<<","<<ptStart.col<<"), ";
   cout<<"goal:("<<ptGoal.row<<","<<ptGoal.col<<")\n";
   return(0);
}

Map::SetPoint(Point pt, char ch)
// Setts point on the map as a character
{
	grid[pt.row][pt.col]=ch;
   return(0);
}

char Map::GetPoint(Point pt)
// Returns character on the map unser point
{
	return (grid[pt.row][pt.col]);
}

Map::Print()
// Prints out the maze map
{
	int a;
   for (a=0; a<maxrow; a++) cout << grid[a] << "\n";
	return(0);
}

#include "maze.h"
                  
void CaseDown(char s[]);
int  abs(int n);
void CloseDir(Point p,Point g,Point &first, Point &second);
void help();
void aboutmaze();
void LeftRight(Point dir, Point &left, Point &right);
enum directions SetDirect(Point dir);
FixTraverseMaze(Map &map, Point ptStart);
DirTraverseMaze(Map &map, Point ptStart);
GoalTraverseMaze(Map &map, Point ptStart, Point ptGoal);
GdirTraverseMaze(Map &map, Point ptStart, Point ptGoal);
BestTraverseMaze(Map &map, Point ptStart);


int main ()
{
	Map map;
   char policy[4];
   Point ptStart, ptGoal;
   int finished;

   if (map.Init(ptStart, ptGoal))
   {
   	// no maze map found on disk
		cout<<"Map initialization failed.\n";
		return(0);
   }
   do{
   	cout<<"\nWhat policy to use? (type ? for help) ";
   	cin>>policy;
		CaseDown(policy); //  make sure itsa lowcase
      finished=1;
   	if (!strcmp(policy,"fix")) FixTraverseMaze(map,ptStart);
else
  	   if (!strcmp(policy,"dir")) DirTraverseMaze(map,ptStart);
else
     	if (!strcmp(policy,"goal")) GoalTraverseMaze(map,ptStart,ptGoal);
else
      if (!strcmp(policy,"gdir"))  GdirTraverseMaze(map,ptStart,ptGoal);
else
      if (!strcmp(policy,"best")) BestTraverseMaze(map,ptStart);
else
      if (!strcmp(policy,"about")) 
{
	aboutmaze();
	finished=0;
}
else
      if (!strcmp(policy,"?")) 
{
	help();
	finished=0;

}
else
      finished=0;
	}while (!finished);
}

int abs(int n)
{
	if (n>=0) return (n); else return(-n);
}

void help()
//  Displays a little info on the program and instructions
{
	char c;
cout << "This program implements a set of maze traversal\n";
    cout << "algorithms that are built by using stacks.  A grid \n";
    cout << "was used as the model.  In it, movement from any point \n";
    cout << "is allowed from one of the for directions(north, south, \n";
    cout << "east, west).  This program reads in a maze and prints \n";
    cout << "it out, then asks the user to select from the \n";
    cout << "following traversal algorithms: \n\n";
cout << " Fixed Order(fix)-uses a fixed order in how it tries the four\n";
cout << "neighboring direction.  North, East, South, West is the ";
cout << "order of \n";
    cout << "priority. (Type FIX to run this method).\n\n";
cout << "Type A and press Enter ...";
cin >> c;
cout << "\n";
cout << " Maintain Direction(dir)-gives first priority to maintaining";
cout << "the\n";
    cout << "direction being traveled when the point is entered. \n";
    cout << "(Type DIR to run this method).\n\n";

cout << " Towards Goal(goal)-this algorithm always knows the ";
cout << " location of the \n";
cout << "goal, but has to find the path to reach it.  It gives first\n";
cout << "priority to the veritical or horizontal direction that ";
cout << "is closest \n";
cout << "to the vertical or horizontal direction that is closest to ";
cout << "the\n";
cout << "straight line direction from the current point to the goal.\n";
cout << "Second priority is given to the direction second closest to";
cout << "the \n";
cout << "straightline direction and third priority is opposite the";
cout << "second \n";
cout << "direction and the fourth priority is opposite the first";
cout << "direction. \n";
cout << "(Type GOAL to run this method).\n\n";
cout << "Type A and press Enter ...";
cin >> c;
cout << "\n";
cout << " Towards Goal Maintaining Direction(gdir)-combines the";
cout << "previous two \n";
cout << "algorithms and gives first priority to maintaining the";
cout << "direction \n";
cout << "being traveled when the point is entered.  The remaining";
cout << "direction \n";
cout << "(of the three) that is closest to the straight-line direction";
cout << "from \n";
cout << "the current point to the goal.  The third direction is the";
cout << "next \n";
cout << "closest to the straight-line direction and the remaining";
cout << "direction \n";
cout << "is the last neighbor direction to try. \n";
cout << "(Type GDIR to run this method).\n\n";
cout << " Best(best)-this method find the shortest path between the";
cout << "start \n";
cout << "point and the end point. This algorythm uses fix method";
cout << "described \n";
cout << "above. It loops until it finds the all possible solutions.\n";
    cout << "(Type BEST to run this method).\n\n";

    cout << " type ABOUT to view the software information page \n";
cout << "Type A and press Enter ...";
cin >> c;
cout << "\n";
}

void aboutmaze()
//   About authors , ect.
{
	 cout << "\n\n 			Maze Program, v1.0 \n\n";
    cout << "   Written for project #2 in CS2005@WPI (1996) by :\n\n";
    cout << "  CRAZY ELEPHANTS : \n";
    cout << " Uzma Fathima Ali <uzma@WPI.EDU> \n";
    cout << " Nicholas Ja Tortorelli <vogar@WPI.EDU> \n";
    cout << " Park Hays <phays@WPI.EDU>   \n";
    cout << " Peter Golaszewski <peterg@WPI.EDU> \n";
    cout << " Robert Scaramangos <robertsc@WPI.EDU> \n\n";
}

void CloseDir(Point p,Point g,Point &first, Point &second)
//   Returns offsets to the first closest and second closest direction
//   tords the goal from the current point
{
	int dx,dy;
	dx=g.col-p.col;
	dy=g.row-p.row;
	if (abs(dx)>abs(dy))
	{
		first.col=dx/abs(dx);
		first.row=0;
		second.col=0;
		if (dy!=0) second.row=dy/abs(dy); else second.row=1;
	}
	 else
	{
		first.col=0;
		if (dy!=0) first.row=dy/abs(dy); else
      {
      	first.row=0;
         first.col=dx/abs(dx);
      }
		if (dx!=0) second.col=dx/abs(dx); else
      {
      	second.col=0;
         second.row=dy/abs(dy);
      }
		second.row=0;
	}
}


void LeftRight(Point dir, Point &left, Point &right)
{
	if (dir.col==0)
   {
   	left.row=0;
      right.row=0;
      if (dir.row==-1)
      {
      	left.col=-1;
         right.col=1;
      } else
      {
      	left.col=1;
         right.col=-1;
      }
   } else
   {
   	left.col=0;
      right.col=0;
      if (dir.col==-1)
      {
      	left.row=1;
         right.row=-1;
      } else
      {
      	left.row=-1;
         right.row=1;
      }
   }
}

enum directions SetDirect(Point dir)
{
	if (dir.col==0)
   {
   	if (dir.row==-1) return(north);
      else return(south);
   }
    else
   {
   	if (dir.col==1) return(east);
      else return(west);
   }
}

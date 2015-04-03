#include <iostream.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <fstream.h>

#define FILENAME "mazemap"
#define WALL '#'
#define FREE ' '
#define START '<'
#define GOAL '>'
#define MAXROWSIZE 20
#define MAXCOLSIZE 80
#define INITDIR north
#define MAXSTACKSIZE MAXROWSIZE * MAXCOLSIZE
#define DEADEND '.'
#define CURRENT 'A'

enum directions {north, south, east, west};
struct Point
{
	int row,col;
   enum directions dir;
};

class Map
{
	private:
   	int maxrow,maxcol;
		char grid[MAXROWSIZE][MAXCOLSIZE];
   public:
      int Init(Point &ptStart, Point &ptGoal);
      SetPoint(Point pt, char ch);
      char GetPoint(Point pt);
      Print();
};

class Stack {
	private:
   	Point st[MAXSTACKSIZE];
      int index;
   public:
   	Stack();
   	Push(Point what);
      int Pop(Point &what);
      int Empty();
};

extern void CloseDir(Point p,Point g,Point &first, Point &second);
extern void LeftRight(Point dir, Point &left, Point &right);
extern enum directions SetDirect(Point dir); 

#include "maze.h"

FixTraverseMaze(Map &map, Point ptStart)
// Fixed Order(fix)-uses a fixed order in how it tries the four
// neighboring direction.  North, East, South, West is the order of
// priority.
{
 char step=CURRENT,count;
 Stack stack;
 int WrongStep=0,length;
 char ok;
 Point point,x,direction,tryd;

 stack.Push(ptStart);
 while (!stack.Empty()) {

	stack.Pop(point);
	if (map.GetPoint(point)==GOAL) {
		map.SetPoint(point,step);
		length=int(step-CURRENT+1);
		cout<<"\nPath was found of length "<<length<<" with ";
		cout<<WrongStep<<" wrong points visited.";
		cout<<"\n(Total of "<<2*WrongStep+length<<" moves, ";
		printf("algorythm efficiency: %.2f)\n",
		 (.0+length)/(2*WrongStep+length));
		map.Print();
		return(0);
	}
	map.SetPoint(point,step++);
	stack.Push(point);
	ok=0;

	x.row=point.row;
	x.col=point.col-1;
	if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
	{
		stack.Push(x);
		ok=1;
	}
	x.row=point.row+1;
	x.col=point.col;
	if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
	{
		stack.Push(x);
		ok=1;
	}
	x.row=point.row;
	x.col=point.col+1;
	if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
	{
		stack.Push(x);
		ok=1;
	}
	x.row=point.row-1;
	x.col=point.col;
	if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
	{
		stack.Push(x);
		ok=1;
	}
	if (!ok)
	{
		ok=0;
		do {
			if (stack.Pop(point)==-1) return(-1);
			if (map.GetPoint(point)>=CURRENT)
			{
				map.SetPoint(point,DEADEND);
				step--;
				WrongStep++;
			} else
			{
				stack.Push(point);
				ok=1;
			}
		} while (ok==0);
   }
 }
 cout<<"\n Could not find the Goal.\n";
 map.Print();
 return(0);
}

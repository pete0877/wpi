#include "maze.h"

int
DirTraverseMaze(Map &map, Point ptStart)
// Maintain Direction(dir)-gives first priority to maintaining the
// direction being traveled when the point is entered.
{
 char step=CURRENT,count;
 Stack stack;
 int WrongStep=0,length,a;
 char ok;
 Point point,x,direction,left,right;

 // Guess on direction :
 direction.row=-1;
 direction.col=0;
 ptStart.dir=INITDIR;
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

   // Find directions to check depending on the current direction
   LeftRight(direction, left, right);
   // Right
	x.row=point.row+right.row;
	x.col=point.col+right.col;
	if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
	{
   	x.dir=SetDirect(right);
		stack.Push(x);
		ok=1;

	}
   // Left
	x.row=point.row+left.row;
	x.col=point.col+left.col;
	if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
	{
   	x.dir=SetDirect(left);

		stack.Push(x);
		ok=1;
	}
	// Mantain dir.
	x.row=point.row+direction.row;
	x.col=point.col+direction.col;
	if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
	{
   	x.dir=SetDirect(direction);

		stack.Push(x);
		ok=1;
	}
	if (!ok)
	{
   	// no optional direction
		ok=0;
		do {
			if (stack.Pop(point)==-1) 
			{
 				cout<<"\n Could not find the Goal.\n";
 				map.Print();
				return(0);
			}
 if (map.GetPoint(point)>=CURRENT)  
			{
				map.SetPoint(point,DEADEND);

				step--;
				WrongStep++;
			} else
			{
         	// set up the current Direction
				stack.Push(point);
            switch (point.dir)
            {
           	case north:
               direction.row=-1;
               direction.col=0;
               break;
            case east:
               direction.row=0;
               direction.col=1;
               break;
            case south:
               direction.row=1;
               direction.col=0;
               break;
            case west:
               direction.row=0;
               direction.col=-1;
               break;
            }
				ok=1;
			}
		} while (ok==0);
   }
    else
   {
   	stack.Pop(x);
      stack.Push(x);
      // New direction :
		direction.row=x.row-point.row;
      direction.col=x.col-point.col;
   }
 }
 cout<<"\n Could not find the Goal.\n";
 map.Print();
 return(0);
}


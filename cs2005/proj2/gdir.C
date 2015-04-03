#include "maze.h"

GdirTraverseMaze(Map &map, Point ptStart, Point ptGoal)
// Towards Goal Maintaining Direction(gdir)-combines the previous two
// algorithms and gives first priority to maintaining the direction
// being traveled when the point is entered.  The remaining direction
// (of the three) that is closest to the straight-line direction from
// the current point to the goal.  The third direction is the next
// closest to the straight-line direction and the remaining direction
// is the last neighbor direction to try.
{
 char c,step=CURRENT,count;
 Stack stack;
 Point ways[4],point,tempdir1,tempdir2,direction,tryd;
 int dirs[3][3],WrongStep=0,a,length;    // Checked directions array

 // Initialize direction
 CloseDir(ptStart,ptGoal,direction,tempdir1);
 ptStart.dir=INITDIR;

 stack.Push(ptStart);
 while (!stack.Empty()) {
   do {
         if (stack.Pop(point)==-1)
			{
         	 // No way out
   			 cout<<"\n ! Could not find the Goal.\n";
   			 map.Print();
			    return(0);
			}
 if (map.GetPoint(point)>=CURRENT)  
         {
         	// Put DEADEND Here
         	map.SetPoint(point,DEADEND);
            step--;
            WrongStep++;
         }
          else
         if (map.GetPoint(point)==FREE || map.GetPoint(point)==START)
count=1;
   } while (count==0);

	if (map.GetPoint(point)==GOAL) {
      // Found exit !
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

	// Clean up the  array
	dirs[0][1]=0;
	dirs[1][0]=0;
	dirs[1][2]=0;
	dirs[2][1]=0;

   // Find closest and second closest directions
	CloseDir(ptStart,ptGoal,tempdir1,tempdir2);

   count=0;

   // try to mantain direction
	tryd.row=point.row+direction.row;
	tryd.col=point.col+direction.col;
 	tryd.dir=SetDirect(direction);
	c=map.GetPoint(tryd);
	if (c==FREE || c==GOAL)
	{
   	// ok
		ways[count].row=tryd.row;
		ways[count++].col=tryd.col;
		dirs[direction.col+1][direction.row+1]=1;
	}
   // Check the closest direction
	tryd.row=point.row+tempdir1.row;
	tryd.col=point.col+tempdir1.col;
 	tryd.dir=SetDirect(tempdir1);
   c=map.GetPoint(tryd);
   if ((!dirs[tempdir1.col+1][tempdir1.row+1]) && (c==FREE || c==GOAL))
	{
   	// ok
		ways[count].row=tryd.row;
		ways[count++].col=tryd.col;
		dirs[tempdir1.col+1][tempdir1.row+1]=1;
	}
   // Check second closest direction :
	tryd.row=point.row+tempdir2.row;
	tryd.col=point.col+tempdir2.col;
 	tryd.dir=SetDirect(tempdir2);
   c=map.GetPoint(tryd);
   if ((!dirs[tempdir2.col+1][tempdir2.row+1]) && (c==FREE || c==GOAL))
	{
   	// ok
		ways[count].row=tryd.row;
		ways[count++].col=tryd.col;
		dirs[tempdir2.col+1][tempdir2.row+1]=1;
	}
   // Find out which directions weren't used and check them
	// LEFT
	if (!dirs[0][1])
	{
		tryd.row=point.row;
		tryd.col=point.col-1;
      tryd.dir=west;
	   c=map.GetPoint(tryd);
      if (c==FREE || c==GOAL)
      {

                	ways[count].row=tryd.row;
                	ways[count++].col=tryd.col;
      }
	}
	if (!dirs[1][0])
	{
   	// UP
		tryd.row=point.row-1;
		tryd.col=point.col;
      tryd.dir=north;
      c=map.GetPoint(tryd);
      if (c==FREE || c==GOAL)
      {
                	ways[count].row=tryd.row;
                	ways[count++].col=tryd.col;
      }
	}
	if (!dirs[1][2])
	{
      // DOWN
		tryd.row=point.row+1;
		tryd.col=point.col;
      tryd.dir=south;
      c=map.GetPoint(tryd);
      if (c==FREE || c==GOAL)
      {
                	ways[count].row=tryd.row;
                	ways[count++].col=tryd.col;
      }
	}
	if (!dirs[2][1])
	{
   	// RIGHT
		tryd.row=point.row;
		tryd.col=point.col+1;
      tryd.dir=east;
      c=map.GetPoint(tryd);
      if (c==FREE || c==GOAL)
      {
                	ways[count].row=tryd.row;
                	ways[count++].col=tryd.col;
      }
	}
   // If no neighboors pushed, then go back
	if (count==0)
	{
      do {
         if (stack.Pop(point)==-1)
			{
   			 cout<<"\n !! Could not find the Goal.\n";
   			 map.Print();
			    return(0);
			}
 if (map.GetPoint(point)>=CURRENT)  
         {
         	map.SetPoint(point,DEADEND);
            step--;
            WrongStep++;
         }
          else
         {
			   if (map.GetPoint(point)==FREE)
			   {
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
               count=1;
			   }
          }
      } while (count==0);
	}
    else
	{
      // Push new options on the stack
 		for (a=count-1;a>=0; a--) stack.Push(ways[a]);
      // memorize new direction :
		direction.row=ways[0].row-point.row;
		direction.col=ways[0].col-point.col;
	}
 }
 // No wya out
 cout<<"\n !!! Could not find the Goal.\n";
 map.Print();
 return(0);
}


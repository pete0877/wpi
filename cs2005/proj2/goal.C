#include "maze.h"

GoalTraverseMaze(Map &map, Point ptStart, Point ptGoal)
//  Towards Goal(goal)-this algorithm always knows the location of the
// goal, but has to find the path to reach it.  It gives first
// priority to the veritical or horizontal direction that is closest
// to the vertical or horizontal direction that is closest to the
// straight line direction from the current point to the goal.
// Second priority is given to the direction second closest to the
// straightline direction and third priority is opposite the second
// direction and the fourth priority is opposite the first direction.
{
	char step=CURRENT, count = 0;
   Stack stack;
   Point point, tempdir1, tempdir2, tryd;
   int WrongStep=0, length,ok;

   stack.Push(ptStart);
   while(!stack.Empty())
   {
   	stack.Pop(point);
      if(ptGoal.row == point.row && ptGoal.col == point.col)
      { // if we're a the goal print the map and algorithm evaluation
//data.
      	map.SetPoint(point,step);
      	length = int(step-CURRENT+1);
cout<<"\nPath was found of length "<<length<<" with ";
cout<<(WrongStep)<<" wrong points visited. "<<"\n(Total of";
cout<<((2*(WrongStep))+length)<<" moves, ";
			printf("algorithm efficiency: %.2f)\n",
         	(.0+length)/(2*(WrongStep)+length));
			map.Print();
			return(0);
		}
      map.SetPoint(point,step++);
		stack.Push(point);

      //decide the next point to visit
      CloseDir(point,ptGoal,tempdir1,tempdir2);
      tryd.col = 0;
      tryd.row = 0;
      count = 0;

      //is it free on the opposite side of the ideal?
      tryd.col = point.col -tempdir1.col;
      tryd.row = point.row -tempdir1.row;
      if(map.GetPoint(tryd) == FREE || map.GetPoint(tryd) == GOAL)
      {
      	stack.Push(tryd);
         count++;
      }

      //is it free on the opposite side of the second choice?
      tryd.col = point.col -tempdir2.col;
      tryd.row = point.row -tempdir2.row;
      if(map.GetPoint(tryd) == FREE || map.GetPoint(tryd) == GOAL)
      {
      	stack.Push(tryd);
         count++;
      }

      //is the second choice free?
      tryd.col = point.col +tempdir2.col;
      tryd.row = point.row +tempdir2.row;
      if(map.GetPoint(tryd) == FREE || map.GetPoint(tryd) == GOAL)
      {
      	stack.Push(tryd);
         count++;
      }

      //is the first choice free?
      tryd.col = point.col +tempdir1.col;
      tryd.row = point.row +tempdir1.row;
      if(map.GetPoint(tryd) == FREE || map.GetPoint(tryd) == GOAL)
      {
      	stack.Push(tryd);
         count++;
      }

      if (count==0) //what to do it we are at a deadend
		{
   ok=0;
		do {
			if (stack.Pop(point)==-1)
         {
            	cout<<"\n Could not find the Goal.\n";
      			map.Print();
         }
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
//      WrongStep--;   //needed to undo the additional entry that occurs
//when the
      					//loop is first started.
      }
	}
 // No wya out
 cout<<"\n Could not find the Goal.\n";
 map.Print();
 return(0);
}


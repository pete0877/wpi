#include "maze.h"

BestTraverseMaze(Map &map, Point ptStart)
// Best(best)-this method find the shortest path between the start
// point and the end point. This algorythm uses fix method described
// above. It loops until it finds the all possible solutions.
{
 char c,ok,step=CURRENT,count;
 Stack stack,temp,bestpath;
 Point point,x,tmpoint;
 int a,WrongStep=0,length;
 int record=MAXSTACKSIZE;

 stack.Push(ptStart);
 while (!stack.Empty()) {

  stack.Pop(point);
  do {
   /// Check if the point is on the Goal
   if (map.GetPoint(point)==GOAL)
	{
  	  length=int(step-CURRENT+1);
	  if (length<record)
	  {
     	  // New shorter path ... clear bestpath and store new one
        while(bestpath.Pop(tmpoint)==0);
        bestpath.Push(point);
	     do {
	  	  	a=stack.Pop(tmpoint);
			if (a==0)
			{
				temp.Push(tmpoint);
 if (map.GetPoint(tmpoint)>=CURRENT)  
bestpath.Push(tmpoint);
			}
	     } while (a==0);
	     do {
                a=temp.Pop(tmpoint);
                if (a==0) stack.Push(tmpoint);
        } while (a==0);
	     record=length;
	  }
     count=0;
     // Pop steps from the stack
     do {
           if (stack.Pop(point)==-1)
           {
   				if (record==MAXSTACKSIZE)
   				{
   					cout<<"\n Could not find the
Goal.\n";
      				map.Print();
   				} else
   				{

			cout<<"\nPath was found of length "<<record<<":";
						step=CURRENT;
						do {
	   					a=bestpath.Pop(point);
	   					if (a==0)
map.SetPoint(point, step++);
					   } while (a==0);
                  map.Print();
 					}
               return(0);
           }
 if (map.GetPoint(point)>=CURRENT)  
           {
              map.SetPoint(point,FREE);
              step--;
              WrongStep++;
           } else
           {
              		if (map.GetPoint(point)!=GOAL) stack.Push(point);
               	count=1;
           }
     } while (count==0);
   } else stack.Push(point);
   // Quit when reached free space
  } while (map.GetPoint(point)==GOAL);
   map.SetPoint(point,step++);
   ok=0;
	// Push new possible directions on the stack:
        x.row=point.row;  // WEST
        x.col=point.col-1;
        if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
        {
                stack.Push(x);
                ok=1;
        }
        x.row=point.row+1; // SOUTH
        x.col=point.col;

        if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
        {
                stack.Push(x);
                ok=1;
        }
        x.row=point.row;  // EAST
        x.col=point.col+1;
        if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
        {
                stack.Push(x);
                ok=1;
        }
        x.row=point.row-1;  // NORTH
        x.col=point.col;
        if (map.GetPoint(x)==FREE || map.GetPoint(x)==GOAL)
        {
                stack.Push(x);
                ok=1;
        }
   if (!ok)
   {
   	// Dead end:
		count=0;
      do {
      	if (stack.Pop(point)==-1)
         {
   			if (record==MAXSTACKSIZE)
   			{
   				cout<<"\n Could not find the Goal.\n";
      			map.Print();
   			} else
            {
			cout<<"\nPath was found of length "<<record<<":\n";
					step=CURRENT;
					do {
	   				a=bestpath.Pop(point);
	   				if (a==0)
                  {
                  	map.SetPoint(point, step++);
                  }
					} while (a==0);
               map.Print();
 				}
            return(0);
         }
 if (map.GetPoint(point)>=CURRENT)  
         {
         	map.SetPoint(point,FREE);
            step--;
            WrongStep++;
         } else
         {
            	stack.Push(point);
               count=1;
         }
      } while (count==0);
   }
 }
 // did we find any paths ?
 if (record==MAXSTACKSIZE)
 {
   	cout<<"\n Could not find the Goal.\n";
      map.Print();
 } else
 {
	cout<<"\nPath was found of length "<<record<<":\n";
	step=CURRENT;
	do {
	   a=bestpath.Pop(point);
	   if (a==0)
      {
   		map.SetPoint(point, step++);
      }
	} while (a==0);
   map.Print();
   // close
 }
}


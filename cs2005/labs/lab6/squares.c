#include "turtle.h"

void main()
{
	/* initialize turtle graphics */
	init_turtle();

	/* draw square */
	square(100, 3);

	/* wait for user to click right mouse button */
	wait_button();

}

square( int length, int level )
{
	if( level == 0 )
		return;

	repeat (4)
	{
		forward(length);
		right(90);
	}
        square(length/2, level-1);
  
}

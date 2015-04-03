#include "turtle.h"

void main()
{
	/* initialize turtle graphics */
	init_turtle();

	/* draw snowflake */
	snowflake(200, 4);

	/* wait for user to click right mouse button */
	wait_button();
}

snowflake( int length, int level )
{
	if( level == 0 )
	{
		forward( length );
		return;
	}

	snowflake( length/3, level-1 );
	left( 60 );
	snowflake( length/3, level-1 );
	right( 120 );
	snowflake( length/3, level-1 );
	left( 60 );
	snowflake( length/3, level-1 );
}
	

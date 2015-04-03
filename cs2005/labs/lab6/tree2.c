
#include "turtle.h"

void main()
{
	/* initialize turtle graphics */
	init_turtle();

	/* draw 'V' */
	vee(40, 8);
	right(180);
	vee(40, 8);


	/* wait for user to click right mouse button */
	wait_button();

}

vee( int length, int level )
{
	if( level == 0 )
		return;

	/* 
	 * draw right branch 
	 */
	right( 45 + 30 );
	forward( length );
vee(length*0.7, level-1);

	back( length );
	left( 90 );

	/* 
	 * draw left branch 
	 */
	forward( length );
vee(length*0.7, level-1);

        back( length );

	right( 45 - 30);




}


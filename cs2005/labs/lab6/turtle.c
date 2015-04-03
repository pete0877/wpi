/************************************************************************/
/* headers								*/
/************************************************************************/
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <X11/Xos.h>
#include <stdio.h>
#include <math.h>

/************************************************************************/
/* defines								*/
/************************************************************************/
#define BOOLEAN		int
#define FALSE		0
#define TRUE		1

#define INVISIBLE	0
#define VISIBLE		1

#define DEFAULT_TURTLE_BASE	1
#define DEFAULT_TURTLE_HEIGHT	1
#define DEFAULT_PENWIDTH	1

#define EAST		180
#define	NORTH		270
#define	SOUTH		90	
#define WEST		0

#define TIP		0
#define LEFT		1
#define RIGHT		2

#define WHITE		0
#define BLACK		1

#define DEG2RAD( a )      ( (double)((a) * 0.01745329251994329576) )
#define RAD2DEG( a )      ( (double)((a) * 57.29577951308232087684) )

#define EPSILON		  0.00001

#define ABS( a )          ( ((a) > 0 )? (a) : (-(a)) )
#define FLT_EQUAL( a, b ) ( ABS((a) - (b)) < EPSILON )
#define FLT_ZERO( a )     ( ABS((a)) <= EPSILON )
#define FLT_LT_ZERO( a )  ( (a) < EPSILON )
#define FLT_GT_ZERO( a )  ( (a) > EPSILON )
#define FLT_NON_ZERO( a ) ( ABS((a)) > EPSILON )

#define set_pt( xpt, pt )	{ xpt.x = (int)pt.x; xpt.y = (int)pt.y; }

#define MAX_NESTED_REPEAT	100

/*
 * window defines
 */
#define WINDOW_NAME		"turtle - click right mouse button to quit"
#define WINDOW_X_INIT	 	100
#define WINDOW_Y_INIT		100
#define DEFAULT_WIN_WIDTH	400
#define DEFAULT_WIN_HEIGHT	400
#define BORDER_WIDTH		1

/************************************************************************/
/* typedefs								*/
/************************************************************************/
typedef struct pt{
	double x;
	double y;
	}PT;

struct turtle{
	double	heading;
	PT	loc;
	PT	corners[3];
	int	half_base;
	int	height;
	int	pen_width;
	BOOLEAN	pendown;
	BOOLEAN	visible;
	}Turtle;

/************************************************************************/
/* public global variables						*/
/************************************************************************/
int _cnts[MAX_NESTED_REPEAT];
int _lvl = 0;

/************************************************************************/
/* private global variables						*/
/************************************************************************/
static Display *display;
static int screen = 0;
static XEvent event;
static Window win;
static GC gc;
static unsigned long black;
static unsigned long white;
static int win_width;
static int win_height;
static int cx;
static int cy;
static int first_time = TRUE;

/************************************************************************/
/* public functions							*/
/************************************************************************/
/*
 * setup functions
 */
void init_turtle();
void init_turtle_complex( int, int, int, int, int );
void wait_button();

/*
 * turtle commands
 */
void home();
void right( int );
void left( int );
void forward( int );
void back( int );
void setheading( int );
void setxy( int, int );
int xcor();
int ycor();
void penup();
void pendown();
void clear();
void hide();
void show();

/************************************************************************/
/* private functions							*/
/************************************************************************/
static void set_turtle( double, int, int, int, int, int, int, int );
static void recalc_corners( double );
static void shift_turtle( double, double );
static void draw_turtle();

/************************************************************************/
/*									*/
/************************************************************************/
void init_turtle()
{
	init_turtle_complex( DEFAULT_WIN_WIDTH, DEFAULT_WIN_HEIGHT,
			     DEFAULT_TURTLE_BASE, DEFAULT_TURTLE_HEIGHT, 
			     DEFAULT_PENWIDTH );
}

/************************************************************************/
/*									*/
/************************************************************************/
void init_turtle_complex( width, height, turtle_base, turtle_height, pen_width )
int width;
int height;
int turtle_base;
int turtle_height;
int pen_width;
{
	XGCValues	values;
	unsigned long	valuemask = 0;
	char		*dname = NULL;

	
	if( (display = XOpenDisplay(dname)) == NULL )
	{
		fprintf( stderr, "Cannot connect to X server %s\n", dname );
		exit( 1 );
	}


	black = BlackPixel( display, screen );
	white = WhitePixel( display, screen );

	win = XCreateSimpleWindow( display, RootWindow(display, screen),
					WINDOW_X_INIT, WINDOW_Y_INIT,
					width, height,
					BORDER_WIDTH,
					black, white );

	XSetStandardProperties( display, win, WINDOW_NAME, (char *)NULL,
					(Pixmap)NULL, (char **)NULL, 0,
					(XSizeHints *)NULL );

	XSelectInput( display, win, ExposureMask | ButtonPressMask );

	XSynchronize( display, TRUE );
	
	gc = XCreateGC( display, win, valuemask, &values );

	XSetForeground( display, gc, black );
	XSetBackground( display, gc, white );

	XSetLineAttributes( display, gc, (unsigned int)pen_width, 
			    LineSolid, CapButt, JoinMiter );

	XSetFillStyle( display, gc, FillSolid );

	XMapWindow( display, win );

	win_width = width;
	win_height = height;
	cx = win_width/2;
	cy = win_height/2;

	set_turtle( NORTH, turtle_base, turtle_height, 
		    cx, cy, pen_width, TRUE, VISIBLE );

	while( TRUE )
	{
                XNextEvent( display, &event );

                switch( event.type )
                {
                        case Expose:
                                XClearWindow( display, win );
				draw_turtle();
				return;
                                break;

                        default:
				break;

                }
        }

}

/************************************************************************/
/*									*/
/************************************************************************/
void wait_button()
{

	while( TRUE )
	{
                XNextEvent( display, &event );

                switch( event.type )
                {
			/*
                        case Expose:
                                XClearWindow( display, win );
                                break;
			*/

                        case ButtonPress:
                                if( event.xbutton.button == Button3 )
					return;
                                else
                                        XBell( display, 100 );
                                break;

                        default:
                                XBell( display, 100 );
                                break;

                }
        }
}

/************************************************************************/
/*									*/
/************************************************************************/
void set_turtle( heading, base, height, x, y, pen_width, pen_down, visible )
double heading;
int base;
int height;
int x, y;
int pen_width;
int pen_down;
int visible;
{
	/*
	 * core settings
	 */
	Turtle.heading = heading;
	Turtle.half_base = base;
	Turtle.height = height;
	Turtle.loc.x = (double)x;
	Turtle.loc.y = (double)y;

	/*
	 * state variables
	 */
	Turtle.pen_width = pen_width;
	Turtle.pendown = pen_down;
	Turtle.visible = FALSE;

	/*
	 * turtle corners
	 */
	recalc_corners( heading );

	Turtle.visible = visible;

}

/************************************************************************/
/*									*/
/************************************************************************/
void recalc_corners( angle )
double angle;
{
	double h = (double)Turtle.height;
	double b = (double)Turtle.half_base;
	double x = Turtle.loc.x;
	double y = Turtle.loc.y;

	if( Turtle.visible )
		draw_turtle();

	Turtle.corners[TIP].x = x + h * cos(DEG2RAD(angle));
	Turtle.corners[TIP].y = y + h * sin(DEG2RAD(angle));

	Turtle.corners[RIGHT].x = x + b * cos(DEG2RAD(angle-90.0));
	Turtle.corners[RIGHT].y = y + b * sin(DEG2RAD(angle-90.0));

	Turtle.corners[LEFT].x = x + b * cos(DEG2RAD(angle+90.0));
	Turtle.corners[LEFT].y = y + b * sin(DEG2RAD(angle+90.0));

	if( Turtle.visible )
		draw_turtle();

}

/************************************************************************/
/*									*/
/************************************************************************/
void shift_turtle( deltax, deltay )
double deltax, deltay;
{
	double x = Turtle.loc.x;
	double y = Turtle.loc.y;

	if( FLT_ZERO(deltax) && FLT_ZERO(deltay) )
		return;

	if( Turtle.visible )
		draw_turtle();

	Turtle.loc.x += deltax;
	Turtle.loc.y += deltay;

	Turtle.corners[TIP].x += deltax;
	Turtle.corners[TIP].y += deltay;

	Turtle.corners[RIGHT].x += deltax;
	Turtle.corners[RIGHT].y += deltay;

	Turtle.corners[LEFT].x += deltax;
	Turtle.corners[LEFT].y += deltay;

	if( Turtle.pendown )
		XDrawLine(display, win, gc, 
				(int)x, (int)y, 
				(int)Turtle.loc.x, (int)Turtle.loc.y );

	if( Turtle.visible )
		draw_turtle();
}

/************************************************************************/
/*									*/
/************************************************************************/
void draw_turtle()
{
}

/************************************************************************/
/*									*/
/************************************************************************/
void home()
{
	Turtle.loc.x = cx;
	Turtle.loc.y = cx;
	Turtle.heading = NORTH;
	
	recalc_corners( NORTH );

}

/************************************************************************/
/*									*/
/************************************************************************/
void penup()
{
	Turtle.pendown = FALSE;
}

/************************************************************************/
/*									*/
/************************************************************************/
void pendown()
{
	Turtle.pendown = TRUE;
}

/************************************************************************/
/*									*/
/************************************************************************/
void setxy( x, y )
int x, y;
{
	if( x == (int)Turtle.loc.x && y == (int)Turtle.loc.y )
		return;

	shift_turtle( (double)x-Turtle.loc.x, (double)y-Turtle.loc.y );
}

/************************************************************************/
/*									*/
/************************************************************************/
int xcor()
{
	return( Turtle.loc.x );
}

/************************************************************************/
/*									*/
/************************************************************************/
int ycor()
{
	return( Turtle.loc.y );
}

/************************************************************************/
/*									*/
/************************************************************************/
void setheading( angle )
int angle;
{
	if( angle == Turtle.heading )
		return;

	Turtle.heading = angle;
	recalc_corners( angle );
	
}

/************************************************************************/
/*									*/
/************************************************************************/
void right( angle )
int angle;
{
	if( angle == 0 )
		return;

	if( (Turtle.heading += angle) > 360 )
		 Turtle.heading -= 360;

	recalc_corners( Turtle.heading );
}

/************************************************************************/
/*									*/
/************************************************************************/
void left( angle )
int angle;
{
	if( angle == 0 )
		return;

	if( (Turtle.heading -= angle) < 0 )
		 Turtle.heading += 360;

	recalc_corners( Turtle.heading );
}

/************************************************************************/
/*									*/
/************************************************************************/
void forward( distance )
int distance;
{
	double angle = DEG2RAD(Turtle.heading);
	double length = (double)distance;
	double deltax, deltay;

	if( distance == 0 )
		return;

	deltax = length * cos(angle);
	deltay = length * sin(angle);

	shift_turtle( deltax, deltay );

}

/************************************************************************/
/*									*/
/************************************************************************/
void back( distance )
int distance;
{
	double angle = DEG2RAD(Turtle.heading);
	double length = (double)distance;
	double deltax, deltay;

	if( distance == 0 )
		return;

	deltax = length * cos(angle);
	deltay = length * sin(angle);

	shift_turtle( -deltax, -deltay );

}

/************************************************************************/
/*									*/
/************************************************************************/
void clear()
{
	XClearWindow( display, win );

	first_time = TRUE;

	draw_turtle();
}

/************************************************************************/
/*									*/
/************************************************************************/
void hide()
{
	Turtle.visible = FALSE;
}

/************************************************************************/
/*									*/
/************************************************************************/
void show()
{
	Turtle.visible = TRUE;
}

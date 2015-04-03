/************************************************************************/
/* defines								*/
/************************************************************************/
#define repeat( x )  for( _cnts[++_lvl] = 0; _cnts[_lvl] < (x) || --_lvl == -1;\
								 _cnts[_lvl]++ )

/************************************************************************/
/* external variables							*/
/************************************************************************/
extern int _cnts[];
extern int _lvl;

/************************************************************************/
/* functions								*/
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

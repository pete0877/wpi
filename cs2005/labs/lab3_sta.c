/*
 * /cs/cs2005/pub/labs/lab3_stack.C -- stack routines
 */

#include "lab3_stack.h"

/*
 * Stack - initialize the stack
 */
Stack::Stack()
{
    top = 0;
}

/*
 * Push - push an item on the stack, return zero for success, -1 on error.
 */
int Stack::Push(Item_type item)
{
    if (top >= MAXSTACK)
	return(-1);
    else {
	rgItem[top++] = item;
	return(0);
    }
}

/*
 * Pop - pop an item off the stack, return zero for success, -1 on error.
 */
Stack::Pop(Item_type &item)
{
    if (top <= 0)
	return(-1);
    else {
	item = rgItem[--top];
	return(0);
    }
}

/*
 * FullStack - is the stack full?
 */
Boolean Stack::FullStack()
{
    return(top >= MAXSTACK);
}

/*
 * EmptyStack - is the stack empty?
 */
Boolean Stack::EmptyStack()
{
    return(top <= 0);
}

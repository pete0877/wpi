/*
 * /cs/cs2005/pub/labs/lab3_rr.C -- reverse a line
 */

#include <iostream.h>
#include "lab3_stack.h"

void main(void)
{
    Stack stack;
    Item_type item;

    // read a character and check for end-of-file (^D) on input
    // cin of a character does not read newline char
    // ADD THE FOLLOWING LINE BEFORE THE LOOP WHEN EDITING
    // cout << "Do not forget to use ^D (cntl-D) to indicate end of input\n"
    while ((cin >> item) && (!cin.eof())) {
	if (stack.Push(item) < 0) {
	    cout << "No more room on stack.\n";
	    exit(1);
	}
    }

    while (stack.Pop(item) >= 0)
	cout << item;
    cout << "\n";
}

/* /cs/cs2005/pub/labs/lab3_stack.h -- header file for stack example */

// EDIT BY INCREASING THE SIZE OF THE STACK TO 100

#define MAXSTACK 10

typedef char Item_type;

typedef short Boolean;

class Stack {
  private:
    int top;        /* top of stack */
    Item_type rgItem[MAXSTACK];
  public:
    Stack();
    Boolean EmptyStack();
    Boolean FullStack();
    int Push(Item_type item);
    int Pop(Item_type &item);
};


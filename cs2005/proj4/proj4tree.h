#include <fstream.h>
#include <string.h>
#include <stdio.h>

/*
 * proj4tree.h -- header file for project 4 on trees
 *
 * Programmer -- Craig E. Wills
 *
 * Date -- 12/6/96
 */

/* tree node definition */
struct TreeNodeType {
    char chNode;		/* value of the node */
    TreeNodeType *pTreeLeft; /* pointer to leftmost child */
    TreeNodeType *pTreeNext; /* pointer to next sibling */
};


typedef short BOOLEAN;
#define TRUE 1
#define FALSE 0

#define PROMPT "Command (a,d,e,h,l,m,n,p,q,s): "
#define ROOT '*'		/* root indication character */
#define MAXLINE 128
#define COMMANDSIZE 32

/* errors */
#define EEXIST -1
#define EMULTROOT -2
#define EPARENT -3
#define ENOMEMORY -4
#define ENOTLEAF -5
#define ENOEXIST -6

/* prototypes */
TreeNodeType *MakeTreeNode(char chNode);
TreeNodeType *FindParentNode(TreeNodeType *pTreeRoot, char chNode);
TreeNodeType *FindNode(TreeNodeType *pTreeRoot, char chNode);
int AddNode(TreeNodeType * &pTreeRoot, char chParent, char chNode);
int DeleteNode(TreeNodeType * &pTreeRoot, char chNode);
void ShowTree(TreeNodeType *pTreeRoot, int wLevel);
int ComputeHeight(TreeNodeType *pTreeRoot);
int CountNode(TreeNodeType *pTreeRoot);
int CountLeafNode(TreeNodeType *pTreeRoot);
int ReadTree(TreeNodeType * &pTreeRoot, char *sbFile);
void PrintMenu();

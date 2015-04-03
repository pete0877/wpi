/*
 * proj4tree.C -- driver program for project 4 on trees
 *
 * Programmer -- Craig E. Wills
 *
 * Date -- 12/6/96
 */

#include "proj4tree.h"
#include "stdio.h"
main(int argc, char *argv[])
{
    TreeNodeType *pTreeRoot;	/* root of the tree */
    TreeNodeType *pTree;
    char sbLine[MAXLINE];	/* store a line of data */
    char *sbCommand;		/* command name */
    char *pch;
    int cCom;			/* length of command name */
    BOOLEAN bDone;
    int wRet;
    char chNode, chParent;

    pTreeRoot = NULL;		/* initialize the tree */
    if (argc >= 2) {
	if (ReadTree(pTreeRoot, argv[1]) < 0) {
	    cout << "Initialization of the tree from file " <<
		argv[1] << " failed.\n";
	    exit(1);
	}
    }

    /* main loop */
    bDone = FALSE;
PrintMenu();
    cout << PROMPT;
    while ((!bDone) && (cin.getline(sbLine, MAXLINE)) && !cin.eof()) {
	sbCommand = sbLine;	/* set to first character in the line */
	pch = sbLine;
	while ((*pch != ' ') && (*pch != '\0'))
	    pch++;
	if (*pch == ' ')
	    *pch++ = '\0';  /* terminate command/leave pch pointing to remain*/
	cCom = strlen(sbCommand); /* save command length */
	if (cCom == 0) {
	    cout << PROMPT;
	    continue;		/* loop again if blank input */
	}

	if (strncmp(sbCommand, "addnode", cCom) == 0) {
	    if (sscanf(pch, "%c %c", &chParent, &chNode) != 2)
		cout << "Need parent and node arguments\n";
	    else {
		if ((wRet = AddNode(pTreeRoot, chParent, chNode)) == EMULTROOT)
		    cout << "Add failed, cannot give a new root\n";
		else if (wRet == EEXIST)
		    cout << "Add failed, node " << chNode << 
			" already exists\n";
		else if (wRet == EPARENT)
		    cout << "Add failed, parent node " << chParent << 
			" not found\n";
		else if (wRet == ENOMEMORY)
		    cout << "Add failed, no space available\n";
		else
		    cout << "Add of " << chNode << " succeeded\n";
	    }
	}

	else if (strncmp(sbCommand, "deletenode", cCom) == 0) {
	    if (sscanf(pch, "%c", &chNode) != 1)
		cout << "Need a node argument\n";
	    else {
		if ((wRet = DeleteNode(pTreeRoot, chNode)) == ENOTLEAF)
		    cout << "Delete failed, node " << chNode << 
			" is not a leaf\n";
		else if (wRet == ENOEXIST)
		    cout << "Delete failed, node " << chNode << " not found\n";
		else
		    cout << "Deletion of " << chNode << " succeeded\n";
	    }
	}

	else if (strncmp(sbCommand, "parent", cCom) == 0) {
	    if (sscanf(pch, "%c", &chNode) != 1) 
		cout << "Need a node argument\n";
	    else {
		if ((pTree = FindParentNode(pTreeRoot, chNode)) == NULL)
		    cout << "Parent of node " << chNode << " not found\n";
		else
		    cout << "Parent of node " << chNode << " is " << 
			pTree->chNode << "\n";
	    }
	}

	else if (strncmp(sbCommand, "exist", cCom) == 0) {
	    if (sscanf(pch, "%c", &chNode) != 1)
		cout << "Need a node argument\n";
	    else {
		if ((pTree = FindNode(pTreeRoot, chNode)) == NULL)
		    cout << "Node " << chNode << " does not exist in tree\n";
		else
		    cout << "Node " << chNode << " does exist in tree\n";
	    }
	}

	else if (strncmp(sbCommand, "show", cCom) == 0)
	    ShowTree(pTreeRoot, 0);

	else if (strncmp(sbCommand, "height", cCom) == 0)
	    cout << "The height of the tree is " <<
		   ComputeHeight(pTreeRoot) << "\n";

	else if (strncmp(sbCommand, "node", cCom) == 0)
	    cout << "The number of nodes in the tree is " <<
		CountNode(pTreeRoot) << "\n";

	else if (strncmp(sbCommand, "leaf", cCom) == 0)
	    cout << "The number of leaf nodes in the tree is " <<
		CountLeafNode(pTreeRoot) << "\n";
	else if (strncmp(sbCommand, "quit", cCom) == 0)
	    bDone = TRUE;

	else if (strncmp(sbCommand, "menu", cCom) == 0)
	    PrintMenu();

	else {
	    cout << "Unknown command: " << sbCommand << "\n";
	    PrintMenu();
	}
	if (!bDone)
	    cout << PROMPT;
    }
}

/*
 * ReadTree -- read a tree stored in a file.  Return 0 on success and -1
 *      on failure.
 */
int ReadTree(TreeNodeType * &pTreeRoot, char *sbFile)
{
    
    ifstream treefile(sbFile);
    char chParent, chNode;

    if (!treefile) {
	return(-1);
    }
    while ((treefile >> chParent >> chNode) && !treefile.eof())
	if (AddNode(pTreeRoot, chParent, chNode) < 0)
	    return(-1);
    treefile.close();
    return(0);
}

/*
 * PrintMenu -- print more complete options
 */
void PrintMenu()
{
    cout << "Commands (shortened versions of command names can be used, \n";
    cout << "node values are characters):\n";
    cout << "addnode x y  -- add the node y with parent x to the tree\n";
    cout << "deletenode x -- delete the node x from the tree, x must be a leaf node\n";
    cout << "exist x -- indicate whether the node currently exists in the tree\n";
    cout << "height -- show the height of the tree\n";
    cout << "leaf -- show the number of leaf nodes in the tree\n";
    cout << "menu -- print this menu\n";
    cout << "node -- show the number of nodes in the tree\n";
    cout << "parent x -- show the parent of the node x\n";
    cout << "quit -- quit the program\n";
    cout << "show -- show the contents of the entire tree\n";
}

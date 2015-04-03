/////////////////////////////////////////////////////////////////////
// File: blmgr_list.h
// Definition and implementation of the block manger using
//  linked lists
// Author: Peter Golaszewski
// Updated: 5/1/99
/////////////////////////////////////////////////////////////////////

#include <fstream.h>

#define MAP_WIDTH 10

// Define list's node structure
struct Node {
	int s_block_number; // number of the first block in range
	int e_block_number; // number of the last block in range
	Node *next_node;
};

/////////////////////////////////////////////////////////////////////
// Class definition:    

class BlMgr_List {
	private:
		int blocks_num;   // maximum number of blocks		
		int run_silent;   // should the manager print out anything
		int cont_mode;    // should the manager run in cont. mode
		int free_blocks;   // number of free nodes at the moment
		int overspan;
		Node *blocks;     // linked list of range nodes
	public:
		BlMgr_List (int,int,int);    // constructor
		~BlMgr_List ();              // destructor
		int Allocate   (int, int*);        // blocks allocator
		int Deallocate (int);        // block delocator
		int Clear ();                // deallocate all blocks
		int Print ();                // prints free block numbers
		int PrintMap ();             // prints out blocks map 
		int Report();                // returns the name of the used method
		int GetOverSpan ();          // returns the overspan of all allocations
		int GetFreeBlocks();         // returns the number of free blocks
		int WriteBlock(int, char *, int); // writes given data to given block number
		int ReadBlock(int, char *, int);  // reads data from given block number to given data buffer
		char *getFileName (int);          // assiciates filename with given block number
		int fillBlockArray (int, int, int, int *); // used to allocate blocks
};

// Class implementation:

/////////////////////////////////////////////////////////////////////
// BlMgr_List (int a_num, int a_silent, int a_cont)
// constructor. Initializes the list size to a_num, as well as it finds
// out if to run in the continguous or silent mode
BlMgr_List::BlMgr_List (int a_num, int a_silent, int a_cont) {
	// Initialize members
	blocks_num = a_num;
	free_blocks = blocks_num;
	run_silent = a_silent;
	cont_mode  = a_cont;
	overspan = 0;

	// create the linked list
	blocks = new Node;
	blocks -> s_block_number = 1;
	blocks -> e_block_number = blocks_num;
	blocks -> next_node = NULL;
}

/////////////////////////////////////////////////////////////////////
// ~BlMgr_List () - destrucotr. Deallocates the linked list
BlMgr_List::~BlMgr_List () {
	run_silent = 1; // This is so that user doesn't have to see Clear()'s output
	Clear();
	delete (blocks);
}

/////////////////////////////////////////////////////////////////////
// Allocate (int a_num) - allocate a_num blocks. Return 0 on success
// else return 1. Display the comment if not runnign silent 
int BlMgr_List::Allocate (int a_num, int *blocknumbers) {
	int blockcoutner = 0;
	// first check, if there are even enough free blocks:
	if (free_blocks < a_num) {
		if (!run_silent) cout << "\nError: Could not allocate this many blocks";
		return 1;
	}
	if (a_num <= 0) { 
		if (!run_silent) cout << "\nError: Bad number of blocks";
		return 1;
	}

	// define help pointers
	Node *pNode;
	Node *pNode2;  
	Node *pNode3;  
	Node *pNode4;  	
	
	int size; // hold number of blocks allocated so far

	if (!run_silent) cout << "\nAllocated " << a_num << " blocks: ";	
	
	// first we need to check if we are running in the cont. mode:
	if (cont_mode) {
		// Cont mode present .. allocate only blocks in whole chunks

		// look for the smallest block range that will be big enough
		// and put the pointer to it in pNode2;
		pNode2    = NULL;
		pNode3    = NULL;  // ptr to the node right before the chosen one
		pNode4    = NULL;  // ptr to the node right before the last node
		size = blocks_num; // size keeps track of smallest available range
		pNode = blocks;
		while (pNode != NULL) {
			if (pNode->e_block_number - pNode->s_block_number + 1 >= a_num) {
				// found a block range that will fit
				if (size >= pNode->e_block_number - pNode->s_block_number + 1) {
					pNode2 = pNode;
					pNode3 = pNode4;
					size = pNode->e_block_number - pNode->s_block_number + 1;
				}
			}
			pNode4 = pNode;
			pNode = pNode->next_node;
		}	
		if (pNode2 == NULL) { 
			if (!run_silent) cout << "\nError: Could not allocate this many blocks";
			return 1; // failed to find enough space (need a whole chunk)
		}
		if (size == a_num) {
			// the block range is perfect size, simply delete the node:
			if (pNode3==NULL) {
				// we have the first node in the list
				if (!run_silent) cout << blocks->s_block_number << "-" << blocks->e_block_number << " ";
				blockcoutner = fillBlockArray (blockcoutner, blocks->s_block_number, blocks->e_block_number, blocknumbers);
				pNode3 = blocks->next_node;
				delete (blocks);
				blocks = pNode3;
			} else {
				// we have a node inside the list
				if (!run_silent) cout << pNode2->s_block_number << "-" << pNode2->e_block_number << " ";
				blockcoutner = fillBlockArray (blockcoutner, pNode2->s_block_number, pNode2->e_block_number, blocknumbers);
				pNode4 = pNode2->next_node;
				delete (pNode2);
				pNode3->next_node = pNode4;
			}
		} else {
			// the block range is larger then we need, so we simply will take the beggining
			// blocks of it.
			if (!run_silent) cout << pNode2->s_block_number << "-" << pNode2->s_block_number + a_num - 1 << " ";
			blockcoutner = fillBlockArray (blockcoutner, pNode2->s_block_number, pNode2->s_block_number + a_num - 1, blocknumbers);
			pNode2->s_block_number += a_num;
		}
		free_blocks -= a_num;
		return 0;
	} else {
		// No cont mode. What we need to do is to allocate any free blocks in the head of the list.
		// For non-cont method we will need to keep track of over-spanning of allocations
		int span_start=0; // used to track the allocation span
		int span_end  =0;
		size  = 0;     // blocks allocated so far		
		int range_size;
		while ((size != a_num)  &&  (blocks != NULL)) {
			// a_num - size is the number of blocks we still need
			range_size = blocks->e_block_number - blocks->s_block_number + 1;
			if (range_size <= a_num - size) {				
				// we will need to use up this whole block range, delete it
				// we have the first node in the list
				if (span_start == 0) span_start = blocks->s_block_number;
				span_end = blocks->e_block_number;
												
				if (!run_silent) cout << blocks->s_block_number << "-" << blocks->e_block_number << " ";
				blockcoutner = fillBlockArray (blockcoutner, blocks->s_block_number, blocks->e_block_number , blocknumbers);
				pNode3 = blocks->next_node;
				delete (blocks);
				blocks = pNode3;
				size += range_size;
			} else {
				// we will need to use only part of this block range
				if (span_start == 0) span_start = blocks->s_block_number;
				span_end = blocks->s_block_number + a_num - size - 1;
				
				if (!run_silent) cout << blocks->s_block_number << "-" << blocks->s_block_number + a_num - size - 1 << " ";
				blockcoutner = fillBlockArray (blockcoutner, blocks->s_block_number, blocks->s_block_number + a_num - size - 1, blocknumbers);
				blocks->s_block_number += a_num - size;
				size = a_num;
			}
		}	
		overspan += span_end - span_start + 1 - a_num; // how many blocks did we have to skip over without allocating
		free_blocks -= a_num;
		return 0;
	}
}

/////////////////////////////////////////////////////////////////////
// Deallocate (int a_num) - deallocate block number a_num. Return 0 on
// success else return 1. Display the comment if not runnign silent 
int BlMgr_List::Deallocate (int a_num) {
	// define help pointers
	Node *pNode = blocks;
	Node *pNode2;

	if ((a_num <= 0) || (a_num > blocks_num)) { 
		if (!run_silent) cout << "\nError: Bad block number";
		return 1;
	}

	// see if the disk is full:
	if (blocks == NULL)  {
		// yes .. simply free up this one block
		blocks = new Node;
		blocks -> s_block_number = a_num;
		blocks -> e_block_number = a_num;
		blocks -> next_node = NULL;
		free_blocks = 1;
		if (!run_silent) cout << "\nBlock " << a_num << " deallocated";	
		return 0;
	}

	// find out if the block is free already
	// if not, remember node pointer there it should be put
	pNode2 = NULL;
	while (pNode != NULL) {
		if (pNode->e_block_number >= a_num && pNode->s_block_number <= a_num) {
			// the block is free already
			if (!run_silent) cout << "\nError: Block " << a_num << " already free";
			return 1;
		}
		if (pNode->s_block_number < a_num) 
			// the block to be deallocated might fit after
                        // this node:
			pNode2 = pNode;
		pNode = pNode->next_node;
	}
	if (pNode2 == NULL) {
		// the block fits before any unused regions. 
		// Two possibilities are here
		// 1. The block can be attached to the next free blocks range
		//    : extend the range node
		// 2. The block is at least one block away from the first free blocks region
		//    : create new node for the freed block
		
		if (blocks->s_block_number - 1 != a_num) {
			// create new node
			pNode2 = new Node;
			pNode2 -> s_block_number = a_num;
			pNode2 -> e_block_number = a_num;
			pNode2 -> next_node = blocks;
			blocks = pNode2;
		} else {
			// extend the next node
			blocks -> s_block_number--; 
		}

		free_blocks++;
		if (!run_silent) cout << "\nBlock " << a_num << " deallocated";	
		return 0;
	} 

	// There are couple of possible scenerios now:
	//  1. freed block cannot be attached to any node
	//      : create a new node
	//  2. freed block can be attached only to the node before it
	//      : extend the node range
	//  3. freed block can be attached only to the node after it
	//      : extend the node range
	//  4. freed block can be attached to both nodes
	//      : merge the two nodes together

	int to_before = 0;
	int to_after  = 0;

	// Check where the freed block would belong
	if (pNode2->e_block_number + 1 == a_num) to_before = 1;
	if (pNode2->next_node != NULL) {
		if (pNode2->next_node->s_block_number - 1 == a_num) to_after = 1;
	} else to_after = 0;

	// depending on where the block belongs, attach it, merge or
	// create new node:

	if (to_before == 0 && to_after == 0) {
		// create new node:
		pNode = pNode2->next_node;
		pNode2 -> next_node = new Node;
		pNode2 = pNode2 -> next_node;
		// pNode2 is now pointing to the new node
		pNode2->s_block_number = a_num;
		pNode2->e_block_number = a_num;
		pNode2->next_node = pNode;
	} else 
	if (to_before == 1 && to_after == 0) {
		// attach the block to the node before it
		pNode2->e_block_number++;
	} else  
	if (to_before == 0 && to_after == 1) {
		// attach the block to the node after it
		pNode2->next_node->s_block_number--;
	} else {
		// merge two nodes
		pNode2->e_block_number = pNode2->next_node->e_block_number;
		pNode = pNode2->next_node;
		pNode2->next_node = pNode->next_node;
		delete (pNode);
	} 

	// record that we have one more free block
	free_blocks++;
	
	if (!run_silent) cout << "\nBlock " << a_num << " deallocated";	
	
	return 0;
}

/////////////////////////////////////////////////////////////////////
// Clear () - deallocate all blocks and create new list. 
// Display the comment if not runnign silent 
int BlMgr_List::Clear () {
	// define help pointers
	Node *pNode = blocks;
	Node *pNode2;

	// deallocate all memory used by the list
	while (pNode != NULL) {
		pNode2 = pNode->next_node;
		delete(pNode);
		pNode = pNode2;	
	}

	// create new linked list
	blocks = new Node;
	blocks -> s_block_number = 1;
	blocks -> e_block_number = blocks_num;
	blocks -> next_node = NULL;

	free_blocks = blocks_num;
		
	if (!run_silent) cout << "\nAll blocks deallocated";	
	
	return 0;
}

/////////////////////////////////////////////////////////////////////
// Print () - print list of free block numbers if not runnign silent 
// return 0 in any case
int BlMgr_List::Print () {
   if (!run_silent) {
	cout << "\nFree blocks: ";
	// define help pointer
	Node *pNode = blocks;
	
	int biggest_allocation = 0; // what's the largest allocation one can make at this moment

	// deallocate all memory used by the list
	while (pNode != NULL) {
		if (biggest_allocation < pNode->e_block_number - pNode->s_block_number + 1) 
		   biggest_allocation = pNode->e_block_number - pNode->s_block_number + 1;
		if (pNode->s_block_number != pNode->e_block_number) 
		   cout << pNode->s_block_number << "-" << pNode->e_block_number << " ";
		else 
		   cout << pNode->s_block_number << " ";
		pNode = pNode->next_node;	
	}
	cout << "\n  Total free blocks: " << free_blocks;
	cout << "\n  Total used blocks: " << blocks_num - free_blocks;
	cout << "\n  Total blocks     : " << blocks_num;
	if (!cont_mode) biggest_allocation = free_blocks;
	cout << "\n  Max allocation   : " << biggest_allocation;
   }
   return 0;
}

/////////////////////////////////////////////////////////////////////
// PrintMap () - print map of the free blocks
// return 0 in any case
int BlMgr_List::PrintMap () {
	cout << "\nBlocks map: (each row has " << MAP_WIDTH << " blocks)\n";
	// define help pointer
	Node *pNode = blocks;

	// check if the disk is full
	if (blocks == NULL) {
		// yes, no more free blocks
		cout << "  All blocks are allocated!";
		return 0;
	}

	// show the block map
	int last_block = 0;	
	int biggest_allocation = 0; // what's the largest allocation one can make at this moment
	int count;
	int number = 1;  // seq number of the printed block
	while (pNode != NULL) {
		if (biggest_allocation < pNode->e_block_number - pNode->s_block_number + 1) 
		   biggest_allocation = pNode->e_block_number - pNode->s_block_number + 1;
		if ((last_block == 0) && (pNode->s_block_number > 1)) 
			for (count=1; count <= pNode->s_block_number -1; count++) {
				if (number % MAP_WIDTH == 1) cout << "\n";
				cout << '#';				
				number++;
			}
	
		if (last_block > 0) {
			// mark used blocks with '#'
			for (count=0; count< pNode->s_block_number - last_block - 1; count++) {
				if (number % MAP_WIDTH == 1) cout << "\n";
				cout << '#';
				number++;
			}
		}
		last_block=pNode->e_block_number;

		// mark unused blocks with '.'
		for (count=0; count< pNode->e_block_number - pNode->s_block_number + 1; count++) {
			if (number % MAP_WIDTH == 1) cout << "\n";
			cout << '.';			
			number++;
		}
		pNode = pNode->next_node;	
	}
	if (last_block < blocks_num) 
		for (count=0; count< blocks_num - last_block; count++) {
			if (number % MAP_WIDTH == 1) cout << "\n";			
			cout << '#';			
			number++;
		}
		
	cout << "\n  Total free blocks: " << free_blocks;
	cout << "\n  Total used blocks: " << blocks_num - free_blocks;
	cout << "\n  Total blocks     : " << blocks_num;
	if (!cont_mode) biggest_allocation = free_blocks;
	cout << "\n  Max allocation   : " << biggest_allocation;
   return 0;
}

/////////////////////////////////////////////////////////////////////
// Report () - returns 0 and prints the method name
int BlMgr_List::Report () { 
	if (!run_silent) cout << "\nUsing Linked-List method\n";
	return 0; 
}

/////////////////////////////////////////////////////////////////////
// GetOverSpan () - returns total overspan of all allocations since the start of the manager
int BlMgr_List::GetOverSpan () { return overspan; }


/////////////////////////////////////////////////////////////////////
// GetFreeBlocks() - returns the number of free blocks
int BlMgr_List::GetFreeBlocks() {
	return free_blocks;
}
		
/////////////////////////////////////////////////////////////////////
// WriteBlock(int, char *, int) - writes given data to given block number
int BlMgr_List::WriteBlock(int block, char *data, int size) {
	ofstream F (getFileName(block));	
	F.write(data, size);
	F.close();
	return 0;
} 

/////////////////////////////////////////////////////////////////////
// ReadBlock(int, char *, int) - reads data from given block number to given data buffer
int BlMgr_List::ReadBlock(int block, char *data, int size) {
	ifstream F (getFileName(block));	
	F.read(data, size);
	F.close();
	return 0;
} 

/////////////////////////////////////////////////////////////////////
// getFileName(int) - returns the filename that should be given to the block number
char * BlMgr_List::getFileName (int block) {
	int b = block;
	char s1[50] = "block_", s2[2] = " ", c, *filename;
	while (b > 0) {
		c = b % 10 + '0';
		s2[0] = c;
		b = b / 10;
		strcpy(s1,strcat (s1,s2));
	}
	strcpy(s1,strcat (s1,".dat"));
	filename = new char(strlen(s1) + 2);
	strcpy (filename, s1);
	return filename;
}

/////////////////////////////////////////////////////////////////////
// fillBlockArray () - appends block numbers to given array. Block numbers start with 
// start and end with end. Function returns new current position in the array
int BlMgr_List::fillBlockArray (int current_pos, int start, int end, int *array) {
	int howmany = end - start + 1;
	int value = start;
	int new_position;
	for (new_position = current_pos; new_position < current_pos + howmany; new_position++) array[new_position] = value++;
	return new_position;
}

// End of the file
/////////////////////////////////////////////////////////////////////


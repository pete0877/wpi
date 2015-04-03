/////////////////////////////////////////////////////////////////////
// File: blmgr_map.h
// Definition and implementation of the block manger using
//  bit map
// Author: Peter Golaszewski
// Updated: 5/1/99
/////////////////////////////////////////////////////////////////////

#include <fstream.h>

#define MAP_WIDTH 10

/////////////////////////////////////////////////////////////////////
// Class definition:    

class BlMgr_Map {
	private:
		int blocks_num;   // maximum number of blocks		
		int map_size;     // maximum number of blocks
		int run_silent;   // should the manager print out anything
		int cont_mode;    // should the manager run in cont. mode
		int free_blocks;  // number of free nodes at the moment
		int overspan;
		char *blocks;
		int IsBlockUsed (int);         // checks if the block is used
		int SetBlockStatus (int,int);  // marks the block as used or free		
	public:
		BlMgr_Map (int,int,int);     // constructor
		~BlMgr_Map ();               // destructor
		int Allocate   (int, int *);        // blocks allocator
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
};

// Class implementation:

/////////////////////////////////////////////////////////////////////
// BlMgr_Map (int a_num, int a_silent, int a_cont)
// constructor. Initializes the list size to a_num, as well as it finds
// out if to run in the continguous or silent mode
BlMgr_Map::BlMgr_Map (int a_num, int a_silent, int a_cont) {
	// Initialize members
	blocks_num = a_num;
	free_blocks = blocks_num;	
	cont_mode  = a_cont;
	overspan = 0;

	// create the bit map
	map_size = a_num / 8;
	if (map_size * 8 != a_num) map_size++;
	blocks = new char [map_size];

	run_silent = 1;
	Clear();
	run_silent = a_silent;	
}

/////////////////////////////////////////////////////////////////////
// ~BlMgr_Map () - destrucotr. Deallocates the linked list
BlMgr_Map::~BlMgr_Map () {
	run_silent = 1; // This is so that user doesn't have to see Clear()'s output
	Clear();
	delete (blocks);
}

/////////////////////////////////////////////////////////////////////
// Allocate (int a_num) - allocate a_num blocks. Return 0 on success
// else return 1. Display the comment if not runnign silent 
int BlMgr_Map::Allocate (int a_num, int *blocknumbers) {
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
	int count;
	// first we need to check if we are running in the cont. mode:
	if (cont_mode) {
		// we are in cont mode .. first we need to scan the blocks, to see
		// if there is space		
		if (free_blocks == blocks_num) {
			// all blocks are free
			if (!run_silent) cout << "\nAllocated " << a_num << " blocks: ";	
			for (count = 1; count <= a_num; count ++) {
				SetBlockStatus (count, 1);
				blocknumbers[blockcoutner++] = count;				
				if (!run_silent) cout << count << " ";	
				free_blocks--;
			}	
			return 0;
		}
		
		int smallest = blocks_num; // the size of the smallest block that will fit our block
		int span=0; // free blocks span size
		int position=0; // position of the first block of the chosen range
		for (count = 1; count <= blocks_num; count++) {
			if (IsBlockUsed(count)) {				
				if ((span < smallest) && (span >= a_num)) {
					smallest = span;
					position = count - span;
				}
				span = 0;			
			} else {				
				span++;
			}
		}		
		if (span!=0) {
			// last blocks on the disk were free .. let's check that range
			if ((span < smallest) && (span >= a_num)) {
				smallest = span;
				position = blocks_num + 1 - span;
			}			
		}
		if (position > 0) {
			// we did find a range where we can allocate
			if (!run_silent) cout << "\nAllocated " << a_num << " blocks: ";	
			for (count = position; count <= position + a_num -1; count ++) {
				SetBlockStatus (count, 1);
				if (!run_silent) cout << count << " ";	
				blocknumbers[blockcoutner++] = count;
				free_blocks--;
			}					
			return 0;
		} else {
			if (!run_silent) cout << "\nError: Could not allocate this many blocks";
			return 1; // we didn't find enough space
		}
	} else {
		// not a cont mode .. we can pick any blocks
		int so_far; // blocks allocated so far
		if (!run_silent) cout << "\nAllocated " << a_num << " blocks: ";	
		count = 0;
		int span_start=0; // used to track the allocation span
		int span_end  =0;		
		for (so_far = 0; so_far < a_num;) {
			count++;
			if (!IsBlockUsed(count)) {
				if (span_start==0) span_start = count;
				span_end = count;
				SetBlockStatus (count, 1);
				so_far++;
				free_blocks--;
				if (!run_silent) cout << count << " ";	
				blocknumbers[blockcoutner++] = count;
			}
		}
		overspan += span_end - span_start + 1 - a_num;
		return 0;
	}
}

/////////////////////////////////////////////////////////////////////
// Deallocate (int a_num) - deallocate block number a_num. Return 0 on
// success else return 1. Display the comment if not runnign silent 
int BlMgr_Map::Deallocate (int a_num) {
	if ((a_num <= 0) || (a_num > blocks_num)) { 
		if (!run_silent) cout << "\nError: Bad block number";
		return 1;
	}
	if (IsBlockUsed(a_num)) {
		SetBlockStatus (a_num, 0);	
		free_blocks++;
		if (!run_silent) cout << "\nBlock " << a_num << " deallocated";	
	} else {
		if (!run_silent) cout << "\nError: Block " << a_num << " already free";
		return 1; // the block is free already
	}
	return 0;
}

/////////////////////////////////////////////////////////////////////
// Clear () - deallocate all blocks and create new list. 
// Display the comment if not runnign silent 
int BlMgr_Map::Clear () {
	free_blocks = blocks_num;
	for (int count = 0; count < map_size; count++) blocks[count] = 0;	
	if (!run_silent) cout << "\nAll blocks deallocated";		
	return 0;
}

/////////////////////////////////////////////////////////////////////
// Print () - print list of free block numbers if not runnign silent 
// return 0 in any case
int BlMgr_Map::Print () {
   if (!run_silent) {
	int biggest_allocation = 0; // what's the largest allocation one can make at this moment   
	int span = 0;
	cout << "\nFree blocks: ";
	int last_free = 0;  // number of the last free block or 0 if the last block was used.
	for (int count = 1; count <= blocks_num; count++) {
		if (IsBlockUsed(count)) {
			if (span > biggest_allocation) biggest_allocation = span;
			span = 0;
			if (last_free != 0) {
				// first used block in the range
				cout << last_free << "-" << count - 1 << " ";
				last_free = 0;				
			}
		} else {	
			span++;
			if (last_free == 0) {
				// first free block in the range
				last_free = count;					
			}
		}
	}	
	if (last_free != 0) cout << last_free << "-" << blocks_num << " ";
	if (span > biggest_allocation) biggest_allocation = span;
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
int BlMgr_Map::PrintMap () {
	int biggest_allocation = 0; // what's the largest allocation one can make at this moment   
	int span = 0;
	cout << "\nBlocks map: (each row has " << MAP_WIDTH << " blocks)\n";
	for (int count = 1; count <= blocks_num; count++) {
		if (count % MAP_WIDTH == 1) cout << "\n";
		if (IsBlockUsed(count)) {
			cout << "#";
			if (span > biggest_allocation) biggest_allocation = span;
			span = 0;			
		} else {
			cout << ".";
			span++;
		}
	}
	if (span > biggest_allocation) biggest_allocation = span;
	cout << "\n  Total free blocks: " << free_blocks;
	cout << "\n  Total used blocks: " << blocks_num - free_blocks;
	cout << "\n  Total blocks     : " << blocks_num;
	if (!cont_mode) biggest_allocation = free_blocks;
	cout << "\n  Max allocation   : " << biggest_allocation;
   return 0;
}

/////////////////////////////////////////////////////////////////////
// Report () - returns 0 and prints the method name
int BlMgr_Map::Report () { 
	if (!run_silent) cout << "\nUsing Bit-Map method\n"; 
	return 0; 
}

/////////////////////////////////////////////////////////////////////
// GetOverSpan () - returns total overspan of all allocations since the start of the manager
int BlMgr_Map::GetOverSpan () { return overspan; }

/////////////////////////////////////////////////////////////////////
// IsBlockUsed (int a_num) - returns 1 if block is used, else reutrns 0
int BlMgr_Map::IsBlockUsed (int a_num) {
	int byte_position = (a_num - 1) / 8;										  
	int bit_position = (a_num - 1) % 8;	
	int mask = 1 << (7 - bit_position);  
	return (mask & blocks[byte_position]);
}

/////////////////////////////////////////////////////////////////////
// SetBlockStatus (int a_num, int a_status) - returns 0. If (a_status) then marks the block as used. Else as unused.
int BlMgr_Map::SetBlockStatus (int a_num, int a_status) {
	int byte_position = (a_num - 1) / 8;										  
	int bit_position = (a_num - 1) % 8;	
	int mask;
	if (a_status) {
		// need to set as used
		mask = 1 << (7 - bit_position);  
		blocks[byte_position] = mask | blocks[byte_position];
	} else  {
		// need to set as free
		mask = 1 << (7 - bit_position);  
		mask = 255 ^ mask;
		blocks[byte_position] = mask & blocks[byte_position];
	}	
	return 0;
}

/////////////////////////////////////////////////////////////////////
// GetFreeBlocks() - returns the number of free blocks
int BlMgr_Map::GetFreeBlocks() {
	return free_blocks;
}
		
/////////////////////////////////////////////////////////////////////
// WriteBlock(int, char *, int) - writes given data to given block number
int BlMgr_Map::WriteBlock(int block, char *data, int size) {	
	ofstream F (getFileName(block));	
	F.write(data, size);
	F.close();
	return 0;
} 

/////////////////////////////////////////////////////////////////////
// ReadBlock(int, char *, int) - reads data from given block number to given data buffer
int BlMgr_Map::ReadBlock(int block, char *data, int size) {	
	ifstream F (getFileName(block));	
	F.read(data, size);
	F.close();
	return 0;
} 

/////////////////////////////////////////////////////////////////////
// getFileName(int) - returns the filename that should be given to the block number
char * BlMgr_Map::getFileName (int block) {
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

// End of the file
/////////////////////////////////////////////////////////////////////


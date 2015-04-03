/////////////////////////////////////////////////////////////////////
// File: file_mgr.h
// Definition and implementation of the directory list
// Keeps track of all the files in the current directory
// Author: Peter Golaszewski
// Updated: 5/1/99
/////////////////////////////////////////////////////////////////////

#define MAX_INODES   1000   // also describes max number of files. Can be increased
#define BLOCKS        127   // number of blocks
#define BLOCK_SIZE    512   // fixed block size
#define MAX_DIR_NEST  500   // maximum directory nesting
#define DIR_BLOCKS      5   // number of direct nodes in the I-node structure
#define MAX_FILENAME  256

#include "blmgr_list.h"
#include "blmgr_map.h"    
#include <iostream.h>
#include <string.h>
#include <stdio.h> 
#include <strstream.h>
extern "C" {
	#include "pgnfs.h" 
}

/////////////////////////////////////////////////////////////////////
// Data structures

struct DirEntry {
	char  name[MAX_FILENAME];    // directory name
	int   inode;   // the directorys' Inode number
};

struct FileEntry {
	char  name[MAX_FILENAME];    // file name
	int   inode;   // the files' Inode number
};

struct Inode {
	char used;   				 // 1 - true, 0 - false
	char remote;				 // 1 - true, 0 - false
	char directory;				 // 1 - true, 0 - false
	int  dir_blocks[DIR_BLOCKS]; // directo block numbers
	int  size;					 // size of the file in blocks
	int  indir_block;			 // indirect block number
};

/////////////////////////////////////////////////////////////////////
// Class definition:    

class File_Mgr {
	private:		
		CLIENT *nfsserver;		
		int method;                // 0 - bitmap    /   1 - link list       
		int run_silent;            // should the manager print out anything
		int cont_mode;             // should the manager run in cont. mode		
		int use_output_stream;     // should the manager run use the output_stream stream?
		
		int folder_blocks[BLOCKS]; // list of blocks used by the current directory
		int idx_folder_blocks;     // index to next free cell in folder_blocks	
		
		DirEntry *current_path[MAX_DIR_NEST];    // array of shortcut pointers to all the direcotries above (& including) the current one 
		int idx_current_path;                    // index in current_path to the current direcory (points to the next empty cell)
		
		FileEntry  *current_dir[MAX_INODES];    // array of pointers to file shortcuts in the current directory.
		int idx_current_dir;   // index in the current_dir to the first free (NULL) cell		
		
		Inode inodes[MAX_INODES];  // array of I-nodes
		int CurrentINode;       // I-node number of the current directory

		BlMgr_Map  *mgr1;		
		BlMgr_List *mgr2;
		
		int *temp; 					// stores temporary block data
		int *allocated_blocks;      // stores allocated blocks
				
		int getDirectorySize ();     // returns number of bytes needed to store the directory
		int getNeededBlocks (int);   // returns number of blocks to store n bytes		
		int outputResult (strstream *output_stream,char *);   // sends string of characters into output
		int outputResult (strstream *output_stream,int);   // sends an integer into output
		int outputResult (strstream *output_stream,char);   // sends a character into output		
		
	public:
		File_Mgr(int a_method, int a_run_silent, int a_cont_mode, int a_use_output_stream, CLIENT *a_nfsserver); // constructor
		~File_Mgr();                   // destructor
		int ListContents(strstream *output_stream);            // displays simple list of files
		int ListContentsLong(strstream *output_stream);        // displays complex list of files
		int CreateFile(char *filename, int blocks, strstream *output_stream);    // creates new file
		int RemoteCreateFile(char *filename, int blocks, strstream *output_stream);    // creates new file		
		int DeleteFile(char *filename, strstream *output_stream);                // deletes existing file
		int ExtendFile(char *filename, int blocks, strstream *output_stream);    // appends more blocks to the file
		int CreateDirectory(char *dirname, strstream *output_stream);            // creates new directory
		int ChangeDirectory(char *dirname, strstream *output_stream);            // changes directory		
		int PrintMap (strstream *output_stream);             // prints out simple blocks map
};

// Class implementation:

/////////////////////////////////////////////////////////////////////
// File_Mgr () - constructor.
File_Mgr::File_Mgr (int a_method, int a_run_silent, int a_cont_mode, int a_use_output_stream, CLIENT *a_nfsserver) {
	// acquire parametners
	method = a_method;
	run_silent = a_run_silent;
	cont_mode = a_cont_mode;
	use_output_stream = a_use_output_stream;
		
	int a;
	
	// Initialize all the I-nodes to not used
	for (a=0 ; a<MAX_INODES; a++) inodes[a].used = 0;
	
	// Initialize the path to NULL	
	for (a=0 ; a<MAX_DIR_NEST; a++) current_path[a] = NULL;

	// Initialize all the I-nodes to NULL	
	for (a=0 ; a<MAX_INODES; a++)   current_dir[a] = NULL;

	// Initialize the block manager and allocate one block for the root
	if (method) {
		mgr1 = new BlMgr_Map(BLOCKS,  1, cont_mode);
		mgr1->Allocate(1, folder_blocks);
	} else {
		mgr2 = new BlMgr_List(BLOCKS, 1, cont_mode);
		mgr2->Allocate(1, folder_blocks);
	}
	idx_folder_blocks = 1;

	// create the root directory i-node
	inodes[0].used = 1;
	inodes[0].directory = 1;
	inodes[0].size = 1;
	inodes[0].dir_blocks[0] = folder_blocks[0];
		
	// for the root directory, fill in the '..' and '.' file links
	current_dir[0] = new FileEntry;
	current_dir[0]->inode = 0;
	strcpy(current_dir[0]->name,".");

	current_dir[1] = new FileEntry;
	current_dir[1]->inode = 0;
	strcpy(current_dir[1]->name,"..");

	idx_current_dir  = 2;

	current_path[0] = new DirEntry;
	current_path[0]->inode = 0;
	strcpy(current_path[0]->name, "");
	idx_current_path = 1;

	// set the current i-node to the root i-node
	CurrentINode = 0;
	
	temp = new int[BLOCK_SIZE / sizeof(int)];		
	allocated_blocks = new int[BLOCKS];

	nfsserver = a_nfsserver;	
}

/////////////////////////////////////////////////////////////////////
// ~File_Mgr () - destrucotr. 
File_Mgr::~File_Mgr() {	
	int a;
	
	if (method) delete mgr1; else  delete mgr2;

	for (a=0 ; a<MAX_DIR_NEST; a++) 
		if (current_path[a] != NULL) delete current_path[a];

	for (a=0 ; a<idx_current_dir; a++) 
		if (current_dir[a] != NULL) delete current_dir[a];
	
	delete (temp);
	
	delete (allocated_blocks);
}

/////////////////////////////////////////////////////////////////////
// ListContents () - displays simple list of files
int File_Mgr::ListContents(strstream *output_stream) {
	if (!run_silent ) {
		int a;
		outputResult(output_stream, "\nPATH: ");
		for (a = 0; a<idx_current_path; a++) {
			outputResult(output_stream, current_path[a]->name);
			outputResult(output_stream, "/");
		}
		for (a = 0; a<idx_current_dir; a++) {
			outputResult(output_stream, "\n   ");
			outputResult(output_stream, current_dir[a]->name);		
		}
	}	
	return 0;
}

/////////////////////////////////////////////////////////////////////
// ListContentsLong ( ) -  displays complex list of files
int File_Mgr::ListContentsLong(strstream *output_stream){
	if (!run_silent ) {
		int a;
		outputResult(output_stream, "\nPATH: ");
		for (a = 0; a<idx_current_path; a++) {
			outputResult(output_stream, current_path[a]->name);
			outputResult(output_stream, "/");
		}
		for (a = 0; a<idx_current_dir; a++) {			
			if (inodes[current_dir[a]->inode].directory) {
				outputResult(output_stream, "\n   [");
				outputResult(output_stream, current_dir[a]->name);
				outputResult(output_stream, "]     ");	 	
			} else {
				outputResult(output_stream, "\n    ");
				outputResult(output_stream, current_dir[a]->name);
				outputResult(output_stream, "      ");		
				outputResult(output_stream, inodes[current_dir[a]->inode].size);
				outputResult(output_stream, " block(s)");
				if (inodes[current_dir[a]->inode].remote) { outputResult(output_stream, "     (remote)"); }
			}
		}
		outputResult(output_stream, "\n Free blocks: ");
		if (method) outputResult(output_stream, mgr1->GetFreeBlocks());
			else    outputResult(output_stream, mgr2->GetFreeBlocks());	
	}	
	return 0;
}

/////////////////////////////////////////////////////////////////////
// CreateFile () -  creates new file. 0 - on success, 1 on error (not enough free blocks)
int File_Mgr::CreateFile(char *filename, int blocks, strstream *output_stream){
	int blocksleft;
	int needed_blocks;	
	int need_expansion = 0;	

	// Argument check 
	if (blocks < 0) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Bad number of blocks");
		return 1;
	}

	// See if i-nodes can hold file that big
	if (blocks > (DIR_BLOCKS + (BLOCK_SIZE / sizeof(int)) )) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Cannot create files that big");
		return 1;
	}	

	if (blocks > DIR_BLOCKS) 
		needed_blocks = blocks + 1;
	else 
		needed_blocks = blocks;

	// See if the directory needs to expand by one block as the result of adding the file:
	// later we need to make sure we assign the extra block to the directory 	
	if (getNeededBlocks(getDirectorySize() + strlen(filename) + 3 * sizeof(int)) > idx_folder_blocks) {
		// we will need to expand the directory by one block
		needed_blocks++;
		need_expansion = 1;
	}

	// See if there are enough blocks for the file
	if (method) blocksleft = mgr1->GetFreeBlocks();	else blocksleft = mgr2->GetFreeBlocks();
	if (blocksleft < needed_blocks) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Not enough free blocks");
		return 1;
	}

	// See if the filename already exists
	int a;
	for (a = 0; a<idx_current_dir; a++) 		
		if (strcmp(current_dir[a]->name, filename) == 0) {
			if (!run_silent) outputResult(output_stream, "\nERROR: File name already exists in the current directory");
			return 1;
		}	

	// Find a free i-node
	int innumber = 0;
	a = 0;
	while ((innumber == 0) && (a < MAX_INODES - 1)) {
		a++;
		if (inodes[a].used==0) innumber = a;
	}

	// check if a free i-node was found
	if (innumber == 0) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Maximum number of files reached");
		return 1;
	}	

	// Try to allocate the blocks for the file
	int error;
	if (method) 
		error = mgr1->Allocate(needed_blocks, allocated_blocks);
	else 
		error = mgr2->Allocate(needed_blocks, allocated_blocks);
	if (error) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Not enough free blocks");		
		return 1;
	}
	
	// print out block number that were allocated:
	if (!run_silent) {
		outputResult(output_stream, "\nAllocated ");
		outputResult(output_stream, needed_blocks - need_expansion);
		outputResult(output_stream, " blocks: ");
		for (a = 0; a < needed_blocks - need_expansion; a++) {
			outputResult(output_stream, allocated_blocks[a]); 
			outputResult(output_stream, " ");					
		}
	}

	// fill in the information in the i-node
	inodes[innumber].used = 1;
	inodes[innumber].remote = 0;
	inodes[innumber].directory = 0;
	inodes[innumber].size = blocks;
	if (blocks>DIR_BLOCKS) {
		// we have more blocks then DIR_BLOCKS, so we will need to use indirect block
		// fill in the direct block numbers first:
		for (a=0; a<DIR_BLOCKS; a++) inodes[innumber].dir_blocks[a] = allocated_blocks[a];
		// set the indirect block number:
		inodes[innumber].indir_block = allocated_blocks[DIR_BLOCKS];
		// we need to serialize the rest of the block numbers into an character array and write them
		// to the disk:		
		for (a=0; a < needed_blocks - DIR_BLOCKS - 1 - need_expansion; a++) temp[a] = allocated_blocks[DIR_BLOCKS + a + 1];				
		if (method) 
			mgr1->WriteBlock(inodes[innumber].indir_block, (char *) temp, BLOCK_SIZE);
		else 
			mgr2->WriteBlock(inodes[innumber].indir_block, (char *) temp, BLOCK_SIZE);
	} else {
		// we have less then DIR_BLOCKS, so we will use at most all direct blocs:		
		for (a=0; a<blocks; a++) inodes[innumber].dir_blocks[a] = allocated_blocks[a];
	}
	
	// add the file to the current directory	
	current_dir[idx_current_dir] = new FileEntry;
	current_dir[idx_current_dir]->inode = innumber;
	strcpy(current_dir[idx_current_dir]->name,filename);
	idx_current_dir++;
	current_dir[idx_current_dir] = NULL;
	
	// expand the directory if it was nesessary:
	int expension_block;
	if (need_expansion) {
		expension_block = allocated_blocks[needed_blocks - 1];
		if (!run_silent) {
			outputResult(output_stream, "\nDirectory expanded by block number: ");
			outputResult(output_stream, allocated_blocks[needed_blocks - 1]);
		}
		folder_blocks[idx_folder_blocks++]=allocated_blocks[needed_blocks - 1];
		inodes[CurrentINode].size++;
		// See if the block needs to be added to the indirect blocks of the direct blocks:
		
		if (inodes[CurrentINode].size == DIR_BLOCKS + 1) {
			// yes, need to create the indirect block				
			if (method) 
				error = mgr1->Allocate(1, allocated_blocks);
			else 
				error = mgr2->Allocate(1, allocated_blocks);					
			if (error) {
				if (!run_silent) outputResult(output_stream, "\nERROR: Not enough free blocks");		
				return 1;
			}					
			inodes[CurrentINode].indir_block = allocated_blocks[0];							 	
		} else {
			// directory fits in the direct blocks
			inodes[CurrentINode].dir_blocks[inodes[CurrentINode].size-1] = expension_block;		
		}
	}	

	return 0;
}

/////////////////////////////////////////////////////////////////////
// RemoteCreateFile () -  creates new file on NFS server
int File_Mgr::RemoteCreateFile(char *filename, int blocks, strstream *output_stream){
	char text[200], tok[200];
	char *command, **result;

	// See if the filename already exists
	int a;
	for (a = 0; a<idx_current_dir; a++) 		
		if (strcmp(current_dir[a]->name, filename) == 0) {
			if (!run_silent) outputResult(output_stream, "\nERROR: File name already exists in the current directory");
			return 1;
		}	

	// Find a free i-node
	int innumber = 0;
	a = 0;
	while ((innumber == 0) && (a < MAX_INODES - 1)) {
		a++;
		if (inodes[a].used==0) innumber = a;
	}

	// check if a free i-node was found
	if (innumber == 0) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Maximum number of files reached");
		return 1;
	}	

	sprintf(text, "create %s %d", filename, blocks);
	
    command = text;
	if ((result = sendcommand_1(&command, nfsserver))==NULL) { clnt_perror(nfsserver,"[NFS server]"); exit(4); }
	strcpy(tok, *result);		
	if (strcmp(strtok(tok, " "), "\nERROR:")==0) {
		cout << "\nNFS ERROR: " << *result << "\n"; 
		return 0;
	}
		
	cout << "\nNFS: " << *result << "\n"; 

	// fill in the information in the i-node
	inodes[innumber].used = 1;
	inodes[innumber].remote = 1;
	inodes[innumber].directory = 0;
	inodes[innumber].size = blocks;
		
	// add the file to the current directory	
	current_dir[idx_current_dir] = new FileEntry;
	current_dir[idx_current_dir]->inode = innumber;
	strcpy(current_dir[idx_current_dir]->name,filename);
	idx_current_dir++;
	current_dir[idx_current_dir] = NULL;
	
	return 0;
}

/////////////////////////////////////////////////////////////////////
// DeleteFile () -   deletes existing file
int File_Mgr::DeleteFile(char *filename, strstream *output_stream){
	char text[200], tok[200];
	char *command, **result;

	// See if the filename exists
	int a;
	int idx_found = -1;
	for (a = 0; a<idx_current_dir; a++) 		
		if (strcmp(current_dir[a]->name, filename) == 0) idx_found = a;
	if (idx_found < 0) {
		if (!run_silent) outputResult(output_stream, "\nERROR: File not found");
		return 1;
	}

	// find the i-node for the filename, and make sure it's a file, not a
	// directory:
	int i = current_dir[idx_found]->inode;
	if (inodes[i].directory) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Cannot delete directories");
		return 1;
	}

	// delete the entry from the current directory listing
	delete (current_dir[idx_found]);
	current_dir[idx_found] = NULL;
	current_dir[idx_current_dir] = NULL;	
	idx_current_dir--;
	current_dir[idx_found] = current_dir[idx_current_dir];
	current_dir[idx_current_dir] = NULL;

	if (inodes[i].remote) {
		// this is a remote directory
		sprintf(text, "delete %s", filename);
		command = text;
		if ((result = sendcommand_1(&command, nfsserver))==NULL) { clnt_perror(nfsserver,"[NFS server]"); exit(4); }
		cout << "\nNFS: " << *result << "\n"; 	
		// free up the i-node	
		inodes[i].used = 0;
		return 0;
	}

	
	int error;

	// free up all the blocks used by the file:
	// check if the indirect block was used:
	if (!run_silent) outputResult(output_stream, "\nDeallocated blocks: ");
	if (inodes[i].size > DIR_BLOCKS) {
	
		// yes, we need to delete the inderect blocks as well				
		// first deallocate all direct blocks
		for (a=0; a < DIR_BLOCKS; a++) {			
			if (method) 
				error = mgr1->Deallocate(inodes[i].dir_blocks[a]);
			else 
				error = mgr2->Deallocate(inodes[i].dir_blocks[a]);
			if (error) {
				if (!run_silent) outputResult(output_stream, "\nERROR: System consistency problem");
				return 1;
			} else if (!run_silent) {
				outputResult(output_stream, inodes[i].dir_blocks[a]);
				outputResult(output_stream, " ");
			}
		}

		// now read in the indirect block:		
		if (method) 
			mgr1->ReadBlock(inodes[i].indir_block, (char *) temp, BLOCK_SIZE);
		else 
			mgr2->ReadBlock(inodes[i].indir_block, (char *) temp, BLOCK_SIZE);

		// deallocate the indicect block		
		if (method) 
			error = mgr1->Deallocate(inodes[i].indir_block);
		else 
			error = mgr2->Deallocate(inodes[i].indir_block);				
		if (error) {
			if (!run_silent) outputResult(output_stream, "\nERROR: System consistency problem");
			return 1;
		} else if (!run_silent) {
			outputResult(output_stream, inodes[i].indir_block);
			outputResult(output_stream, " ");
		}
				
		// deallocate all blocks from the indirect block
		for (a=0; a < inodes[i].size - DIR_BLOCKS; a++) {						
			if (method) 
				error = mgr1->Deallocate(temp[a]);
			else 
				error = mgr2->Deallocate(temp[a]);		
				
			if (error) {
				if (!run_silent) outputResult(output_stream, "\nERROR: System consistency problem");
				return 1;
			} else if (!run_silent) {
				outputResult(output_stream, temp[a]);
				outputResult(output_stream, " ");
			}
		}		
	} else {
		// no, we only need to delete the direct blocks
		for (a=0; a < inodes[i].size; a++) {			
			if (method) 
				error = mgr1->Deallocate(inodes[i].dir_blocks[a]);
			else 
				error = mgr2->Deallocate(inodes[i].dir_blocks[a]);
			if (error) {
				if (!run_silent) outputResult(output_stream, "\nERROR: System consistency problem");
				return 1;
			} else {
				if (!run_silent) {
					outputResult(output_stream, inodes[i].dir_blocks[a]);
					outputResult(output_stream, " ");
				}
			}
		}
	}

	// free up the i-node	
	inodes[i].used = 0;

	// Now we need to check, if we can shrink the directory 
	// by one block:
	if ((getNeededBlocks(getDirectorySize()) < idx_folder_blocks) && (idx_folder_blocks > 1)) {
		idx_folder_blocks--;
		inodes[CurrentINode].size--;	
		if (method) 
			error = mgr1->Deallocate(folder_blocks[idx_folder_blocks]);
		else 
			error = mgr2->Deallocate(folder_blocks[idx_folder_blocks]);
		if (error) {
			if (!run_silent) outputResult(output_stream, "\nERROR: System consistency problem");
			return 1;
		} else {			
			if (!run_silent) {
				outputResult(output_stream, "\nDirectory shrinked by one block: ");
				outputResult(output_stream, folder_blocks[idx_folder_blocks]);
			}
		}
	}

	return 0;
}

/////////////////////////////////////////////////////////////////////
// ExtendFile () -  appends more blocks to the file
int File_Mgr::ExtendFile(char *filename, int blocks, strstream *output_stream){
	char text[200], tok[200];
	char *command, **result;
	
	int blocksleft;	
	int required_blocks;	// blocks requested, plus maybe the indirect block
	
	// Argument check 
	if (blocks < 0) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Bad number of blocks");
		return 1;
	}

	// See if the filename exists
	int a;
	int innumber = -1;
	for (a = 0; a<idx_current_dir; a++) 		
		if (strcmp(current_dir[a]->name, filename) == 0) innumber = current_dir[a]->inode;

	// check if the i-node was found
	if (innumber < 0) {
		if (!run_silent) outputResult(output_stream, "\nERROR: File not found");
		return 1;
	}

	// make sure the it's a file:	
	if (inodes[innumber].directory) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Cannot extend directories");
		return 1;
	}


	if (inodes[innumber].remote) {
		// this is a remote directory
		sprintf(text, "extend %s %d", filename, blocks);
		command = text;
		if ((result = sendcommand_1(&command, nfsserver))==NULL) { clnt_perror(nfsserver,"[NFS server]"); exit(4); }

		strcpy(tok, *result);		
		if (strcmp(strtok(tok, " "), "\nERROR:")==0) {
			cout << "\nNFS ERROR: " << *result << "\n"; 
			return 0;
		}	
		cout << "\nNFS: " << *result << "\n"; 
		inodes[innumber].size = inodes[innumber].size + blocks;
		
		return 0;
	}

	// See if the i-node can hold file after expansion
	if (inodes[innumber].size + blocks > (DIR_BLOCKS + (BLOCK_SIZE / sizeof(int)) )) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Cannot create files that big");
		return 1;
	}	

	// See if there are enough blocks for the file	
	required_blocks = blocks;
	if (inodes[innumber].size + blocks > DIR_BLOCKS) 
		if (inodes[innumber].size <= DIR_BLOCKS) {
			// we will need to create and use the indirect block
			required_blocks++;
		}
	
	if (method) blocksleft = mgr1->GetFreeBlocks();	else blocksleft = mgr2->GetFreeBlocks();
	if (blocksleft < required_blocks) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Not enough free blocks");
		return 1;
	}

	// Try to allocate the blocks for the file
	int error;
	if (method) error = mgr1->Allocate(required_blocks, allocated_blocks);
	       else error = mgr2->Allocate(required_blocks, allocated_blocks);
	if (error) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Not enough free blocks");
		return 1;
	}
	
	// print out block numbers that were allocated:
	if (!run_silent) {
		outputResult(output_stream, "\nAllocated ");
		outputResult(output_stream, required_blocks);
		outputResult(output_stream, " blocks: ");
		for (a = 0; a < required_blocks; a++) {
			outputResult(output_stream, allocated_blocks[a]);
			outputResult(output_stream, " ");
		}
	}

	// update the information in the i-node:
	int appended = 0;

	// see if the i-node was already using the indirect block 
	if (inodes[innumber].size > DIR_BLOCKS) { 
		// yes, the indirect block was used
		// we neeed to read that block, and append more block numbers to 
		// it:		
		if (method) 
			mgr1->ReadBlock(inodes[innumber].indir_block, (char *) temp, BLOCK_SIZE);
		else 
			mgr2->ReadBlock(inodes[innumber].indir_block, (char *) temp, BLOCK_SIZE);

		int start = inodes[innumber].size - DIR_BLOCKS;
		// start now holds the index of the first empty cell

		while (appended < blocks) {
			temp[start + appended] = allocated_blocks[appended];
			appended++;
		}

		// now we can write the block back to the disk (file)
		if (method) 
			mgr1->WriteBlock(inodes[innumber].indir_block, (char *) temp, BLOCK_SIZE);
		else 
			mgr2->WriteBlock(inodes[innumber].indir_block, (char *) temp, BLOCK_SIZE);
	} else {
		// No, only direct block numbers were used

		// first let's try to fill out the rest of the direct block numbers
		a = inodes[innumber].size;
		while ((a < DIR_BLOCKS) && (appended < blocks)) {
			inodes[innumber].dir_blocks[a] = allocated_blocks[appended];
			appended++;
			a++;
		}
		// at this point we dont' have any more space in the direct
		// block numbers list. If we have more blocks to append, we will
		// need to create the indirect block for them:
		if (appended < blocks) {			
			// we need to create the indirect block
			inodes[innumber].indir_block = allocated_blocks[appended];
			appended++;
			
			a = 0;
			while (appended < required_blocks) {
				temp[a] = allocated_blocks[appended];				
				appended++;				
				a++;
			}
			
			if (method) 
				mgr1->WriteBlock(inodes[innumber].indir_block, (char *) temp, BLOCK_SIZE);
			else 
				mgr2->WriteBlock(inodes[innumber].indir_block, (char *) temp, BLOCK_SIZE);
		}
	} 
	
	inodes[innumber].size = inodes[innumber].size + blocks;

	return 0;
}

/////////////////////////////////////////////////////////////////////
// CreateDirectory () -   creates new directory
int File_Mgr::CreateDirectory(char *dirname, strstream *output_stream){
	int blocksleft;
	int needed_blocks = 1;		
	int need_expansion = 0;

	// See if we have a free block for the directory	
	if (method) blocksleft = mgr1->GetFreeBlocks();	else blocksleft = mgr2->GetFreeBlocks();

	// See if the directory needs to expand by one block as the result of adding the entry:
	if (getNeededBlocks(getDirectorySize() + strlen(dirname) + 3 * sizeof(int)) > idx_folder_blocks) {
			// we will need to expand the directory by one block
			needed_blocks++;
			need_expansion = 1;
			// later we need to make sure we assign the extra block to the directory 
			// [1] in the allocated_blocks array
	}

	if (blocksleft < needed_blocks) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Not enough free blocks");
		return 1;
	}

	// See if the filename already exists
	int a;
	for (a = 0; a<idx_current_dir; a++) 		
		if (strcmp(current_dir[a]->name, dirname) == 0) {
			if (!run_silent) outputResult(output_stream, "\nERROR: File name already exists in the current directory");
			return 1;
		}	

	// Find a free i-node
	int innumber = 0;
	a = 0;
	while ((innumber == 0) && (a < MAX_INODES - 1)) {
		a++;
		if (inodes[a].used==0) innumber = a;
	}

	// check if a free i-node was found
	if (innumber == 0) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Maximum number of files reached");
		return 1;
	}


	// Try to allocate the block for the file (and maybe extra one for the current directory)
	int error;
	if (method) 
		error = mgr1->Allocate(needed_blocks, allocated_blocks);
	else 
		error = mgr2->Allocate(needed_blocks, allocated_blocks);
	if (error) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Not enough free blocks");
		return 1;
	}

	// print out block number that were allocated:
	if (!run_silent) {
		outputResult(output_stream, "\nAllocated 1 block: ");
		outputResult(output_stream, allocated_blocks[0]);
	}

	// fill in the information in the i-node
	inodes[innumber].used = 1;
	inodes[innumber].directory = 1;
	inodes[innumber].size = 1;
	inodes[innumber].dir_blocks[0] = allocated_blocks[0];

	// fill in the directory information into the new block	
	
	int next_pos  = 0;
	int next_cell = 0;

	next_pos = next_pos + 2 + ((strlen(".") / sizeof(int)) + 1); 
	temp[next_cell]  = next_pos;
	temp[next_cell + 1]  = innumber;
	strcpy((char *) &temp[next_cell + 2], ".");
	next_cell = next_pos;

	next_pos = next_pos + 2 + ((strlen("..") / sizeof(int)) + 1); 
	temp[next_cell]  = next_pos;
	temp[next_cell + 1]  = CurrentINode;
	strcpy((char *) &temp[next_cell + 2], "..");
	next_cell = next_pos;

	temp[next_cell]  = 0;

	// write the block down		
	if (method) 
		mgr1->WriteBlock(allocated_blocks[0], (char *) temp, BLOCK_SIZE);
	else 
		mgr2->WriteBlock(allocated_blocks[0], (char *) temp, BLOCK_SIZE);	
		
	// add the file to the current directory
	current_dir[idx_current_dir] = new FileEntry;
	current_dir[idx_current_dir]->inode = innumber;
	strcpy(current_dir[idx_current_dir]->name,dirname );
	idx_current_dir++;
	current_dir[idx_current_dir] = NULL;

	// expand the directory if it was nesessary:
	int expension_block = allocated_blocks[1];
	if (need_expansion) {		
		if (!run_silent) {
			outputResult(output_stream, "\nDirectory expanded by block number: ");
			outputResult(output_stream, expension_block);
		}
		folder_blocks[idx_folder_blocks++]=expension_block;
		inodes[CurrentINode].size++;
		// See if the block needs to be added to the indirect blocks of the direct blocks:
		
		if (inodes[CurrentINode].size == DIR_BLOCKS + 1) {
			// yes, need to create the indirect block				
			if (method) 
				error = mgr1->Allocate(1, allocated_blocks);
			else 
				error = mgr2->Allocate(1, allocated_blocks);					
			if (error) {
				if (!run_silent) outputResult(output_stream, "\nERROR: Not enough free blocks");		
				return 1;
			}					
			inodes[CurrentINode].indir_block = allocated_blocks[0];							 	
		} else {
			// directory fits in the direct blocks
			inodes[CurrentINode].dir_blocks[inodes[CurrentINode].size-1] = expension_block;		
		}
	}	

	return 0;
}

/////////////////////////////////////////////////////////////////////
// ChangeDirectory () -  changes directory		
int File_Mgr::ChangeDirectory(char *dirname, strstream *output_stream){

	// see if we stand in the same dir
	if (strcmp(dirname, ".")==0) return 0;		

	// See if the filename exists
	int a;
	int innumber = -1;
	for (a = 0; a<idx_current_dir; a++) 		
		if (strcmp(current_dir[a]->name, dirname) == 0) innumber = current_dir[a]->inode;

	// check if the i-node was found
	if (innumber < 0) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Directory not found");
		return 1;
	}

	// make sure the it's a directory:	
	if (!inodes[innumber].directory) {
		if (!run_silent) outputResult(output_stream, "\nERROR: Not a directory");
		return 1;
	}

	// First thing to do is to save the current directory into the blocks:

	int ints_left = 0;
	int needed_ints;
	int next_pos;
	int next_cell;
	int blk_idx = 0;
	int blk_num = -1;
	char *ptC;
	

	for (a = 0; a < idx_current_dir; a++) {
		needed_ints = 2 + ((strlen(current_dir[a]->name) / sizeof(int)) + 1);
				
		if (ints_left < needed_ints) {
			// need to write the current block and jump to the one:

			// see if we need to write the block			
			if (blk_num != -1) {
				// Yes, we need to write the data as 
				temp[next_cell]  = 0;				
				if (method) 
					mgr1->WriteBlock(blk_num, (char *) temp, BLOCK_SIZE);
				else 
					mgr2->WriteBlock(blk_num, (char *) temp, BLOCK_SIZE);				
			}

			if (blk_idx == idx_folder_blocks) {
				if (!run_silent) outputResult(output_stream, "\nERROR: System consistency problem");
				return 1;
			}
			next_pos = 0;
			next_cell = 0;			
			blk_num = folder_blocks[blk_idx++];
			ints_left = BLOCK_SIZE / sizeof(int) - 1;
		}
		next_pos = next_pos + needed_ints; 
		temp[next_cell]  = next_pos;
		temp[next_cell + 1]  = current_dir[a]->inode;	
		
		strcpy((char *) &temp[next_cell + 2], current_dir[a]->name);
		next_cell = next_pos;
		temp[next_cell] = 0;
		ints_left = ints_left - needed_ints;		
	}
	
	if (next_pos != 0) {
		// Need to write the last block down
		if (method) 
			mgr1->WriteBlock(blk_num, (char *) temp, BLOCK_SIZE);
		else 
			mgr2->WriteBlock(blk_num, (char *) temp, BLOCK_SIZE);
	}
	
	// we might need to write down the indirect block number:
	if (idx_folder_blocks > DIR_BLOCKS) {
		// yes we do:		
		for (a=DIR_BLOCKS; a<idx_folder_blocks; a++) {
			temp[a-DIR_BLOCKS] = folder_blocks[a];			
		}
		if (method) 
			mgr1->WriteBlock(inodes[CurrentINode].indir_block, (char *) temp, BLOCK_SIZE);
		else 
			mgr2->WriteBlock(inodes[CurrentINode].indir_block, (char *) temp, BLOCK_SIZE);		
	}
	
	// see if we are even going to go anywhere:	
	if (innumber == CurrentINode) return 0;		

	// Update the path:
	if (strcmp(dirname, "..")==0) {		
		// getting out of the directory
		idx_current_path--;
		delete (current_path[idx_current_path]);
		current_path[idx_current_path]=NULL;
	} else {		
		// getting into one of the sub-directories
		current_path[idx_current_path] = new DirEntry;
		current_path[idx_current_path]->inode=innumber;				
		strcpy(current_path[idx_current_path]->name,dirname);
		idx_current_path++;		
	}	

// NOW, REMOVE ALL THE DATA ASSOCIATED WITH THE CURRENT DIRECTORY:

	// directory listing
	
	int bbb;
	for (a=0 ; a<MAX_INODES; a++) if (current_dir[a] != NULL) {				
		delete (current_dir[a]);		
		current_dir[a] = NULL;		
	}
	idx_current_dir = 0;

	// used directory blocks
	for (a=0 ; a<BLOCKS; a++) folder_blocks[a] = 0;
	idx_folder_blocks = 0;

// SET / READ NEW DIRECTORY ENVIROMENT VARIABLES:

	// current I-node number
	CurrentINode = current_path[idx_current_path-1]->inode;
	
	// readin directory blocks			
	if (inodes[CurrentINode].size <= DIR_BLOCKS) {		
		// only direct blocks were used, so we read just those
		for (a = 0; a < inodes[CurrentINode].size; a++) {			
			folder_blocks[idx_folder_blocks++] = inodes[CurrentINode].dir_blocks[a];						
		}
	} else {		
		// the Indirect block was used as well.
		// first read the direct block numbers:
		for (a = 0; a < DIR_BLOCKS; a++) {
			folder_blocks[idx_folder_blocks++] = inodes[CurrentINode].dir_blocks[a];
		}
		// Now read the rest of the block numbers off of the indirect block	
		if (method) 
			mgr1->ReadBlock(inodes[CurrentINode].indir_block, (char *) temp, BLOCK_SIZE);
		else 
			mgr2->ReadBlock(inodes[CurrentINode].indir_block, (char *) temp, BLOCK_SIZE);		
		a=0;
		
		while (idx_folder_blocks < inodes[CurrentINode].size) {
			folder_blocks[idx_folder_blocks] = temp[a];
			idx_folder_blocks++;
			a++;
		}
	}	
	
	// directory listing	
	idx_current_dir = 0;
	int b;
	for (a = 0; a < idx_folder_blocks; a++) {		
			
		// clear the temp buffer:
		for (b=0; b< (BLOCK_SIZE / sizeof(int)); b++) temp[b] = 0;

			
		// Read the block:
		if (method) 
			mgr1->ReadBlock(folder_blocks[a], (char *) temp, BLOCK_SIZE);
		else 
			mgr2->ReadBlock(folder_blocks[a], (char *) temp, BLOCK_SIZE);
			
				
		int next_position, current_position = 0;		
		next_position = temp[0];
		
		do {
			current_dir[idx_current_dir] = new FileEntry;			
			current_dir[idx_current_dir]->inode = temp[current_position + 1];			
			strcpy(current_dir[idx_current_dir]->name, (char *) &temp[current_position + 2]);			
			
			idx_current_dir++;
			current_position = next_position;						
			next_position = temp[current_position];
		} while (next_position!=0);
	}

	if (!run_silent) outputResult(output_stream, "\nDirectory changed.");
	return 0;
}

/////////////////////////////////////////////////////////////////////
// PrintMap() () -  prints out simple blocks map		
int File_Mgr::PrintMap(strstream *output_stream){
	if (!run_silent) if (method) return mgr1->PrintMap(); else return mgr2->PrintMap();
}

/////////////////////////////////////////////////////////////////////
// getDirectorySize() () - returns number of bytes needed to store the directory
int File_Mgr::getDirectorySize () {
	int a;
	int sum=0;
	for (a=0; a<idx_current_dir; a++) sum = sum + 3 + ((strlen(current_dir[a]->name) / sizeof(int)) + 1); 
	return (sizeof(int) * sum);
} 

/////////////////////////////////////////////////////////////////////
// getNeededBlocks (int) - returns number of blocks to store n bytes
int File_Mgr::getNeededBlocks (int bytes) {
	if ((bytes % (BLOCK_SIZE - sizeof(int))) == 0) return bytes / (BLOCK_SIZE - sizeof(int)); else
							       return 1 + bytes / (BLOCK_SIZE - sizeof(int)); 
}

/////////////////////////////////////////////////////////////////////
// outputResult (strstream *output_stream, char *) - sends string of characters into output
int File_Mgr::outputResult (strstream *output_stream,char *s) {	
	if (use_output_stream) output_stream->operator<<(s);  else   cout << s;	
}

/////////////////////////////////////////////////////////////////////
// outputResult (strstream *output_stream, int) - sends an integer into output
int File_Mgr::outputResult (strstream *output_stream,int a) {
	if (use_output_stream) output_stream->operator<<(a); else   cout << a;
}

/////////////////////////////////////////////////////////////////////
// outputResult (strstream *output_stream, char) - sends a character into output
int File_Mgr::outputResult (strstream *output_stream,char c) {        
	if (use_output_stream) output_stream->operator<<(c); else   cout << c;
}

// End of the file
/////////////////////////////////////////////////////////////////////


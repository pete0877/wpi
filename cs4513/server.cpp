/////////////////////////////////////////////////////////////////////
// File: server.cpp
// Implements the remote procedure for the RPC-based NFS
// Author: Peter Golaszewski
// Updated: 5/1/99
/////////////////////////////////////////////////////////////////////

#include <iostream.h>
#include <rpc/rpc.h>
#include <string.h>
#include <strstream>

#include "file_mgr.h" 
#include "FileSystem.h"

extern "C" {
#include "pgnfs.h" 
}

/////////////////////////////////////////////////////////////////////
// Define few constants for the server FS:
#define SWITCH_METHOD 0 // 0 = bitmap, 1 = link list
#define SWITCH_SILENT 0 // 1 = silent mode
#define SWITCH_CONT   0 // 1 = cont. mode  
#define OUTPUT_BUFFER 80*25

/////////////////////////////////////////////////////////////////////
// File System manager
File_Mgr *filemgr=NULL;


/////////////////////////////////////////////////////////////////////
// Implementation of the remote procedure:

/////////////////////////////////////////////////////////////////////
// sendcommand_1() - receives the command sent from the client
char ** sendcommand_1 (char **cmd) {	
	static char *ret;
	static char buffer[OUTPUT_BUFFER];			
	char flush[OUTPUT_BUFFER];
	char command[COMMAND_SIZE];
	int  command_arg_int;
	char command_arg_str[COMMAND_SIZE];
	strstream temp;	
	cout << "\nClient request: " << *cmd << "\n";			
	temp << *cmd;
	temp >> command;
	strstream *output_stream = new strstream();	
	output_stream->operator<<(">");
	if (!strcmp(command, "create")) {
		temp >> command_arg_str;									
		temp >> command_arg_int;							
		filemgr->CreateFile(command_arg_str, command_arg_int, output_stream);										
	} else
	if (!strcmp(command, "extend")) {
		temp >> command_arg_str;									
		temp >> command_arg_int;										
		filemgr->ExtendFile(command_arg_str, command_arg_int, output_stream);								
	} else
	if (!strcmp(command, "delete")) {
		temp >> command_arg_str;												
		filemgr->DeleteFile(command_arg_str, output_stream);
 	} else strcpy (buffer, "\nNFS communication error.\n");		
	
	strcpy(buffer, "");
	strcpy(flush, " ");
	while (strlen(flush)!=0) {			
		output_stream->getline(flush, OUTPUT_BUFFER);			
		if (strlen(flush)!=0) {				
			strcat(buffer, flush);
			strcat(buffer, "\n");
		}
	}
	output_stream->clear();  
	delete (output_stream);		
	ret=&buffer[1];
	return (&ret);
}

/////////////////////////////////////////////////////////////////////
// fs_init() - initializes the server FS
void fs_init() {	
	cout << "\nDistributed File System Server\n   by Peter Golaszewski, for CS4513 D99 Proj3";		
	filemgr = new File_Mgr(SWITCH_METHOD, SWITCH_SILENT, SWITCH_CONT, 1, NULL);
	cout << "\n\nNFS server started (press Ctrl+C to stop the server)...\n";
}

// End of the file
/////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
// File: client.cpp
// Implements a NFS client
// Author: Peter Golaszewski
// Updated: 5/1/99
/////////////////////////////////////////////////////////////////////

#include <iostream.h>
#include <rpc/rpc.h>
#include <fstream.h>
#include <stdio.h>
#include <string.h>

#include "file_mgr.h" 
#include "FileSystem.h"

extern "C" {
	#include "pgnfs.h" 
}

/////////////////////////////////////////////////////////////////////

int main (int argc, char **argv) {

	CLIENT *cl;	

// define few help variables
	int  count, error;
	char command[COMMAND_SIZE];
	char bad_switch[COMMAND_SIZE];
	int  command_arg_int;
	char command_arg_str[COMMAND_SIZE];
	char *server=NULL, **result, *remote_command;
	char text[200];


	// reset command arguments switches and variables
	int switch_method = 1;  //  0 - list    1 - bit map (default)
	int switch_silent = 0;
	int switch_loop = 0;
	int switch_cont = 0;
	int switch_help = 0;
	int switch_filefeed = 0;
	char command_filename [FILENAME_SIZE] = "";
	int file_loops = FILE_LOOPS;

	
	error = 0;
	for (count=1; count<argc; count++) {
		if (!strcmp(argv[count],"-l")) switch_method = 0; else
		if (!strcmp(argv[count],"-b")) switch_method = 1; else
		if (!strcmp(argv[count],"-s")) switch_silent = 1; else
		if (!strcmp(argv[count],"-r")) switch_loop   = 1; else
		if (!strcmp(argv[count],"-c")) switch_cont   = 1; else
		if (!strcmp(argv[count],"-help")) switch_help   = 1; else
		if (!strcmp(argv[count],"-nfs")) {
				if (count+1 < argc) {
						count++;
						server = argv[count];						
				} else error = 3;  // no filename provided
		} else
		if (!strcmp(argv[count],"-f")) {
			switch_filefeed = 1;
			if (count+1 < argc) {
				count++;
				strcpy (command_filename,argv[count]);
			} else error = 2;  // no filename provided
		} else {
			error = 1;   // unknown switch 
			strcpy(bad_switch,argv[count]);
		}
	}
	cout << "\nDistributed File System Client\n   by Peter Golaszewski, for CS4513 D99 Proj3\n";	
	if (switch_help) {		
		cout << "   Command line switches: \n";
		cout << "\n             -l : Use linked list method ";
		cout << "\n             -b : Use bit map method ";
		cout << "\n             -s : Run in silent mode ";		
		cout << "\n             -c : Allocate only contiguous blocks ";
	    cout << "\n             -r : Repeat the command file " << FILE_LOOPS << " time(s)";		
		cout << "\n    -f filename : Use the command file ";
		cout << "\n   -nfs address : Use machine under the address as the NFS server";		
		cout << "\n          -help : Display this help";		
		cout << "\n  Program commands:";
		cout << "\n    create F N : create file F using N blocks";
		cout << "\n   rcreate F N : create file F using N blocks on the NFS server";		
		cout << "\n    entend F N : extend file F by additional N blocks";
		cout << "\n    delete F   : delete file F";
		cout << "\n    dir        : print short directory listing";
		cout << "\n    longdir    : print long directory listing";
		cout << "\n    map        : display block map (local only)";
		cout << "\n    help       : display commands list";
		cout << "\n    quit       : quit command prompt\n";								
		return 0;

	}
	
	if ((server==NULL) || (error == 3) ) {
			cout << "\nYou need to specify the nfs server address after -nfs switch.";	
			cout << "\nType 'client -help' for more options. \n";	
			return 0;
	} else {
		if ((cl=clnt_create (server, PGNFS_PROG, PGNFS_VER, "udp"))==NULL)  {
			clnt_pcreateerror(server);
			exit(2);
		}
	}
	

	cout << "(special commands are included in this program. Type help for help.)\n";
	if (error) {
		if (error == 1) {
			cout << "Unknown command switch: ";
			cout << bad_switch;
			cout << ". Run with -help for list of switches\n";
		}
		if (error == 2) cout << "Missing commands file name. Run with -help for list of options\n";		
		return 0;
	}
	
	if (switch_method)   cout << "\n    - using bit map method"; else
						 cout << "\n    - using linked-list method";
 	if (switch_filefeed) cout << "\n    - using command file: " << command_filename;	
	if (switch_silent)   cout << "\n    - silent mode";
	if (switch_loop)     cout << "\n    - file loop " << FILE_LOOPS << " time(s)";		
	if (switch_cont)     cout << "\n    - using contiguous mode";
	
	// create the manager:
	File_Mgr filemgr(switch_method, switch_silent, switch_cont, 0, cl);
	
	ifstream input_file;
	if (switch_filefeed) {
		 input_file.open(command_filename);
		 if (input_file==NULL) {
		 	cout << "\n\nCould not open the command file for reading.\n\n";
		 	clnt_destroy(cl);
			return 0;
		 }
	}
		
	while (1) {
		if (switch_filefeed) {
			// we are using file feed
			if (!input_file) {
				// check if we were supposed to loop again:
				if (switch_loop && (file_loops > 0)) {
					file_loops--;		
					// yes ... reopen the file
					input_file.close();
					input_file.open(command_filename);
					if (input_file==NULL) {
						cout << "\n\nCould not re-open the command file for reading.\n\n";
						clnt_destroy(cl);
						return 0;
					}
					input_file >> command;			
					if (!strcmp(command,"")) strcpy (command,"N");
					// we also want to report the overspan .. the command file will not have it,
					// but we need it for the analysis:
				} else strcpy (command, "quit");  // no, just quit. 
			} else {
				input_file >> command;			
				if (!strcmp(command,"")) strcpy (command,"N");
			}
		} else {
			// stdin feed
			cout << "\n\nCommand: ";
			cin >> command;		
		}
		if (!strcmp(command, "rcreate")) {
			if (switch_filefeed) input_file >> command_arg_str; else cin >> command_arg_str;									
			if (switch_filefeed) input_file >> command_arg_int; else cin >> command_arg_int;									
			filemgr.RemoteCreateFile(command_arg_str, command_arg_int, NULL);
		} else 		
		if (!strcmp(command, "create")) {
			if (switch_filefeed) input_file >> command_arg_str; else cin >> command_arg_str;									
			if (switch_filefeed) input_file >> command_arg_int; else cin >> command_arg_int;									
			filemgr.CreateFile(command_arg_str, command_arg_int, NULL);
		} else 
		if (!strcmp(command, "extend")) {
			if (switch_filefeed) input_file >> command_arg_str; else cin >> command_arg_str;									
			if (switch_filefeed) input_file >> command_arg_int; else cin >> command_arg_int;									
			filemgr.ExtendFile(command_arg_str, command_arg_int, NULL);
		} else 
		if (!strcmp(command, "delete")) {
			if (switch_filefeed) input_file >> command_arg_str; else cin >> command_arg_str;												
			filemgr.DeleteFile(command_arg_str, NULL);
		} else 
		if (!strcmp(command, "dir")) { filemgr.ListContents(NULL); } else 
		if (!strcmp(command, "longdir")) { filemgr.ListContentsLong(NULL); } else 
		if (!strcmp(command, "map")) filemgr.PrintMap(NULL); else 		
		if (!strcmp(command, "help")) {
			cout << "\n  Program commands: \n";
			cout << "\n    create F N : create file F using N blocks";
			cout << "\n   rcreate F N : create file F using N blocks on the NFS server";		
			cout << "\n    entend F N : extend file F by additional N blocks";
			cout << "\n    delete F   : delete file F";
			cout << "\n    dir        : print short directory listing";
			cout << "\n    longdir    : print long directory listing";
			cout << "\n    map        : display block map (local only)";
			cout << "\n    help       : display commands list";
			cout << "\n    quit       : quit command prompt\n";					
		} else 
		if (!strcmp(command, "N") || !strcmp(command, "n")) {
		    // no-op command. Used to handle the empty lines of the input
		} else
		if (!strcmp(command, "quit")) {			
			if (switch_filefeed) input_file.close(); {
				clnt_destroy(cl);
				return 0;
			}
		} else cout << "\nError: Unknown command: " << command << ".Type help for help ... ";
	}			
}

// End of the file
/////////////////////////////////////////////////////////////////////

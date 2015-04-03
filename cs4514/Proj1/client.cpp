////////////////////////////////////////////////////////////////////////////
// Client - Oracle Project, Peter Golaszewski, 8/11/98
// CS 4514 B98, WPI - Computer Networks
////////////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <strings.h>
#include <iostream.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

#include "project1.h"
#include "oracle.h"

////////////////////////////////////////////////////////////////////////////

int getOracleServices(char *service, char *uid, om *svr_lst[])
{
	sockaddr_in   	socket_address; // Few variables used for networking
	hostent 	*host_entry;	
	servent 	*server_entry;		
	int  		client_socket;	
	char 		*oracleserver="garden.wpi.edu";
	int 		len, count, number;

	cout << "    Connecting to Oracle ... ";
	if ((host_entry = gethostbyname(oracleserver)) == NULL) {
	    // Failed to get server info
	    cout << "\n	ERROR: Could not find the oracle server on the net.\n";
	    return -1;
	}
    	if ((server_entry = getservbyname("netoracle","udp")) == NULL) {  
	    // Failed to get server info
	    cout << "\n	ERROR: Could not find the oracle server port.\n";				
	    return -1;
	}

	socket_address.sin_port = server_entry->s_port;  
	socket_address.sin_family = AF_INET;
	bcopy(host_entry->h_addr, (char *) &socket_address.sin_addr, host_entry->h_length);				
	if ((client_socket = socket (AF_INET, SOCK_DGRAM, 0)) < 0)  {
		  // Failed to establish the socket:
		  cout << "\n	ERROR: Could not open the socket.\n";
		  return -1;
	}
	if (connect(client_socket, (struct sockaddr *) &socket_address, sizeof(socket_address)) < 0) {
		    // Failed to connect to the server
		    cout << "\n	ERROR: Could not connect to the oracle server.\n";
		    return -1;
	}
	cout << "ok.\n";
		    
	// Ask Oracle for the services list:
	om *om00 = new om;
	om00->ver = verCur;
	om00->cmd = cmdGet;
	strcpy(om00->uid, uid);
	strcpy(om00->sbServ, service);
		    
	if (write(client_socket,om00,lom) < 0) { 
		cout << "\n	ERROR: Oracle refuses receiving commands."; 
		return -1;
	}
	delete (om00);

	// First delete the list of known services			
	for (count = 0; count < MAX_SERVICE_NUM; count++) 
		if (svr_lst[count] == NULL) 
			delete (svr_lst[count]);
	number = 0;
			
	while (1) {
		om *om00 = new om;
		if ((len = read(client_socket,om00,lom)) < lom) {
			cout << "\n	ERROR: Oracle refuses sending massages."; 
			return -1;
		}		
		if (om00->cmd == cmdEnd) { 
			close (client_socket);
			return number; 
		} 
		svr_lst[number++] = om00;
		if (om00->cmd == cmdErr) { 
			close (client_socket);
			return number; 
		} 
	}
}

////////////////////////////////////////////////////////////////////////////

int connect2Service(om *theom)
{
	int client_socket, result, len; // Socket and help variables

	cout << "    Connecting to: " << theom->sbServ << " [" << theom->uid << "] ... ";		  

	if ((client_socket = socket (AF_INET, SOCK_STREAM, 0)) < 0) {
		// Failed to establish the socket:
		cout << "\n	ERROR: Could not open the socket.\n";
		return -1;
	}
	if (connect(client_socket, (struct sockaddr *) &theom->sa, sizeof(theom->sa)) < 0) {
		// Failed to connect to the server
		cout << "\n	ERROR: Could not connect to the service server.\n";
		return -1;
	} 

	cout << "ok.\n    Use CTRL-D to terminate service connection:\n\n";
			    
	// Following peace of code handles parralel communication between
	// us (the client) and the server (TCP service)

	int pid;
	int fdServer;
	char sb[cchMax];
	char ch;
	int cch;
	fd_set bvfdRead;
			    
	// Let's use the socket that we already have opened
	fdServer = client_socket;	
			    
	FD_ZERO(&bvfdRead);
	FD_SET(fdServer, &bvfdRead);   /* socket for the server */
	FD_SET(fdIn, &bvfdRead);	   /* fd for stdin */
	result = 1;
	do {
		len = select (fdServer+1, &bvfdRead, 0, 0, 0);
			      
		/* see what fd's have activity */
		if (FD_ISSET(fdServer, &bvfdRead)) {
			/* activity on socket (may be EOF) */
			cch = read (fdServer, sb, cchMax);
			if (cch <= 0) result = 0;
			 else write (fdOut, sb, cch);	/* write to stdout */
		}
		if (FD_ISSET(fdIn, &bvfdRead)) {
			/* activity on stdin (may be EOF) */
			cch = read (fdIn, sb, cchMax);
			if (cch <= 0) result = 0;
			 else write (fdServer, sb, cch);
		}
		if (result && len > 0) {
			/* get ready for another call to select */
			FD_ZERO(&bvfdRead);
			FD_SET(fdServer, &bvfdRead);   /* socket for the server */
			FD_SET(fdIn, &bvfdRead);	   /* fd for stdin */
		}
	} while (result && len > 0);
			    
	// Close the communication
	result = close (client_socket);
	cout << "\n    Service connection closed.\n";
	return 0;
}

////////////////////////////////////////////////////////////////////////////

int main ()
{
  char command[MAX_COMMAND_LEN] = "\0",	// Command input related variables
    *command_token,
    *service,
    *uid; 	                        
  
  om   *services_list[MAX_SERVICE_NUM];	// List of known services
  int  services_list_len;
 
  sockaddr_in   socket_address; // Few variables used for networking
  hostent 	*host_entry;	
  servent 	*server_entry;		
  int  		client_socket;	
  char 		*oracleserver="garden.wpi.edu";
  
  char 		buffer[MAX_BUFFER_SIZE];  // other help variables used in the program
  int 		len, result, count, quit_receiving;	

  for (count = 0; count < MAX_SERVICE_NUM; count++) services_list[count] = NULL;
  
  cout << "\n\nOracle client by Peter Golaszewski, version 1.0.\n";
  cout << "Type help for list of commands.\n";
  
  while (strcmp(command, "quit")!=0) {
    cout << "\nCommand: ";

    // Get the command from the standard input
    gets(command);
    
    command_token = strtok(command, " ");

    // Parse the command
    if (!strcmp(command,"quit")) {
      cout << "    Quiting the client program.\n\n";
      // we need to delete allocated momory, so that we don't leak
      for (count = 0; count < services_list_len; count++)
	{ delete (services_list[count]); }
      services_list_len = 0;
    } else
      if (!strcmp(command,"help")) {
	cout << "    Following commands are case-sensitive:\n";
	cout << "      list [SERVICE] [UID]- lists available oracle services.\n";
	cout << "      connect SERVICE [UID] - connect to the SERVICE\n";
	cout << "      quit - exit the client program\n";
      } else
	if ((command_token!=NULL ) && (!strcmp(command_token,"list"))) {
	  // Refresh the list of articles and display them:  
	  service = strtok(NULL, " ");
	  uid = strtok(NULL, " ");
	  if (uid == NULL) {
	          uid = new char[5];	
		  strcpy (uid,"");
	  }
	  if (service == NULL) {
	          service = new char[5];	
		  strcpy (service,"");
	  }

	  services_list_len = getOracleServices(service, uid, services_list);
	  if (services_list_len == 0) {
	      cout << "    \nSorry, there are no services available. Try later.\n";	  		
	  } else
	  if (services_list_len > 0) {
	  	  count = 0;
		  quit_receiving = 0;
		  while ((count < services_list_len) && (!quit_receiving)){
		  	if (services_list[count]->cmd == cmdErr){
				quit_receiving = 1;
				cout << "    \nOracle Error: "<< services_list[count]->sbDesc <<"\n";	  						
			} else cout << "   " << services_list[count]->sbServ <<" [" << services_list[count]->uid << "] - " << services_list[count]->sbDesc <<"\n";						
			
			count++;
		  }
	  }
  	} else
	    if ((command_token!=NULL ) && (!strcmp(command_token,"connect"))) {
	      service = strtok(NULL, " ");
	      uid = strtok(NULL, " ");
	      if (uid == NULL) {
	          uid = new char[5];	
		  strcpy (uid,"");
	      }
	      if (service == NULL) { cout << "    Bad command syntax. Type help for list of commands.\n"; }
	      else 
		{
	  		services_list_len = getOracleServices(service, uid, services_list);
	  		if (services_list_len == 0) { cout << "    \nSorry, there is no such services available. Try list command.\n"; } 
			else
	  		if (services_list_len > 0) {
		  		if (services_list[0]->cmd == cmdErr) { cout << "    \nOracle Error: "<< services_list[0]->sbDesc <<"\n"; } 
				else {
					// found it !!!
					result = connect2Service(services_list[0]);
	  			}
			}
		}
	    } else {
	      // Bad command message:
	      cout << "    Unknown command. Type help for list of commands.\n";
	    }
  }
  
}

////////////////////////////////////////////////////////////////////////////
// End of the source code for the Client
 









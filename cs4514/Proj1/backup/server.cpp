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
#include <pwd.h> 
#include <unistd.h>
#include <signal.h>
#include <ctype.h>

#include "project1.h"
#include "oracle.h"

////////////////////////////////////////////////////////////////////////////

int manageServiceAtOracle(int registerMode, int port)
{

	sockaddr_in   	socket_address, tmp; // Few variables used for networking
	hostent 	*host_entry;	
	servent 	*server_entry;		
	int  		client_socket;	
	char 		*oracleserver="garden.wpi.edu";
	long	   	sin_addr = 0;
	int 		len, count, number;
	char		username[50];

	strcpy(username, getlogin());

	if (registerMode) {
	 cout << "    Getting the username: " << username << "\n";
	 cout << "    Getting the server address ... ";				
	}

    if ((host_entry = gethostbyname("localhost")) == NULL) {
	    // Failed to get server info
	    cout << "\n	ERROR: Could not find the localhost name.\n";
	    return -1;
	}

	bcopy(host_entry->h_addr, (char *) &tmp.sin_addr, host_entry->h_length);				

	if (registerMode) {cout << "ok\n    Trying to register the service ... ";} else		
	{ cout << "    Trying to unregister the service ... "; }		
	
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
		    
	// Ask Oracle to register our service:
	om *om00 = new om;
	om00->ver           = verCur;

	if (registerMode) 
	   om00->cmd        = cmdPut;
        else
	   om00->cmd        = cmdClr;

	om00->sa.sin_addr   = tmp.sin_addr;
	om00->sa.sin_family = AF_INET;
	om00->sa.sin_port   = port;	
	strcpy(om00->uid,     username);
	strcpy(om00->sbServ,  "exp_eval");
	strcpy(om00->sbDesc,  "Reverse Polish Notation Evaluator");	
		    
	if (write(client_socket,om00,lom) < 0) { 
		cout << "\n	ERROR: Oracle refuses receiving commands."; 
		return -1;
	}
	delete (om00);

	// First delete the list of known services			
	
	om00 = new om;
	if ((len = read(client_socket,om00,lom)) < lom) {
		cout << "\n	ERROR: Oracle refuses sending massages."; 
		return -1;
	}		

	if (registerMode) {
	 if (om00->cmd == cmdAckPut) { 
		close (client_socket);
		cout << "ok.\n";
		return 0; 	
	 } else {
		close (client_socket);
		cout << "\n	ERROR: Oracle refuses registering the service.\n"; 		
		return -1; 
	 }
	} else {
	 if (om00->cmd == cmdAckClr) { 
		close (client_socket);
		cout << "ok.\n";
		return 0; 	
	 } else {
		close (client_socket);
		cout << "\n	ERROR: Oracle refuses unregistering the service.\n"; 		
		return -1; 
	 }
   	}
}

////////////////////////////////////////////////////////////////////////////

float polish_eval(char *exp, int *error) {	

	float stack[500],a,b,e,number;
	int   stack_pos=0,c,first_digit;

	// Make sure there is at least one space behind the input
	// (makes it easier to parse nubmers)
	c = strlen(exp);
	exp[c+0]=' ';
	exp[c+1]='\0';

	c=0;
	number = 0;
	first_digit = 0;
	while (exp[c] != '\0')
	{		
		if ((exp[c] == '+') || (exp[c] == '-') || (exp[c] == '*') || (exp[c] == '/')) {

			if (first_digit) {
				stack[stack_pos++] = number;
				first_digit = 0;
				number = 0;
			}

			// Pop last two values and do it !!!
			if (stack_pos >= 2) {
				b = stack[--stack_pos];
				a = stack[--stack_pos];
				if (exp[c] == '+') e = a + b;
				if (exp[c] == '-') e = a - b;
				if (exp[c] == '*') e = a * b;
				if (exp[c] == '/') e = a / b;
				stack[stack_pos++] = e;
			} else {
				error[0] = 1;				
				error[1] = c;
				return 0;
			}
			
		} else 
		if (isdigit(exp[c]))
		{
			first_digit = 1;
			number = 10 * number + (exp[c]-'0');
		} else
		if (exp[c]==' ')
		{
			if (first_digit) {
				stack[stack_pos++] = number;
				first_digit = 0;
				number = 0;
			}

		}
		c++;		
	}
	if (stack_pos > 0) { 
		error[0] = 0;
		return stack[--stack_pos]; 		
	}
	error[0] = 1;
	error[1] = c;
	return 0;
}

////////////////////////////////////////////////////////////////////////////

void shut_down(int blabla)
{
	if (!manageServiceAtOracle(UNREGISTER, 0))
		cout << "	Terminating one of the server proccesses.\n";
	else
		cout << "	Terminating one of the server proccesses. Oracle refuses unregistering the service.\n";
	exit (0);
}


////////////////////////////////////////////////////////////////////////////

int main ()
{
  cout << "\n\nReverse Polish Notation Evaluator\n";
  cout << " Oracle service by Peter Golaszewski, version 1.0.\n";

  signal(SIGINT, shut_down);

  cout << "\n    Trying to allocate the socket ... ";		
  // Initialize the socket address:
  sockaddr_in 	   socks, socks2, socks3;
  socks.sin_family = AF_INET;
  socks.sin_port   = 0;
  socks.sin_addr.s_addr   = INADDR_ANY;

  // Open and Bind the socket:
  int server_socket;
  if ((server_socket=socket(AF_INET, SOCK_STREAM, 0))<0) {
	cout << "\n	ERROR: Could not open the socket.\n\n";
	return 0;
  }
  if (bind (server_socket, (struct sockaddr *) &socks, sizeof(socks)) < 0) {
	cout << "\n	ERROR: Could not bind the socket.\n\n";
	return 0;
  }
	
  // Now that we have the server ready to start accpeting connections,
  // we need to find out it's new address and register with Oracle:
  int ssize = sizeof(socks2);
  if (getsockname (server_socket, (struct sockaddr *) &socks2, &ssize) < 0) {
	cout << "\n	ERROR: Could not get the service port.\n\n";
	return 0;
  }
  cout << "ok\n";

  int connection, len;
  char buffer[MAX_BUFFER_SIZE];
  
  if (!manageServiceAtOracle(REGISTER, socks2.sin_port))
  {
	cout << "____________________________\nSERVER LOG:\n";
 	listen (server_socket,5);
	while (1)
	{
		if ((connection = accept (server_socket, (struct sockaddr *) &socks, &len)) < 0) {
			cout << "\n	ERROR: Can not start client communication.\n\n";
			exit (1);							
		}
		if (fork() == 0)
		{
			socks3 = socks;
			cout << "Opening a new connection.\n";

			strcpy(buffer,"\nReverse Polish Notation Evaluator, by Peter Golaszewski.\n");
			if (write (connection, buffer, strlen(buffer)) < 0 ) { 
				cout << "\n	ERROR: Can not send data.\n\n";
				exit (1); }

			do {
				strcpy(buffer,"\nEVAL: ");
				if (write (connection, buffer, strlen(buffer)) < 0 ) {
					cout << "\n	ERROR: Can not send data.\n\n";
					exit (1); }
				len=read (connection, buffer, MAX_BUFFER_SIZE);
				if (len>0) {
								
					int  errors[2];
					float result, anything;
					int ccc;

					errors[0]=0;
					errors[1]=0;
					buffer[len]='\0';

					anything = 0;
					for (ccc=0; ccc<len; ccc++)
						if ((isdigit(buffer[ccc])) || 
						    (buffer[ccc]=='+') ||
						    (buffer[ccc]=='-') ||
						    (buffer[ccc]=='*') ||
						    (buffer[ccc]=='/')) anything=1;
					if (anything) {
 					  result = polish_eval(buffer, errors);
					  if (!errors[0]) 
						{ sprintf(buffer, "%.5f\n", result); }
					  else    { sprintf(buffer, "Syntax error on position %d.\n", errors[1]);}
	
					  if (write (connection, buffer, strlen(buffer)) < 0 ) {
						cout << "\n	ERROR: Can not send data.\n\n";
						exit (1); }
					}
				}
			} while (len>0);			
			cout << "Closing existing connection.\n";
			close(connection);
			exit (0);
		}
	}	
  }   
}

////////////////////////////////////////////////////////////////////////////
// End of the source code for the Server
 









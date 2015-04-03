# define luid 16
# define cchMaxServ 10
# define cchMaxDesc 40
# define verCur 'C'

enum cmd {
  cmdErr,                       /* An error occurred. See sbDesc for details */
  cmdGet,                       /* Get the address of a service */
  cmdAckGet,                    /* ACK for cmdGet message */
  cmdEnd,                       /* Last response to a cmdGet message */
  cmdPut,                       /* Register a new service  */
  cmdAckPut,                    /* ACK for cmdPut message */
  cmdClr,                       /* Unregister a service */
  cmdAckClr                     /* ACK for cmdClr message */
};

struct om {                     /* oracle message */
  char ver;                     /* version number of this structure */
  enum cmd cmd;                 /* command/reply code */
  char sbDesc[cchMaxDesc];      /* description of service (or error reply) */
  char uid[luid];               /* user id (login id) of requester/provider */
  char sbServ[cchMaxServ];      /* name of service requested/provided */
  struct sockaddr_in sa;        /* socket addr where service is available */
  unsigned long ti;             /* time of registration */
};
# define lom (sizeof (struct om))

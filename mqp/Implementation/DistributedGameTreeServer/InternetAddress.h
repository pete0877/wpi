// InternetAddress.h
//

#if !defined(InternetAddress_h)
#define InternetAddress_h

/*
 * InternetAddress Class is used to store the Internet identity of
 * a particular helper. It gathers all the information on would
 * need to contact the helper process on another machine and
 * request one of the services.
 */

#include "resource.h"

typedef struct tagInternetAddress {
    char ipAddress[256];
} InternetAddress;

#endif /* InternetAddress_h */

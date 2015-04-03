/////////////////////////////////////////////////////////////////////////////
// DLLSocket.h
//	This file defines DLL class and its dependencies
/////////////////////////////////////////////////////////////////////////////

#ifndef DLLSOCKET_H
#define DLLSOCKET_H

#include "Common.h"
#include "Socket.h"
#include "CRC.h"

#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>
#include <time.h>
#include <sys/socket.h>
#include <iostream.h>

/////////////////////////////////////////////////////////////////////////////
const int FRAME_DATA_SIZE   = 96;    // Fixed size of the data part of the frame
const int WINDOW_SIZE       = 16;     // See Selective-Repeat
const int TIMEOUT           = 1;     // Timeout in timeout units.
const int TIMEOUT_UNIT      = 1;     // Time in sec.

struct Frame {
	int	   seq;			// frame sequence number
	char	   isused;			// 1 if the frames is outstanding, 0 if not
	char	   nAcked;			// 1 if an NACK has been sent for this frame
	int		datastart;			// Data head pointer
	int		dataend;			// Data tail pointer
	int		timeout_counter;	// Data size
   int 	   crc32check;
	char	data[FRAME_DATA_SIZE];  // frame data
};

enum ack_commands {
	cmdAck,     // Positive Ack for a frame
	cmdAckNot   // Negative Ack for a frame
};

struct Ack {
	ack_commands command;           // Positive / Negative
	int	     seq;		// Frame sequence number
};

const int FRAME_LEN   = sizeof (Frame);
const int ACK_LEN     = sizeof (Ack);

/////////////////////////////////////////////////////////////////////////////
// Class DLLSocket

class DLLSocket : public Socket {
 private:
 	int   m_errorpct; // error hander
	int   m_crc_errorpct; // error hander

	int   size;     // Number of currently outstanding frames

	int   send_next_seq;     // Sequence number of the next frame to be sent
	int   send_expected_seq; // Sequence number of the frame we are expected to receive Ack

	int   recv_lowest_seq;     // Lowest sequence number the receiver is willing to receive
	int   recv_higher_seq;     // 1 + the histest numbered frame the receiver is willing to accept

	Frame outgoing_frames_buffer [2 * WINDOW_SIZE]; // All the frames sent but not ACK'ed
	Frame incoming_frames_buffer [2 * WINDOW_SIZE]; // All the frames sent but not ACK'ed

	void ResetLayer(); 	// Called by constructors to reset all the resources

	int recv_block(char *message, int message_size);
	int send_block(char *message, int message_size);

	int AckNAckReceiver();
   int between(int a, int b, int c);

   int show_stat();
   int reset_stat();

   int inc(int sqn);
   int dec(int sqn);

   int get_random_percent();

   // Stat variables:
   float stat_datarate;
	int   stat_data_sent;     // includes frame data sizes of resends
	int   stat_frames_sent;   // includes resends
	int   stat_resend;        // includes all resends
	int   stat_resend_bynack; // includes all resends
	int   stat_timeouts;      //
	int   stat_acks_recv;     //
	int   stat_nacks_recv;    //
	int   stat_data_recv;     // size of date from any frame received
	int   stat_frames_recv;   // total number of frames that came in on the socket
	int   stat_bad_crcs;      //
	int   stat_acks_sent;     // can be less then number of frames that came in corretly.
	int   stat_nacks_sent;    //

 public:
	DLLSocket(Socket *socket, int errorpct, int crc_errorpct) : Socket(socket->getSocket()) {
		m_errorpct = errorpct;
		m_crc_errorpct = crc_errorpct;
		ResetLayer();
	}
	DLLSocket(const char *hostname, int port, int errorpct, int crc_errorpct) : Socket(hostname, port) {
		m_errorpct = errorpct;
		m_crc_errorpct = crc_errorpct;
		ResetLayer();
	}
	DLLSocket(Socket *socket, int errorpct) : Socket(socket->getSocket()) {
		m_errorpct = errorpct;
      m_crc_errorpct = 5;
		ResetLayer();
	}
	DLLSocket(const char *hostname, int port, int errorpct) : Socket(hostname, port) {
		m_errorpct = errorpct;
      m_crc_errorpct = 5;
		ResetLayer();
	}
	~DLLSocket() { show_stat(); }
	void recv(void *message, int message_size);
	void send(void *message, int message_size);

};
/////////////////////////////////////////////////////////////////////////////

#endif

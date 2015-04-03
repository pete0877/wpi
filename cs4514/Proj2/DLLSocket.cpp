/////////////////////////////////////////////////////////////////////////////
// DLLSocket.cpp
//	Implements the DLLSocket class
/////////////////////////////////////////////////////////////////////////////

#include "DLLSocket.h"

////////////////////////////////////////////////////////////////////////////
void DLLSocket::ResetLayer() {
		size = 0;
		// Mark all the frames unused:
		for (int c=0; c < 2 * WINDOW_SIZE; c++) {
      	outgoing_frames_buffer[c].isused = 0;
			incoming_frames_buffer[c].isused = 0;
			incoming_frames_buffer[c].nAcked = 0;
		}
		// Initialize the send. sequence numbers
		send_expected_seq = 0;
		send_next_seq     = 0;
		// Initialize the recv. sequence numbers
		recv_lowest_seq   = 0;
		recv_higher_seq   = WINDOW_SIZE;

      reset_stat();
      srand(time(NULL));
}
////////////////////////////////////////////////////////////////////////////
int DLLSocket::between(int a, int b, int c) {
	return (
   	((a<=b) && (b<c))  ||
   	((c<a)  && (a<=b))  ||
   	((b<c)  && (c<a))
   );
}
////////////////////////////////////////////////////////////////////////////
int DLLSocket::inc(int sqn) {
	if (sqn == (2 * WINDOW_SIZE - 1)) return 0; else
	return (sqn + 1);
}
////////////////////////////////////////////////////////////////////////////
int DLLSocket::dec(int sqn) {
	if (sqn == 0) return (2 * WINDOW_SIZE - 1); else
	return (sqn - 1);
}
////////////////////////////////////////////////////////////////////////////
int DLLSocket::get_random_percent() {
	return ((int) (101.0*rand()/(RAND_MAX+1.0)));
}
////////////////////////////////////////////////////////////////////////////
int DLLSocket::AckNAckReceiver () {
		// It checks if there are any Acks / NAcks
		// coming in. If an Ack comes in, it sets .isused = 0 for the frame
		// of given sequence number.
      // If the ack is for the frame is for the oldest frame that was
      // sent out, it moves the window up and returns
		// If the NAck comes in, the thread resends the frame, and sets its
		// .timeout_counter = TIMEOUT.
		// procedure also decrements the .timeout_counter variable of each
		// frame (for which .isused == 1) every 1 second. If any of these
		// timeout_coutner's become 0, it resends the frame and sets its
		// .timeout_counter = TIMEOUT.

		int c,len;
		Ack AckOrNAck;
		timeval watingtime;
      fd_set selectset;

		while (1) {
		   // Wait till 1 sec passes or a data comes in on the
		   // sock socket using SELECT function:

		   // We will wait one sec before we stop checking the socket:
		   watingtime.tv_sec  = TIMEOUT_UNIT;
		   watingtime.tv_usec = 0;
		   // Clear the set
		   FD_ZERO(&selectset);

		   // set the socket descriptor in the set
		   FD_SET(m_socket, &selectset);   /* socket for the server */

		   len = select (m_socket+1, &selectset, 0, 0, &watingtime);

		   if (len==0) {
		   		// Socket time out
				// If data didn't come in, decrement all the frame timeout counters:
				for (c = 0; c < 2 * WINDOW_SIZE; c++)
					if (outgoing_frames_buffer[c].isused && (outgoing_frames_buffer[c].timeout_counter > 0))
					   outgoing_frames_buffer[c].timeout_counter--;
				// Check if any of the frames timed out:
				for (c = 0; c < 2 * WINDOW_SIZE; c++)
					if (outgoing_frames_buffer[c].isused && (outgoing_frames_buffer[c].timeout_counter == 0))
					{
						// Yes, this one did timeout, so we resend it:

						// Set up the frame timeout
			  			outgoing_frames_buffer[c].timeout_counter = TIMEOUT;

                  // Mark the stats:
                  stat_data_sent = stat_data_sent + outgoing_frames_buffer[c].dataend;
                  stat_frames_sent++;
                  stat_resend++;
                  stat_timeouts++;

					  	// Send the frame again:
                  // We need to let the errors get in:
						int original_crc = outgoing_frames_buffer[c].crc32check;
                  // Mess up the Check-sum:
						if (m_crc_errorpct >= get_random_percent()) outgoing_frames_buffer[c].crc32check++;
                  // Drop the frame if we get lucky:
          			if (m_errorpct <= get_random_percent()) send_block((char *) &outgoing_frames_buffer[c],FRAME_LEN);
                  outgoing_frames_buffer[c].crc32check = original_crc;
					}
	       } else {
			    // There was an activity on the socket
				// Data came in, process it:
				// Let the AckOrNAck be the Ack / NAck:
				len = recv_block((char *) &AckOrNAck, ACK_LEN);
				// Check if it's an Ack or a NAck:
				if (AckOrNAck.command == cmdAck) {
            	stat_acks_recv++;
					// Now, we need to move the window up if the incoming Ack was for the
					// oldest outstanding frame:
					while (between(send_expected_seq,AckOrNAck.seq,send_next_seq)) {

						// It's an Ack. We need to mark that the frames below it
                  // arrived ok, so that the timeouts routine, doesn't
                  // resend them:
						outgoing_frames_buffer[send_expected_seq].isused = 0;

						// Shrink the window, and mark free space in the buffeR:
						send_expected_seq = inc(send_expected_seq);
						size--;
					}
					return 0;
				} else {
            	stat_nacks_recv++;
					// It's a NAck, send the frame again, but only if it is within
               // our window:
               if (between (send_next_seq,AckOrNAck.seq,send_next_seq)) {
                  stat_resend++;
                  stat_resend_bynack++;
						outgoing_frames_buffer[AckOrNAck.seq].timeout_counter = TIMEOUT;
						send_block((char *) &outgoing_frames_buffer[AckOrNAck.seq],FRAME_LEN);
					}
				}
		   }
		}
		return 0;
}
////////////////////////////////////////////////////////////////////////////
int DLLSocket::recv_block (char *message, int message_size) {
	int got_so_far=0,len=0;
	while ((got_so_far != message_size) || (len==-1)){
		len = read (m_socket, (void *) &message[got_so_far], message_size - got_so_far);
		if (len > 0) {
			got_so_far = got_so_far + len;
		}
	}
   return 0;
}
////////////////////////////////////////////////////////////////////////////
int DLLSocket::send_block (char *message, int message_size) {
	write (m_socket, message, message_size);
   return 0;
}
////////////////////////////////////////////////////////////////////////////
int DLLSocket::show_stat () {
	cout << "______________________________________\n";
   cout << "DLL Stats:\n";
	cout << "  Method: Selective Repeat\n";
	cout << "  CRC: CRC-32\n";
	cout << "  Sender/receiver window size: "<< WINDOW_SIZE <<"\n";
	cout << "  Frame data size: "<< FRAME_DATA_SIZE <<"\n";
   cout << "  SENDING MODULE:\n";
	cout << "    Total data sent: " << stat_data_sent <<"\n";
   cout << "    Total frames sent: " << stat_frames_sent<<"\n";
   cout << "    Frame retransmissions: " << stat_resend <<"\n";
   cout << "    Retransmissions  from timeouts: " << stat_timeouts <<"\n";
   cout << "    Retransmissions  from NACKs: " << stat_resend_bynack <<"\n";
	cout << "    Timeout period: "<< TIMEOUT_UNIT * TIMEOUT <<" [sec]\n";
	cout << "    ACKs received: " << stat_acks_recv <<"\n";
	cout << "    NACKs received: " << stat_nacks_recv <<"\n";
	cout << "    Frames to be dropped: %" << m_errorpct << "\n";
	cout << "    Frames to be corrupted: %" << m_crc_errorpct << "\n";
   cout << "  RECEIVING MODULE:\n";
	cout << "    Total data received: " << stat_data_recv <<"\n";
   cout << "    Total frames received: " << stat_frames_recv <<"\n";
   cout << "    Frame CRC failures: " << stat_bad_crcs <<"\n";
	cout << "    ACKs sent: " << stat_acks_sent <<"\n";
	cout << "    NACKs sent: " << stat_nacks_sent <<"\n";
	cout << "______________________________________\n";
   sleep (5);
   return 0;
}
////////////////////////////////////////////////////////////////////////////
int DLLSocket::reset_stat () {
	   stat_datarate = 0;
		stat_data_sent = 0;
		stat_frames_sent = 0;
		stat_resend = 0;
      stat_resend_bynack = 0;
		stat_timeouts = 0;
		stat_nacks_recv = 0;
		stat_acks_recv = 0;
		stat_frames_recv = 0;
      stat_data_recv = 0;
		stat_bad_crcs = 0;
		stat_acks_sent = 0;
		stat_nacks_sent = 0;
      return 0;
}
////////////////////////////////////////////////////////////////////////////
void   DLLSocket::recv (void *message, int message_size) {
		// This function takes message_size bytes of data from the
		// received framed buffer (if they are available and in correct
		// order) and copies it into the message. If the data is not available
		// in the incoming frames buffer, it waits for socket activity.
		// When a frame comes in, it processes it using selective-repeat.
		// (This function is responsible for sending Ack and Nacks)
		// If it's the lowest frame seq.# acceptable, it passes it's data to
		// the message.

		int c,len,filled, bytesleft,framesize;
		Ack AckOrNAck;  // Used to send Acknowledgments
		Frame tmpFrame; // Received frame
		char  *pt1;     // Frame data pointer
		fd_set bvfdRead; // Used with the SELECT statement
		filled = 0; // number of bytes already sent to *message

		while (filled < message_size) {
		   bytesleft = message_size - filled;

		   // See if we have the first frame we expect, and if we find it,
		   // we copy all or part of it's data into the message.
		   // If we copied all of its data, we free up the buffer space
		   // and move the window up. If we used only part of the frame
		   // data we need to mark that in the frame and leave the frame
		   // in the buffer, so that we can use it's data on the next call
		   // to recv():
			int pos;
         pos = -1;
		   while ((incoming_frames_buffer[recv_lowest_seq].isused==1) && (bytesleft > 0)){
            pos = recv_lowest_seq;

				// The lowest frame is in the buffer and ready to be passed to
            // the leyer above. Before we do that however, we need to
            // to send Ack for it:

            // The seq. number of the Ack depends on how many
            // frames are ready to go in the buffer. The seq. number
            // of the Ack will be equal to the highers corretly received
            // frame. In other words, if we correcly received frames:
            // 0,1,2,3,5,6 and the lowest expected # is 0, then we send
            // Ack with sq# of 3. The Sender will know that frames 0..3
            // were received

	   		// We need to find out how many bytes of data there in the frame:
				framesize = incoming_frames_buffer[recv_lowest_seq].dataend - incoming_frames_buffer[recv_lowest_seq].datastart;
				pt1 = (char *) &incoming_frames_buffer[recv_lowest_seq].data[
            				incoming_frames_buffer[recv_lowest_seq].datastart  ];

		   		// The lowest expected frame is in .. let's get it
				if (bytesleft >= framesize) {
				   // We will need to take all the data from this frame
					bcopy (pt1,&message[filled],framesize);

					// Mark the frame unused and move the window up:
					incoming_frames_buffer[recv_lowest_seq].isused = 0;
					incoming_frames_buffer[recv_lowest_seq].nAcked = 0;
					recv_lowest_seq = inc(recv_lowest_seq);
               recv_higher_seq = inc(recv_higher_seq);

					bytesleft = bytesleft - framesize;
					filled = filled + framesize;
				} else {
					// We don't need all the data from this frame. We take what we need
					// mark that some data was taken from the frame and leave the
					// frame in there.

					bcopy (pt1,&message[filled],bytesleft);

					incoming_frames_buffer[recv_lowest_seq].datastart = incoming_frames_buffer[recv_lowest_seq].datastart + bytesleft;
					// We will be done with this message:
					bytesleft = 0;
					filled = message_size;
				}
		   }

         if (pos != -1) {
           		// There was at least one frame that needs to be
               // Acked.
					// Now we just send an Ack for the SQ# one less then
               // the position after the last frame that needs to
               // Acked:
               stat_acks_sent++;
					AckOrNAck.command = cmdAck;
					AckOrNAck.seq     = pos;
					send_block((char *) &AckOrNAck,ACK_LEN);
         }

		   // Check if we finished filling up the message.
		   if (bytesleft == 0) {
		   		// Yes we are. We can just quit the function
				return;
		   } else {
		   		// No, we need to receive more frames.
				// We need to wait for a frame to come in, and then go back up to
				// put it in the message.

		   		// Wait till data comes in on the sock socket (using SELECT function):

		   		// Clear the set
		   		FD_ZERO(&bvfdRead);
		   		// set the socket descriptor in the set
		   		FD_SET(m_socket, &bvfdRead);   /* socket for the server */
		   		select (m_socket+1, &bvfdRead, 0, 0, 0);

				// Read the frame:
				recv_block((char *) &tmpFrame, FRAME_LEN);

            stat_data_recv += tmpFrame.dataend;
            stat_frames_recv++;
				// Check if the frame is within the window:
				if (between (recv_lowest_seq,tmpFrame.seq,recv_higher_seq) ) {
            	// Yes, Check the checksum:

	          	int crc_checksum = crc32(0, (byte *) &tmpFrame.data[0],FRAME_DATA_SIZE);
					if (tmpFrame.crc32check == crc_checksum) {
						// Checksum is correct, mark that this frame was
                  // received corretly, and the it's ready to be included
                  // in the next Ack

						// We also want to plug in that frame into our buffer,
						// and mark it used:
						bcopy ((char *) &tmpFrame, (char *) &incoming_frames_buffer[tmpFrame.seq],FRAME_LEN);
                  incoming_frames_buffer[tmpFrame.seq].nAcked = 0;
						incoming_frames_buffer[tmpFrame.seq].isused = 1;
					} else {
						// Checksum is not correct, send a NAck for that frame
                  // if we didn't already:
                  stat_bad_crcs++;
                  if (incoming_frames_buffer[tmpFrame.seq].nAcked == 0) {
							incoming_frames_buffer[tmpFrame.seq].nAcked = 1;
							incoming_frames_buffer[tmpFrame.seq].isused = 0;
							AckOrNAck.command = cmdAckNot;
							AckOrNAck.seq     = tmpFrame.seq;
							send_block((char *) &AckOrNAck,ACK_LEN);
                     stat_nacks_sent++;
						}
					}
				} else {
					// No, the frame that came in is not expected at all.
					// We send a NACK with seq # lower then the frame one less
					// then exptected:
               stat_nacks_sent++;
					AckOrNAck.command = cmdAckNot;
					AckOrNAck.seq     = recv_lowest_seq - 1;
					send_block((char *) &AckOrNAck,ACK_LEN);
				}
		   }
		   // Here we loop back up to fill the message with the frames.
		}
}
////////////////////////////////////////////////////////////////////////////
void   DLLSocket::send (void *message, int message_size) {
int aaa;
		// Takes the message, divides it into frames and sends it.
		// When the window is full of outstanding frames, or if there are
      // no more frames to send, it checks for incoming Acks / Nacks

		// frames - number of frames that will need to be constructed:
		int frames = message_size / FRAME_DATA_SIZE;
		if (frames * FRAME_DATA_SIZE < message_size) frames++;

		// Here we send all the frames:
		int count=0;  // number of frames sent
		while ((count < frames) || (size != 0)) {
		  // Wait till there is space availabe in the buffer:
		  while ((size == WINDOW_SIZE) || ((count == frames) && (size != 0))) {
        	 // Buffer is full, so we just have to wait for Acks.
			 AckNAckReceiver();
        }

        if (count < frames) {
		    int c=0, frame_size;

		    // Figure out what the size of the frame data will be
		    if (count == (frames - 1)) {
			  // Last frame to be send (within the message)
			  frame_size = message_size - (frames - 1)* FRAME_DATA_SIZE;
		    } else {
			  // Not the last frame, so it has to be full size:
			  frame_size = FRAME_DATA_SIZE;
		    }
		    outgoing_frames_buffer[send_next_seq].datastart = 0;
		    outgoing_frames_buffer[send_next_seq].dataend   = frame_size;

		    // We copy the data into the buffer:
		    bcopy(&message[count * FRAME_DATA_SIZE],
		  			     &outgoing_frames_buffer[send_next_seq].data[0],
                        frame_size);

		    count++;

          // Mark stats that we will send more data
          stat_data_sent = stat_data_sent + frame_size;
			 stat_frames_sent++;

		    // Set up the frame sequence number.
		    outgoing_frames_buffer[send_next_seq].seq = send_next_seq;

		    // Set up the frame timeout
		    outgoing_frames_buffer[send_next_seq].timeout_counter = TIMEOUT;

          // Compute the CRC32:
          int crc_checksum = crc32(
          		0,
					(byte *) &outgoing_frames_buffer[send_next_seq].data[0]
               ,FRAME_DATA_SIZE);
          outgoing_frames_buffer[send_next_seq].crc32check = crc_checksum;

          // Final step:
          // Send the frame again:
          // We need to let the errors get in:
			 int original_crc = outgoing_frames_buffer[send_next_seq].crc32check;
          // Mess up the Check-sum:
			 if (m_crc_errorpct >= get_random_percent()) outgoing_frames_buffer[send_next_seq].crc32check++;
          // Drop the frame if we get lucky:
          if (m_errorpct <= get_random_percent()) send_block((char *) &outgoing_frames_buffer[send_next_seq],FRAME_LEN);
          outgoing_frames_buffer[send_next_seq].crc32check = original_crc;

		    // We mark the slot as used:
		    outgoing_frames_buffer[send_next_seq].isused = 1;

		    send_next_seq = inc(send_next_seq);
  		    size++;
		  }
		}
	}

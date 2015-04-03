#!/usr/local/bin/perl -wI./
#
#Created 9/25/97 by Anthony Ball
#ant@wpi.edu

@dude = split("\n", `ipcs`);

foreach (@dude) {
  $type = "-q" if /Message Queues/i;  
  $type = "-m" if /Shared Memory/i;  
  $type = "-s" if /Semaphores/i;

  s/\s+/ /ig;  
  @data = split(" ", $_);
  if($data[4] && $data[4] =~ /$ENV{USER}/) {
    system("ipcrm $type $data[1]");
    print "IPC $data[1] of type ";
    print "Message Queue" if $type eq "-q";
    print "Shared Memory" if $type eq "-m";
    print "Semaphore" if $type eq "-s";
    print " has been removed.\n";
  }
}

print "Done.\n";


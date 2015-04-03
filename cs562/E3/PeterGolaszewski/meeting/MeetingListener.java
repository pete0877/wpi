// Class        : Scheduler
// Author       : Peter Golaszewski (peterg@wpi.edu)
// Version      : 1.0
// Description  : Implementor of this interface is obligated to perform UI actions when alarmed.

package meeting;

public interface MeetingListener {
    
    // METHODS:
    
    // this method will be called by the MeetingBean when a meeting reminder is up
    public void alarm(Meeting meeting);    
    
}    

// Class        : MeetingBean
// Author       : Peter Golaszewski (peterg@wpi.edu)
// Version      : 1.0
// Description  : Keeps track of all scheduled meetings and notifies listeners about
//                  upcoming meetings. This class also checks for conflicts between
//                  meetings.
// Known Problems: At this point removeConflictingMeetings() ignores the date-part of 
//                  the end-date parameter. Only the time-part is being considered.
//                  This limitation can be fixed with a little bit more programming.

package meeting;

import java.io.Serializable;
import java.io.InputStream;
import java.awt.Component;
import java.util.Vector;
import java.util.Enumeration;
import java.lang.Runnable;

public class MeetingBean extends Component implements Serializable, Runnable {

    // ATTRIBUTES:
    Vector m_meetings;
    Vector m_listeners;   
    int m_lastID;
    Runner m_runner; // since we are extending Component, we can't extend Thread. So we will use some help.
    
    
    // METHODS:

    // default constructor
    public MeetingBean () {
        m_meetings = new Vector();
        m_listeners = new Vector();
        m_lastID = 0;
        m_runner = new Runner();
        m_runner.addApp(this);
        m_runner.setSleep(1000 * 60 * 5);
        m_runner.start();        
    }   
    
    // adds another meeting to the list, but first verifies that the 
    // new meeting does not run into conflicts with any of the 
    // existing meetings. If it does, Expection instance is thrown
    public void addMeeting(Meeting the_meeting) throws Exception {
        // first we need to check if there are any conflicts:
        if (isInConflict(the_meeting)) throw new Exception("The new meeting is in conflict with an existing meeting.");            
        // here we are fine, no conflicts
        Meeting tmpMeeting = new Meeting(the_meeting);
        m_lastID++;
        tmpMeeting.setReferenceID(m_lastID);
        m_meetings.addElement(tmpMeeting);
    }
    
    // returns all meetings that are scheduler for given day. The time part of the 
    // parameter is ignored.
    public Enumeration getMeetings(MeetingDate day) {
        // we create a dummy meeting to check which meeting colide with it:
        MeetingDate start_of_day = new MeetingDate(day);
        MeetingDate end_of_day = new MeetingDate(day);
        start_of_day.setHour(0);
        start_of_day.setMinute(0);
        end_of_day.setHour(23);
        end_of_day.setMinute(59);
        Meeting dummyMeeting = new Meeting ();
        dummyMeeting.setStartDate(start_of_day);
        dummyMeeting.setEndDate(end_of_day);
        dummyMeeting.setReoccurence(0); // no reoccurence
        
        // check all existing meetings
        Vector tmpVect = new Vector();
        Enumeration enumList = m_meetings.elements();
        while (enumList.hasMoreElements()) {            
            Meeting tmpMeeting = (Meeting) enumList.nextElement();
            if (tmpMeeting.isInConflict(dummyMeeting)) {
                // we know that tmpMeeting takes place during this day
                Meeting copyMeeting = new Meeting(tmpMeeting);
                tmpVect.addElement(copyMeeting);
            }
        }
        return tmpVect.elements();   
    }
    
    // returns all meetings 
    public Enumeration getAllMeetings() {
        Vector tmpVect = new Vector();
        Enumeration enumList = m_meetings.elements();
        while (enumList.hasMoreElements()) {            
            Meeting copyMeeting = new Meeting((Meeting) enumList.nextElement());
            tmpVect.addElement(copyMeeting);
        }
        return tmpVect.elements();   
    }    
    
    // Removes a meeting from the schedule
    public void removeMeeting(Meeting the_meeting) {
        Enumeration enumList = m_meetings.elements();
        while (enumList.hasMoreElements()) {
            Meeting tmpMeeting = (Meeting) enumList.nextElement();
            if (the_meeting.getReferenceID() == tmpMeeting.getReferenceID()) {
                m_meetings.removeElement(tmpMeeting);
                return;
            }
        }        
    }
    
    // removes meetings that come into conflict with parameter specificatios
    //  startdate - start of the date/time range
    //  enddate - end of the time range (the date part is ignored)
    //  all_instances - if a conflicting meeting is reoccuring, should the entire pattern be cancelled?
    //          if this parameter is false, only an exception date will be given to the reoccuring meeting
    public void removeConflictingMeetings(MeetingDate start, MeetingDate end, boolean all_instances) throws Exception {        
        // error check
        if (!start.before(end)) { throw new Exception("Incorrect date range was speficied"); }

        // create a dummy meeting instance
        MeetingDate new_startdate = new MeetingDate(start);
        MeetingDate new_enddate = new MeetingDate(end);
        
        new_enddate.setYear(new_startdate.getYear());
        new_enddate.setMonth(new_startdate.getMonth());
        new_enddate.setDay(new_startdate.getDay());
        Meeting tmpMeeting = new Meeting();
        tmpMeeting.setStartDate(new_startdate);
        tmpMeeting.setEndDate(new_enddate);
        tmpMeeting.setReoccurence(0);
        
        // for each of the scheduled meetings, find out if the dummy 
        // meeting runs into a conflict:
        Enumeration enumList = m_meetings.elements();
        while (enumList.hasMoreElements()) {
            Meeting existingMeeting = (Meeting) enumList.nextElement();
            
            if (existingMeeting.isInConflict(tmpMeeting)) {
                // yes, this meeting needs to be deleted.
                // check if the meeting is reoccuring:
                if (existingMeeting.getReoccurence() != 0) {
                    // yes, it is. 
                    if (all_instances) 
                        // delete all instances
                        m_meetings.removeElement(existingMeeting);
                    else 
                        // just create an exception
                        existingMeeting.addException(new_startdate);
                } else {
                    // delete this non-reoccuring meeting
                    m_meetings.removeElement(existingMeeting);
                }
            } 
        }
    }
    
    public void updateMeeting(Meeting new_meeting) throws Exception {
        Enumeration enumList = m_meetings.elements();
        while (enumList.hasMoreElements()) {
            Meeting existingMeeting = (Meeting) enumList.nextElement();
            if (new_meeting.getReferenceID() == existingMeeting.getReferenceID()) {
                m_meetings.removeElement(existingMeeting);
                // check if the new meeting colides with any of the existing meetings
                if (isInConflict(new_meeting)) {           
                    // bring back the original meeting and throw an exception
                    m_meetings.addElement(existingMeeting);
                    throw new Exception("This runs into a conflict with an existing meeting.");
                } else {
                    // everything is fine
                    m_meetings.addElement(new_meeting);
                }
                return;
            }
        }
    }
    
    // returns true if the meeting is in conflict with any of the existing meetings
    public boolean isInConflict(Meeting the_meeting) {
        Enumeration enumList = m_meetings.elements();
        while (enumList.hasMoreElements()) {
            Meeting tmpMeeting = (Meeting) enumList.nextElement();
            if (the_meeting.isInConflict(tmpMeeting)) {
                return true;
            }
        }      
        return false;
    }
    
    // adds a meeting alarm listener to the listeners list
    public void addListener(MeetingListener listener) {
        m_listeners.addElement(listener);
    }
    
    // removes particular listener from the list
    public void removeListener(MeetingListener listener) {
        Enumeration enumList = m_listeners.elements();
        while (enumList.hasMoreElements()) {
            MeetingListener tmpMeetingListener = (MeetingListener) enumList.nextElement();
            if (listener == tmpMeetingListener) {
                m_listeners.removeElement(tmpMeetingListener);
                return;
            }
        }
    }
    
    // checks if any of the meetings need to be alarmed, and if there are,
    // it alarms all the listeners
    public void run() {        
        Enumeration meetingsList = m_meetings.elements();
        while (meetingsList.hasMoreElements()) {
            Meeting tmpMeeting = (Meeting) meetingsList.nextElement();
            if (tmpMeeting.needsAlarm()) {
                // message all listeners
                tmpMeeting.wasAlarmed(new MeetingDate());
                Enumeration listnersList = m_listeners.elements();
                while (listnersList.hasMoreElements()) {
                    MeetingListener tmpMeetingListener = (MeetingListener) listnersList.nextElement();                        
                    tmpMeetingListener.alarm(tmpMeeting);
                }    
            }
        }     
    }

    // returns object serialization
    public Serializable Serialize() {
        return m_meetings;
    }        
}

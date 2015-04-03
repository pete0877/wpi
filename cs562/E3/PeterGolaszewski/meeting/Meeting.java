// Class        : Meeting
// Author       : Peter Golaszewski (peterg@wpi.edu)
// Version      : 1.0
// Description  : Stores all meeting-related data

package meeting;

import java.lang.String;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Date;
import java.io.Serializable;

public class Meeting implements Serializable {
    // NOTE: each meeting can only span through one day, 
    // so the m_endDate is only used to keep track of the end time.
    // In future, this limitation can be dropped.
    
    // ATTRIBUTES
    protected int m_ID;
    protected MeetingDate m_startDate;  // start date + time
    protected MeetingDate m_endDate;    // end time
    protected MeetingDate m_lastAlarm;    // end time
    protected int m_reminder;
    protected int m_reoccurence;
    protected String m_description;    
    protected Vector m_exceptions;
    
    // METHODS        
    
    // default constuctor
    public Meeting () {        
        m_ID = 0;
        m_startDate = new MeetingDate();
        m_endDate = new MeetingDate();
        m_reminder = 0;
        m_reoccurence = 0;
        m_description = new String();
        m_exceptions = new Vector();
        m_lastAlarm = null;
    }    
    
    // copy constructor
    public Meeting (Meeting the_meeting) {
        m_ID = the_meeting.getReferenceID();
        m_startDate = new MeetingDate(the_meeting.getStartDate());
        m_endDate = new MeetingDate(the_meeting.getEndDate());
        m_reminder = the_meeting.getReminder();
        m_reoccurence = the_meeting.getReoccurence();
        m_description = new String(the_meeting.getDescription());        
        m_exceptions = new Vector();
        setExceptions(the_meeting.getExceptions());        
        m_lastAlarm = the_meeting.whenWasAlarmed();
    }
    
    // following are genetic data member access functions:
    
    public MeetingDate getStartDate () {
        return new MeetingDate(m_startDate);
    }
    
    public MeetingDate getEndDate () {
        return new MeetingDate(m_endDate);
    }
    
    public void setStartDate (MeetingDate startdate) {
        m_startDate = new MeetingDate(startdate);
        // synchronize the start date and the end date:
        m_endDate.setYear(m_startDate.getYear());
        m_endDate.setMonth(m_startDate.getMonth());
        m_endDate.setDay(m_startDate.getDay());
        // since we are changin the start date, it might be a time for a alarm
        m_lastAlarm = null;
    }
    
    public void setEndDate (MeetingDate enddate) {
        m_endDate = new MeetingDate(enddate);
        // synchronize the start date and the end date:
        m_endDate.setYear(m_startDate.getYear());
        m_endDate.setMonth(m_startDate.getMonth());
        m_endDate.setDay(m_startDate.getDay());
        // since we are changin the end date, it might be a time for a alarm
        m_lastAlarm = null;
    }
    
    public String getDescription() {
        return new String(m_description);   
    }
    
    public void setDescription(String description) {
        m_description = new String(description);
    }
    
    public int getReminder() {
        return m_reminder;
    }
    
    public void setReminder(int reminder) {
        m_reminder = reminder;
        // since we are changin the reminder, it might be a time for a alarm
        m_lastAlarm = null;
    }
    
    public int getReoccurence() {
        return m_reoccurence;
    }
    
    public void setReoccurence(int reoccurence) {
        m_reoccurence = reoccurence;
        // since we are changin reoccurence, it might be a time for a alarm
        m_lastAlarm = null;
    }
    
    public Enumeration getExceptions() {
        // create copies of current expection dates
        Vector exceptions = new Vector();
        Enumeration tmpList = m_exceptions.elements();
        while (tmpList.hasMoreElements()) {
            MeetingDate tmpDate = new MeetingDate((MeetingDate) tmpList.nextElement());
            exceptions.addElement(tmpDate);
        }
        return exceptions.elements();
    }
    
    public void setExceptions(Enumeration exceptions) {
        // create a copy of the new expections list
        m_exceptions = new Vector();
        while (exceptions.hasMoreElements()) {            
            MeetingDate tmpDate = new MeetingDate ((MeetingDate) exceptions.nextElement());
            m_exceptions.addElement(tmpDate);
        }         
        // since we are changin expections it might be a time for a alarm
        m_lastAlarm = null;
    }
    
    public void addException(MeetingDate exception) {
        m_exceptions.addElement(new MeetingDate(exception));        
    }
    
    public void removeException(MeetingDate exception) {
        // we need to find the expection in the list
        Enumeration exceptions = m_exceptions.elements();
        while (exceptions.hasMoreElements()) {
            MeetingDate meetingdate = (MeetingDate) exceptions.nextElement();
            if (meetingdate.equals(exception)) m_exceptions.removeElement(meetingdate);
        }   
        // since we are removing an expection it might be a time for a alarm
        m_lastAlarm = null;
    }
    
    public int getReferenceID() {
        return m_ID;
    }    
    
    public void setReferenceID(int ID) {
        m_ID = ID;
    }    

    // returns true if the meeting is in conflict with the current meeting object
    public boolean isInConflict(Meeting the_meeting) {                
        // first case: we need to check the time. If the times
        // do not overlap, then there is no conlict:                
        MeetingDate startdate1 = new MeetingDate(m_startDate);
        MeetingDate enddate1 = new MeetingDate(m_endDate);
        MeetingDate startdate2 = new MeetingDate(the_meeting.getStartDate());
        MeetingDate enddate2 = new MeetingDate(the_meeting.getEndDate());  
        
        // normalize the dates, but keep the times the same:
        startdate1.setYear(2000);
        startdate2.setYear(2000);        
        enddate1.setYear(2000);
        enddate2.setYear(2000);        
        startdate1.setMonth(1);
        startdate2.setMonth(1);        
        enddate1.setMonth(1);
        enddate2.setMonth(1);        
        startdate1.setDay(1);
        startdate2.setDay(1);        
        enddate1.setDay(1);
        enddate2.setDay(1);
        
        if  (
            (enddate1.before(startdate2) || enddate1.equals(startdate2)) ||
            (enddate2.before(startdate1) || enddate2.equals(startdate1))
            ) return false; // no conflict
        
        // at this point we know that the two meetings happen at the same time of 
        // the day but we don't know if the dates are the same.
        
        // now we need to check reoccurence:
        
        if (the_meeting.getReoccurence() == 0 && m_reoccurence == 0) {
            // case: neither of the meetings is reoccuring so all we have 
            // to do is to check the day
            startdate1 = new MeetingDate(m_startDate);
            enddate1 = new MeetingDate(m_endDate);
            startdate2 = new MeetingDate(the_meeting.getStartDate());
            enddate2 = new MeetingDate(the_meeting.getEndDate());  
            // normalize the time, but keep the date the same:
            startdate1.setHour(1);
            startdate2.setHour(1);
            enddate1.setHour(1);
            enddate2.setHour(1);
            startdate1.setMinute(1);
            startdate2.setMinute(1);       
            enddate1.setMinute(1);
            enddate2.setMinute(1);   
            if  (enddate1.before(startdate2) || enddate2.before(startdate1)) 
                return false; // no conflict            
            else 
                return true; // yes, there is a conflict
        }
        
        if ((the_meeting.getReoccurence() != 0 && m_reoccurence != 0)) {
            // case: both of the the meetings is reoccuring
            // first, in order for two reoccurin meetings not to run
            // into conflict, they have to have different start dates:
            startdate1 = new MeetingDate(m_startDate);
            enddate1 = new MeetingDate(m_endDate);
            startdate2 = new MeetingDate(the_meeting.getStartDate());
            enddate2 = new MeetingDate(the_meeting.getEndDate());  
            // normalize the times, but keep the dates the same:
            startdate1.setHour(1);
            startdate2.setHour(1);
            enddate1.setHour(1);
            enddate2.setHour(1);
            startdate1.setMinute(1);
            startdate2.setMinute(1);       
            enddate1.setMinute(1);
            enddate2.setMinute(1);   
            if  (startdate1.equals(startdate2)) return true; // pattners cannot start the same day
            
            // here we know that meeting patterns start on different days,
            // all we have to do is to make sure that one pattern always
            // falls between instances of the other pattern:
            int longerPattern;
            int shorterPattern;
            if (the_meeting.getReoccurence() > m_reoccurence) {
                longerPattern = the_meeting.getReoccurence();
                shorterPattern = m_reoccurence;
            } else {
                longerPattern = m_reoccurence;
                shorterPattern = the_meeting.getReoccurence();
            }
            if (shorterPattern==1) return true;
            if (longerPattern % shorterPattern != 0) return true; // patterns will conflict
            
            return false; 
        }

        // case: only one of the meetings is reoccuring
        Meeting meeting_rec; // the reoccuring meeting
        Meeting meeting_non_rec; // the non-reoccuring meeting
        if (the_meeting.getReoccurence() != 0) {
            meeting_rec = the_meeting;
            meeting_non_rec = this;
        } else {
            meeting_rec = this;
            meeting_non_rec = the_meeting;   
        }
        
        // now we need to check for two cases:
        // 1. the non-reoccuring meeting is on a date that is among the 
        //      exceptions of the reoccuring meeting. In this case, there
        //      is no conflict
        // 2. the non-reoccuring meeting is on a date that is not reached
        //      the the reoccurence of the other meeting        
        // If neither of the above is true, then we have a conflict:

        MeetingDate non_rec_date = new MeetingDate(meeting_non_rec.getStartDate());        
        // normalize the time:
        non_rec_date.setHour(1);
        non_rec_date.setMinute(1);                
        
        // check for case 1:
        Enumeration dates = meeting_rec.getExceptions();
        while (dates.hasMoreElements()) {
            MeetingDate excep_rec_date = new MeetingDate ((MeetingDate) dates.nextElement());
            // normalize the time:
            excep_rec_date.setHour(1);
            excep_rec_date.setMinute(1);
            if (excep_rec_date.equals(non_rec_date)) 
                // the non-reoccuring meeting is among exceptions of the reoccuring meeting
                return false; 
        }
        
        // check for case 2:
        
        // first we need to check if the non-reoccuring meeting is scheduled before the 
        // reoccuing meeting even begins. If that's the case, we cannot have a conflict:
        MeetingDate rec_date = new MeetingDate (meeting_rec.getStartDate()); // create a copy 
        // normalize the time:
        rec_date.setHour(1);
        rec_date.setMinute(1);
        if (non_rec_date.before(rec_date)) return false; // no conflict
        
        // now we need to check if the two meetings start on the same day:
        if (non_rec_date.equals(rec_date)) return true; // there is a conflict
        
        // now we need to check if the reoccurence hits our non-reoccuring date. In order
        // to do this, we have to know how many days apart the two dates are:
        int numDaysApart = 0;
        while (!non_rec_date.equals(rec_date)) {
            numDaysApart++;
            
            // FIX: for some reason rolling the days changes the time
            // This might be a bug with the Calendar class or it mighte
            // be the spring-time change thingy
            
            non_rec_date.setHour(1);
            non_rec_date.setMinute(1);
            rec_date.setHour(1);
            rec_date.setMinute(1);
            
            String tmp1 = rec_date.toString();
            String tmp2 = non_rec_date.toString();
            rec_date = rec_date.next(1);            
            
        }
        // if the number of days that the two dates are appart divides by the reoccurence
        // with reminder of 0, we know that there is conflic. Otherwise we know that we do
        // not have a conflict:
        if ((numDaysApart % meeting_rec.getReoccurence()) == 0) return true; 
        
        return false; // no conflicts were detected        
    }          

    // return true if the current meeting runs over specified meeting date
    public boolean runsOver(MeetingDate date) {
        Meeting the_meeting = new Meeting();
        the_meeting.setStartDate(date);
        the_meeting.setEndDate(date);
        the_meeting.setReoccurence(0);        
        return isInConflict(the_meeting);
    }        
    
    // returns simple representation of the current meeting
    public String toString() {    
        String result = "";        
        result = result + "MEETING #: " + (new Integer(m_ID)).toString() + "\n";
        result = result + "   starts      : " + m_startDate.toString() + "\n";
        result = result + "   ends        : " + m_endDate.toString() + "\n";  
        result = result + "   reoccurence : " + (new Integer(m_reoccurence)).toString() + " days\n";                
        result = result + "   description : " + m_description + "\n";        
        result = result + "   reminder    : " + (new Integer(m_reminder)).toString() + " min\n";
        result = result + "   exceptions  : \n";
        Enumeration exceptions = m_exceptions.elements();
        while (exceptions.hasMoreElements()) {
            MeetingDate meetingdate = (MeetingDate) exceptions.nextElement();
            result = result + "        " + meetingdate.toString() + "\n";
        }        
        return result;
    }    
    
    public boolean needsAlarm() {        
        MeetingDate now;
        MeetingDate lastAlarm;
        // find out when was the last alarm issued
        if (m_lastAlarm != null) {
            // alarm was issued at some point. we need to check it if was today:
            now = new MeetingDate(); 
            // normalize the time:
            now.setHour(1);
            now.setMinute(1);
            lastAlarm = new MeetingDate(m_lastAlarm);
            now.setHour(1);
            now.setMinute(1);
            lastAlarm.setHour(1);
            lastAlarm.setMinute(1);
            if (now.equals(lastAlarm)) return false; // last alarm was sent sometime today. we can be sure
            // alarm was issued some time in the past, but not today            
        }
        // alarm was never issued or it was issued at least one day ago
        // all we have check is if we need to issue for today within the 
        // reminder amount of time.
        MeetingDate later_date = new MeetingDate(); 
        later_date = later_date.nextMinute(m_reminder);
        return (runsOver(later_date));        
    }
    
    public void wasAlarmed(MeetingDate when) {
        m_lastAlarm = new MeetingDate(when);        
    }    
    
    public MeetingDate whenWasAlarmed() {
        if (m_lastAlarm==null) 
            return null; 
        else
            return new MeetingDate(m_lastAlarm);        
    } 
    
    // returns object serialization
    public Serializable Serialize() {
        Vector result = new Vector();
        result.addElement(new Integer(m_ID));
        result.addElement(m_startDate);
        result.addElement(m_endDate);
        result.addElement(m_lastAlarm);      
        result.addElement(new Integer(m_reminder));
        result.addElement(new Integer(m_reoccurence));
        result.addElement(m_description);        
        result.addElement(m_exceptions);            
        return result;
    }    

    
}    

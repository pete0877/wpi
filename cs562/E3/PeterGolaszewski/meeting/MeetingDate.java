// Class        : MeetingDate
// Author       : Peter Golaszewski (peterg@wpi.edu)
// Version      : 1.0
// Description  : Keeps track of date and time down to minutes.

package meeting;

import java.util.Calendar;
import java.util.Vector;
import java.io.Serializable;

public class MeetingDate implements Serializable {

    // ATTRIBUTES:
    protected Calendar m_date;

    // METHODS:
    
    // default consturctor - sets date/time to now:
    public MeetingDate () {
        m_date = Calendar.getInstance();        
        // reset the seconds / mseconds count
        m_date.set(m_date.SECOND, 0);
        m_date.set(m_date.MILLISECOND, 0);
    }

    // copy contructor
    public MeetingDate (Calendar date) {
        this();
        if (date==null) { return; }
        m_date = Calendar.getInstance();
        m_date.setTime(date.getTime());
        // reset the seconds / mseconds count
        m_date.set(m_date.SECOND, 0);
        m_date.set(m_date.MILLISECOND, 0);
    }
    
    // copy contructor
    public MeetingDate (MeetingDate meetingdate) {
        this();
        if (meetingdate==null) { return; }
        Calendar tmpDate = meetingdate.getDate();        
        m_date = Calendar.getInstance();
        m_date.setTime(tmpDate.getTime());
        // reset the seconds / mseconds count
        m_date.set(m_date.SECOND, 0);
        m_date.set(m_date.MILLISECOND, 0);
    }    
    
    // constructor will all parameters
    public MeetingDate (int y, int m, int d, int h, int min) {        
        m_date = Calendar.getInstance();
        m_date.set(y, m, d, h, m);
    }
    
    // following are all genetic data-access methods:
    
    public void setDate(Calendar date) {
        m_date.setTime(date.getTime());
        // reset the seconds / mseconds count
        m_date.set(m_date.SECOND, 0);
        m_date.set(m_date.MILLISECOND, 0);
    }    
    
    public Calendar getDate() {
        return m_date;
    }

    public int getYear () {
        return m_date.get(m_date.YEAR);
    } 
    
    public int getMonth () {        // 0 .. 11
        return m_date.get(m_date.MONTH);
    }
    
    public int getDay () {            // 1 .. 31
        return m_date.get(m_date.DAY_OF_MONTH);
    }
    
    public int getHour () {         // 0 .. 23
        return m_date.get(m_date.HOUR_OF_DAY);
    }
    
    public int getMinute () {     // 0 .. 59    
        return m_date.get(m_date.MINUTE);
    }

    public void setYear (int y) {
        m_date.set(m_date.YEAR, y);
    }
    
    public void setMonth (int m) {     
        m_date.set(m_date.MONTH, m);
    }
    
    public void setDay (int d) {
        m_date.set(m_date.DAY_OF_MONTH, d);
    }
    
    public void setHour (int h) {
        m_date.set(m_date.HOUR_OF_DAY, h);
    }
    
    public void setMinute (int m) {
        m_date.set(m_date.MINUTE, m);
    }

    // returns a new meeting date object that is # of days in the past
    public MeetingDate previous (int num) {        
        int currentHour = getHour();
        int currentMinute = getMinute();
        MeetingDate tmpDate = new MeetingDate(this);
        Calendar tmpCalendar = tmpDate.getDate();
        for (int a = 0; a < num; a++) tmpCalendar.roll(m_date.DAY_OF_YEAR, false);
        // we have to do this because the Calendar class changes
        // the hour when switching to spring/winter time
        tmpDate.setHour(currentHour); 
        tmpDate.setMinute(currentMinute);
        tmpDate.setDate(tmpCalendar);
        return tmpDate;
    }

    // returns a new meeting date object that is # of hours in the past
    public MeetingDate previousHour (int num) {
        MeetingDate tmpDate = new MeetingDate(this);
        Calendar tmpCalendar = tmpDate.getDate();
        for (int a = 0; a < num; a++) tmpCalendar.roll(m_date.HOUR_OF_DAY, false);        
        tmpDate.setDate(tmpCalendar);
        return tmpDate;
    }
    
    // returns a new meeting date object that is # of minutes in the past
    public MeetingDate previousMinute (int num) {
        // we have to do this manually because Calendar does not roll
        // hours when minutes are rolled over
        int hours   = num / 60;
        int minutes = num % 60;        
        // minute overflow check:
        MeetingDate tmpDate = new MeetingDate(this);        
        Calendar tmpCalendar = tmpDate.getDate();        
        minutes = tmpCalendar.get(tmpCalendar.MINUTE) - minutes;
        if (minutes < 0) {
            // need to roll one extra hour
            hours++;
            minutes = minutes + 60;
        }
        // roll hours
        tmpDate = tmpDate.previousHour(hours);
        // set the minutes
        tmpCalendar = tmpDate.getDate();       
        tmpCalendar.set(tmpCalendar.MINUTE, minutes);        
        tmpDate.setDate(tmpCalendar);
        return tmpDate;
    }

    // returns a new meeting date object that is # of days in the future
    public MeetingDate next (int num) {
        int currentHour = getHour();
        int currentMinute = getMinute();
        MeetingDate tmpDate = new MeetingDate(this);
        Calendar tmpCalendar = tmpDate.getDate();
        for (int a = 0; a < num; a++) tmpCalendar.roll(m_date.DAY_OF_YEAR, true);        
        // we have to do this because the Calendar class changes
        // the hour when switching to spring/winter time
        tmpDate.setHour(currentHour); 
        tmpDate.setMinute(currentMinute);
        tmpDate.setDate(tmpCalendar);
        return tmpDate;
    }

    // returns a new meeting date object that is # of hours in the future
    public MeetingDate nextHour (int num) {
        MeetingDate tmpDate = new MeetingDate(this);
        Calendar tmpCalendar = tmpDate.getDate();
        for (int a = 0; a < num; a++) tmpCalendar.roll(m_date.HOUR_OF_DAY, true);        
        tmpDate.setDate(tmpCalendar);
        return tmpDate;
    }
    
    // returns a new meeting date object that is # of minutes in the future
    public MeetingDate nextMinute (int num) {                
        // we have to do this manually because Calendar does not roll
        // hours when minutes are rolled over
        int hours   = num / 60;
        int minutes = num % 60;        
        // minute overflow check:
        MeetingDate tmpDate = new MeetingDate(this);        
        Calendar tmpCalendar = tmpDate.getDate();        
        minutes = minutes + tmpCalendar.get(tmpCalendar.MINUTE);  
        if (minutes >= 60) {
            // need to roll one extra hour
            hours++;
            minutes = minutes - 60;
        }
        // roll hours
        tmpDate = tmpDate.nextHour(hours);
        // set the minutes
        tmpCalendar = tmpDate.getDate();       
        tmpCalendar.set(tmpCalendar.MINUTE, minutes);        
        tmpDate.setDate(tmpCalendar);
        return tmpDate;
    }

    // returns whether the current MeetingDate object is between these two dates
    // (if current is the same as start or end, result will be false)
    public boolean between (MeetingDate start, MeetingDate end) {
        return (after(start) && before(end));
    }
    
    // returns whether the current MeetingDate object is before another date
    // (if current is the same as meetingdate, result will be false)
    public boolean before (MeetingDate meetingdate) {
        // we have to do this comparison manually because Calender
        // class uses global milisec count when it runs this function
        if (getYear() < meetingdate.getYear()) return true;
        if (getYear() > meetingdate.getYear()) return false;
        if (getMonth() < meetingdate.getMonth()) return true;
        if (getMonth() > meetingdate.getMonth()) return false;
        if (getDay() < meetingdate.getDay()) return true;
        if (getDay() > meetingdate.getDay()) return false;
        if (getHour() < meetingdate.getHour()) return true;
        if (getHour() > meetingdate.getHour()) return false;
        if (getMinute() < meetingdate.getMinute()) return true;
        if (getMinute() > meetingdate.getMinute()) return false;
        return false; // all the field (down to minutes) are equal
    }    
    
    // returns whether the current MeetingDate object is after another date
    // (if current is the same as meetingdate, result will be false)
    public boolean after (MeetingDate meetingdate) {
        // we have to do this comparison manually because Calender
        // class uses global milisec count when it runs this function
        return (!before(meetingdate) && !equals(meetingdate));
    }        
    
    // returns whether the current MeetingDate object equal to the passed object
    public boolean equals(MeetingDate meetingdate) {
        // we have to do this comparison manually because Calender
        // class uses global milisec count when it runs this function        
        return (         
                meetingdate.getYear() == getYear() &&
                meetingdate.getMonth() == getMonth() &&
                meetingdate.getDay() == getDay() &&
                meetingdate.getHour() == getHour() &&
                meetingdate.getMinute() == getMinute()
            );
    }        
    
    // returns its date+time representation as a String
    public String toString() {
        return toStringDate() + " " + toStringTime();
    }     
    
    // returns its time representation as a String
    public String toStringTime() {
        String result = "";        
        int hour = getHour() % 12;
        if (hour == 0) hour = 12;
        result = result + (new Integer(hour)).toString() + ":";
        result = result + (new Integer(getMinute())).toString() + " ";
        if (m_date.get(m_date.AM_PM)==0) 
            result = result + "AM";
        else 
            result = result + "PM";        
        return result;
    }     
    
    // returns its date representation as a String
    public String toStringDate() {
        String result = "";
        result = result + (new Integer(getMonth()+1)).toString() + "/";
        result = result + (new Integer(getDay())).toString() + "/";
        result = result + (new Integer(getYear())).toString();
        return result;
    }         
    
    // returns object serialization
    public Serializable Serialize() {
        Vector result = new Vector();
        result.addElement(new Integer(getYear()));
        result.addElement(new Integer(getMonth()));
        result.addElement(new Integer(getDay()));
        result.addElement(new Integer(getHour()));
        result.addElement(new Integer(getMinute()));
        return result;
    }    
                
}        

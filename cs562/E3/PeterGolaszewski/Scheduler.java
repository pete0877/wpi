// Class        : Scheduler
// Author       : Peter Golaszewski (peterg@wpi.edu)
// Version      : 1.0
// Description  : Applet that provides GUI and manages all application components.

import java.applet.Applet;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.*;
import java.io.*;

import gui.*;
import meeting.*;
import java.util.Calendar;
import java.util.Enumeration;

public class Scheduler extends Applet implements ActionListener, TableListener, MeetingListener {

  protected Meeting         m_currentSelection; // representation of the currently selected region
  protected MeetingDate     m_mondayDate;       // the monday's date 

  // The Five Java Bean components for this application
  protected MeetingBean      m_meetingBean;
  protected TableBean        m_calendarGrid;
  protected TableBean        m_cornerBox;
  protected DaysOfWeekTB     m_dateBar;
  protected HoursOfDayTB     m_timeBar;

  // Helper components
  protected Button           cancel;
  protected Button           about;
  protected Button           view;
  protected Button           make;
  protected Button           save;
  protected Button           prevMonth;
  protected Button           prevWeek;
  protected Label            weekLabel;
  protected Button           nextWeek;
  protected Button           nextMonth;
  protected Label            status;
    
  // protected TextArea         m_area_DEL;

  // Global Time
  private int numHalfHours = 20;
  private int numDays = 5;
  
  public void init() {
      init_gui();
      init_components();
      
      // establish the Monday date so that we can 
      // redraw the schedule
      m_mondayDate = new MeetingDate();
      Calendar tmpCal = m_mondayDate.getDate();      
      // roll down until we hit a monday
      while (tmpCal.get(tmpCal.DAY_OF_WEEK)!=2) {
        tmpCal.roll(tmpCal.DAY_OF_YEAR,false); 
      }      
      m_mondayDate.setDate(tmpCal);
  
      redrawView();

  
		//{{INIT_CONTROLS
		setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		setBackground(new java.awt.Color(255,255,221));
		setSize(600,600);		
		//}}
	}
  
  /**
   * Creates the user interface, composed of the TableBean and other
   * sundry buttons.
   */
  private void init_gui() {
    Label l;
    Font  font = new Font("Dialog", Font.PLAIN, 12);
    Font  headerFont = new Font("Dialog", Font.BOLD, 12);
    FontMetrics fm = getFontMetrics(font);

    Panel p = new Panel();
    p.setLayout (new BorderLayout());

    // Construct the beans first
    // ----------------------------------------
    m_calendarGrid = new TableBean (numDays, numHalfHours);    // center
    m_calendarGrid.setEditable (false);
    m_calendarGrid.setFont (font);
    m_calendarGrid.setColumnWidth (fm.stringWidth("XXXXXXXXXXXX"));
    m_calendarGrid.setRowHeight   (fm.getMaxAscent() + fm.getMaxDescent());
    m_calendarGrid.setForeground (Color.black);
    m_calendarGrid.setBackground (Color.white);
    

    m_dateBar = new DaysOfWeekTB (numDays);    // Column Headers
    m_dateBar.setEditable (false);
    m_dateBar.setFont (headerFont);
    m_dateBar.setColumnWidth (fm.stringWidth("XXXXXXXXXXXX"));
    m_dateBar.setRowHeight   (fm.getMaxAscent() + fm.getMaxDescent());
    m_dateBar.setForeground (Color.white);
    m_dateBar.setBackground (Color.gray);

    m_timeBar = new HoursOfDayTB (numHalfHours);   // Row Headers
    m_timeBar.setEditable (false);
    m_timeBar.setFont (headerFont);
    m_timeBar.setColumnWidth (fm.stringWidth(" xx:xx "));
    m_timeBar.setRowHeight   (fm.getMaxAscent() + fm.getMaxDescent());
    m_timeBar.setForeground (Color.white);
    m_timeBar.setBackground (Color.gray);

    m_cornerBox = new TableBean (1, 1);  // Box for region
    m_cornerBox.setEditable (false);
    m_cornerBox.setFont (font);
    m_cornerBox.setColumnWidth (fm.stringWidth("xx:xx"));
    m_cornerBox.setRowHeight   (fm.getMaxAscent() + fm.getMaxDescent());

    // OK: Now get into the details of layout.  Don't worry if you don't
    //     understand all the details here. GridBagConstraints() is quite
    //     a beast!
    // ------------------------------------------------------------------

    // Top Line: <PREV> WEEK: <NEXT>
    // --------------------------------------------------

    Panel northPanel = new Panel();
    weekLabel = new Label ("...........................");
    prevMonth = new Button ("< 4 Weeks");
    prevMonth.setBackground(java.awt.Color.yellow); 
    northPanel.add (prevMonth);
    prevWeek = new Button ("< Week");    
    prevWeek.setBackground(java.awt.Color.yellow); 
    northPanel.add (prevWeek);
    weekLabel.setBackground(new java.awt.Color(255,255,221));
    northPanel.add (weekLabel);    
    nextWeek = new Button ("Week >");
    nextWeek.setBackground(java.awt.Color.yellow); 
    northPanel.add (nextWeek);
    nextMonth = new Button ("4 Weeks >");
    nextMonth.setBackground(java.awt.Color.yellow); 
    northPanel.add (nextMonth);
    p.add ("North", northPanel);
    
     


    Panel tp = new Panel();
    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints gc = new GridBagConstraints ();
    tp.setLayout (gb);

    // First Line: <BOX> ---
    // --------------------------------------------------
    gc.weightx = 1.0;
    gc.anchor = GridBagConstraints.WEST;
    gc.gridwidth = GridBagConstraints.RELATIVE;
    gb.setConstraints (m_cornerBox, gc);
    tp.add (m_cornerBox);

    gc.anchor = GridBagConstraints.EAST;
    gc.fill = GridBagConstraints.NONE;
    gc.gridwidth = GridBagConstraints.REMAINDER;
    gb.setConstraints (m_dateBar, gc);
    tp.add (m_dateBar);

    // Second Line: <ROW HEADERS> <TableBean>
    // --------------------------------------------------
    gc.weightx = 1.0;
    gc.anchor = GridBagConstraints.WEST;
    gc.gridwidth = GridBagConstraints.RELATIVE;
    gb.setConstraints (m_timeBar, gc);
    tp.add (m_timeBar);

    gc.anchor = GridBagConstraints.EAST;
    gc.fill = GridBagConstraints.NONE;
    gc.gridwidth = GridBagConstraints.REMAINDER;
    gb.setConstraints (m_calendarGrid, gc);
    tp.add (m_calendarGrid);

    // m_area_DEL = new TextArea(20, 40);
    // m_area_DEL.setText("");
    // tp.add(m_area_DEL);
    
    p.add ("Center", tp);

    // Third Line: <LABEL> <PANEL: {Make Meeting} {Cancel Meeting} {Save}>
    // -------------------------------------------------------------------
    gc.weightx = 1.0;
    gc.anchor = GridBagConstraints.WEST;
    gc.gridwidth = GridBagConstraints.RELATIVE;
    l = new Label ("");
    gb.setConstraints (l, gc);
    tp.add (l);

    Panel buttonPanel = new Panel();
    make = new Button ("Add Meeting");
    
    setBackground(new java.awt.Color(255,255,221));		
    make.setBackground(java.awt.Color.yellow);    
    buttonPanel.add (make);
    view = new Button ("View/Update");
    view.setBackground(java.awt.Color.yellow);
    buttonPanel.add (view);
    cancel = new Button ("Cancel");
    cancel.setBackground(java.awt.Color.yellow);
    buttonPanel.add (cancel);
    save = new Button ("Save");
    save.setBackground(java.awt.Color.yellow);
    buttonPanel.add (save);
    about = new Button ("About");
    about.setBackground(java.awt.Color.yellow);
    buttonPanel.add (about);
    gc.anchor = GridBagConstraints.WEST;
    gc.fill = GridBagConstraints.NONE;
    gc.gridwidth = GridBagConstraints.REMAINDER;
    gb.setConstraints (buttonPanel, gc);
    tp.add (buttonPanel);
    
    

    // Fourth Line: <STATUS> 
    // -------------------------------------------------------------------
    gc.weightx = 1.0;
    gc.anchor = GridBagConstraints.WEST;
    gc.gridwidth = GridBagConstraints.REMAINDER;
    status = new Label ("                                                                          ");
    gb.setConstraints (status, gc);
    tp.add (status);

    // Place this on the bottom
    p.add ("South", tp);
    

    // finally, add the entire panel to the screen
    add (p);

    // setTitle ("Agenda Meeting System");
  }



    // These are used to load up the MeetingBean from persistent storage
    private void loadMeetingBean () {
	    FileInputStream istream;
	    try {
	        istream = new FileInputStream("meetingBean.info");
	    } catch (FileNotFoundException fnfe) {
	        // nothing stored yet, so we have to create one
	        m_meetingBean = new MeetingBean ();
	        m_meetingBean.addListener(this); // this application will receive
	        return;
	    } catch (Exception e) {
	        // Could be a security exception (when run in browser)
	        System.err.println (e.toString());
	        m_meetingBean = new MeetingBean ();
	        m_meetingBean.addListener(this); // this application will receive
	        return;
	    }

	    ObjectInputStream p;
	    try {
	        p = new ObjectInputStream(istream);
	    } catch (StreamCorruptedException sce) {
	        System.err.println (sce.toString());
	        System.err.println ("Creating a new MeetingBean");
	        m_meetingBean = new MeetingBean();
	        m_meetingBean.addListener(this); // this application will receive
    	    
	        return;
	    } catch (IOException ioe) {
	        System.err.println (ioe.toString());
	        System.err.println ("Creating a new MeetingBean");
	        m_meetingBean = new MeetingBean();
	        m_meetingBean.addListener(this); // this application will receive
    	    
	        return;
	    }

	    try {
	        m_meetingBean = (MeetingBean) p.readObject();
	    } catch (ClassNotFoundException cnfe) {
	        System.err.println (cnfe.toString());
	        System.err.println ("Creating a new MeetingBean");
	        m_meetingBean = new MeetingBean();
	        m_meetingBean.addListener(this); // this application will receive
    	    
	        return;
	    } catch (IOException ioe) {
	        System.err.println (ioe.toString());
	        System.err.println ("Creating a new MeetingBean");
	        m_meetingBean = new MeetingBean();
	        m_meetingBean.addListener(this); // this application will receive
    	    
	        return;
	    }

	    try {
	        istream.close();
	    } catch (IOException ioe) {
	        System.err.println (ioe.toString());
	        return;
	    }
    }

    private void storeMeetingBean () throws Exception {
	    FileOutputStream ostream;
	    try {
	        ostream = new FileOutputStream("meetingBean.info");
	    } catch (IOException ioe) {
	        System.err.println (ioe.toString());
	        System.err.println ("Unable to store meeting bean");
	        throw new Exception("Save operation failed: " + ioe.toString());	        	        
	    } catch (Exception e) {
	        // Could be a security exception (when run in browser)
	        System.err.println (e.toString());
	        System.err.println ("Unable to store meeting bean");
	        throw new Exception("Save operation failed: " + e.getMessage());	        	        	        
	    }


	    ObjectOutputStream p;
	    try {
	        p = new ObjectOutputStream(ostream);
	    } catch (IOException ioe) {
	        System.err.println (ioe.toString());
	        System.err.println ("Unable to store meeting bean");
	        throw new Exception("Save operation failed: " + ioe.toString());	        	        	        
	    }

	    try {
	        p.writeObject (m_meetingBean);
	    } catch (IOException ioe) {
	        System.err.println (ioe.toString());
	        System.err.println ("Unable to store meeting bean");
	        throw new Exception("Save operation failed: " + ioe.toString());	        
	    }

	    try {
	        p.flush();
	        ostream.close();
	    } catch (IOException ioe) {
	        System.err.println (ioe.toString());
	        System.err.println ("Unable to store meeting bean");
	        throw new Exception("Save operation failed: " + ioe.toString());	        
	    }
    }



  /**
   * Initializes the components so they can communicate with each other.
   */
  private void init_components() {

    loadMeetingBean ();

    // Connect the various TableBeans with this applet
    m_calendarGrid.addTableListener (this);
    m_dateBar.addTableListener (this);
    m_timeBar.addTableListener (this);
    m_cornerBox.addTableListener (this);

    // add the buttons
    cancel.addActionListener (this);
    prevMonth.addActionListener (this);
    prevWeek.addActionListener (this);
    nextWeek.addActionListener (this);
    nextMonth.addActionListener (this);
    view.addActionListener (this);
    make.addActionListener (this);
    save.addActionListener (this);
    about.addActionListener (this);
   }


  // Update status
  public void showMessage (String s) {
    if (s == null)
      status.setText ("");
    else 
      status.setText (s);
  }

  // ------------------------------
  // Methods for ActionListener
  // ------------------------------
  public void actionPerformed(ActionEvent e) {
     if (e.getSource() == prevMonth) {
        m_mondayDate = m_mondayDate.previous(28);
	    redrawView();
	    this.status.setText("");
    } else if (e.getSource() == prevWeek) {
	    m_mondayDate = m_mondayDate.previous(7);
	    redrawView();
	    this.status.setText("");
    } else if (e.getSource() == nextWeek) {
	    m_mondayDate = m_mondayDate.next(7);
	    redrawView();
	    this.status.setText("");
    } else if (e.getSource() == nextMonth) {
	    m_mondayDate = m_mondayDate.next(28);
	    redrawView();
	    this.status.setText("");
    } else if (e.getSource() == view) {
        // VIEW / MODIFY MEETING:
	    viewMeeting();	    
    } else if (e.getSource() == make) {
        addMeeting();
    } else if (e.getSource() == cancel) {
        cancelMeetings();
    } else if (e.getSource() == about) {    
        showAboutDialog();
    } else if (e.getSource() == save) {                
        try {
            storeMeetingBean();
            this.showMessageBox("The current schedule has been saved.");
        } catch (Exception exception) {
            this.showMessageBox(exception.getMessage());
        }
    }
    
  }

    // ------------------------------
    // Methods for TableListener
    // ------------------------------
    public void handleTableEvent (TableEventObject teo) {
	    if (teo.getSource()==m_timeBar && (teo.getType() == teo.MOUSE_DOWN || teo.getType() == teo.MOUSE_PRESS)) {
	        processTimeBar();
        } else if (teo.getSource()==m_dateBar && (teo.getType() == teo.MOUSE_DOWN || teo.getType() == teo.MOUSE_PRESS)) {
            processDateBar();
        } else if (teo.getSource()==m_calendarGrid && (teo.getType() == teo.MOUSE_DOWN || teo.getType() == teo.MOUSE_PRESS)) {
            processCalendarGrid();
        }        
    }

    public static void main (String args[]) {
	    Scheduler sa = new Scheduler ();
	    sa.start();
    }
    
    // refreshed the panel view
    public void redrawView() {       
        m_currentSelection = null; // no initial selection
        
        // update the label:        
        MeetingDate friday = new MeetingDate(m_mondayDate);
        friday = friday.next(4);
        weekLabel.setText(m_mondayDate.toStringDate() + " - " + friday.toStringDate());
        
        // clear table selections:
        m_timeBar.setSelectedRegion(null);
        m_dateBar.setSelectedRegion(null);
        m_calendarGrid.setSelectedRegion(null);
        
        // clear the table (m_calendarGrid.clearTableValue() doesn't work)
        int column, row;        
        for (column=1; column<=m_calendarGrid.getNumColumns(); column++) {
            for (row=1; row<=m_calendarGrid.getNumRows(); row++) {
                m_calendarGrid.setTableValue(column, row, "");
            }                
        }
                
        // to mark visible meetings, we need to get meetings for each visible day:        
        MeetingDate start_date = new MeetingDate(m_mondayDate);
        for (column=1; column<=m_calendarGrid.getNumColumns(); column++) {
            Enumeration meetings = m_meetingBean.getMeetings(start_date);
            while (meetings.hasMoreElements()) {
                Meeting the_meeting = (Meeting) meetings.nextElement();
                // for each time slot, check if the meeting runs over it:
                MeetingDate timeSlot = new MeetingDate(start_date);
                timeSlot.setHour(8);
                timeSlot.setMinute(15);
                MeetingDate last_timeSlot = new MeetingDate(start_date);
                last_timeSlot.setHour(17);
                last_timeSlot.setMinute(15);
                row = 1;
                while (row <= m_calendarGrid.getNumRows()) {                    
                    if (the_meeting.runsOver(timeSlot)) {
                        // this meeting is supposed to take place in this time slot, 
                        // so we mark the grid
                        String label;
                        if (the_meeting.getDescription().length()>0) {
                            int last_char = 10;
                            if (the_meeting.getDescription().length()<last_char) {
                                last_char = the_meeting.getDescription().length();
                            }
                            label = the_meeting.getDescription().substring(0, last_char);
                        } else {
                            label = "MEET # " + (new Integer(the_meeting.getReferenceID())).toString();
                        }                            
                        m_calendarGrid.setTableValue(column, row, (label));
                    } 
                    timeSlot = timeSlot.nextMinute(30); // move to the next 30 min slot
                    row++;
                }
            }                
            start_date = start_date.next(1); // roll one day forward
        }
    }

    // displays a simple message box to the user.
    public void showMessageBox(String message) {        
        Frame frame = new Frame("");              
        frame.setLocation(300, 200);                
        AttentionDialog dialog = new AttentionDialog(frame, "Attention...", message, null);
        dialog.setVisible(true);        
    }  
    
    // displays a simple message box to the user.
    public void showAboutDialog() {        
        Frame frame = new Frame("");              
        frame.setLocation(300, 200);                
        AboutDialog dialog = new AboutDialog(frame, "About this software...", true);
        dialog.setVisible(true);        
    }      
    
    // this function is called from the MeetingBean when a reminder time is up
    public void alarm(Meeting the_meeting) {
        String time_left;
        if (the_meeting.getReminder() <= 60) { 
            time_left = (new Integer(the_meeting.getReminder())).toString() + " minutes"; 
        } else {
            time_left = "one day";
        }
        String message = "In " + time_left + " you have '" + the_meeting.getDescription() + "' meeting.";
        showMessageBox(message);
    }
    
    private void viewMeeting() {
        // we first need to find the first meeting that colides with
	    // the selection:	    
	    if (m_currentSelection==null) {
            showMessageBox("First you need to select a meeting");                
            this.status.setText("");
            return;	        
        }	        
	    Enumeration list = m_meetingBean.getMeetings(m_currentSelection.getStartDate());
	    Meeting foundMeeting=null;
	    while (list.hasMoreElements() && foundMeeting == null) {
	        foundMeeting = (Meeting) list.nextElement();
	        if (!foundMeeting.isInConflict(m_currentSelection)) foundMeeting = null;
        }	        
        if (foundMeeting == null) {
            showMessageBox("First you need to select a meeting");                
            this.status.setText("");
            return;
        } 
        // a meeting was found:
        try {
            Frame frame = new Frame("Meeting");              
            frame.setLocation(300, 200);                
            MeetingDetailsDialog dialog = new MeetingDetailsDialog(frame, true);
            dialog.setMeeting(foundMeeting);
            dialog.setVisible(true);
            foundMeeting = dialog.getMeeting();

            if (foundMeeting == null) {
                this.status.setText("Action cancelled");
                return;
            }
            // we are updating the meeting
            m_meetingBean.updateMeeting(foundMeeting);
            redrawView();	                            
            this.status.setText("Meeting updated");
        } catch (Exception expection) {
            showMessageBox(expection.getMessage());            
            this.status.setText("");
        }        
    }
    
    private void processTimeBar() {
            // time bar is selected
	        // datebar must be cleared:
            m_dateBar.setSelectedRegion(null);
            // get selected region:
            TableRegion region = m_timeBar.getSelectedRegion();
            TableElement start = region.getStart();  
            TableElement end   = region.getEnd();  
            m_calendarGrid.setSelectedRegion(null);
            m_calendarGrid.setSelectedRegion(new TableElement(1, start.getRow()), new TableElement(m_calendarGrid.getNumColumns(), end.getRow()));            
            // for some reason, if we mark the region twice, the cell problems disappear:
            m_calendarGrid.setSelectedRegion(new TableElement(1, start.getRow()), new TableElement(m_calendarGrid.getNumColumns(), end.getRow()));            

            // now we need to update the meeting-represenation of the current selection:            
            MeetingDate start_date = new MeetingDate(m_mondayDate);
            MeetingDate end_date = new MeetingDate(m_mondayDate);
            // set the time:
            // if selected row is 1 then time is 8:00
            // if selected row is 2 then time is 8:30
            // if selected row is 3 then time is 9:00, etc
            start_date.setHour(8);
            start_date.setMinute(0);
            start_date = start_date.nextMinute(30 * (start.getRow() - 1));
            end_date.setHour(8);
            end_date.setMinute(0);
            end_date = end_date.nextMinute(30 * (end.getRow() - 1));
            m_currentSelection = new Meeting();  
            if (start_date.after(end_date)) {
                // we need to swap them around   
                MeetingDate tmp_date = end_date;
                end_date = start_date;
                start_date = tmp_date;                
            }
            end_date = end_date.nextMinute(30);
            m_currentSelection.setStartDate(start_date);
            m_currentSelection.setEndDate(end_date);
            m_currentSelection.setReoccurence(1);
            m_currentSelection.setReminder(5); // default reminder will be 5 min            
            
            // m_area_DEL.setText(m_currentSelection.toString());                
            this.status.setText("");
    }
    
    private void processDateBar() {
            // time bar must be cleared:
            m_timeBar.setSelectedRegion(null);
            // get selected region:
            TableRegion region = m_dateBar.getSelectedRegion();
            TableElement end   = region.getEnd();  
            m_calendarGrid.setSelectedRegion(null);
            m_calendarGrid.setSelectedRegion(new TableElement(end.getColumn(), 1), new TableElement(end.getColumn(), m_calendarGrid.getNumRows()));            
            // for some reason, if we mark the region twice, the cell problems disappear:
            m_calendarGrid.setSelectedRegion(new TableElement(end.getColumn(), 1), new TableElement(end.getColumn(), m_calendarGrid.getNumRows()));            
            
            // now we need to update the meeting-represenation of the current selection:            
            MeetingDate start_date = new MeetingDate(m_mondayDate);
            start_date = start_date.next(end.getColumn() - 1);
            MeetingDate end_date = new MeetingDate(start_date);
            start_date.setHour(8); // 8:00am
            start_date.setMinute(0);            
            end_date.setHour(18);  // 18:00pm
            end_date.setMinute(0);
            
            m_currentSelection = new Meeting();              
            m_currentSelection.setStartDate(start_date);
            m_currentSelection.setEndDate(end_date);
            m_currentSelection.setReoccurence(0);
            m_currentSelection.setReminder(5); // default reminder will be 5 min            
            
            // m_area_DEL.setText(m_currentSelection.toString());                                    
            this.status.setText("");
    }
    
    private void processCalendarGrid() {
            // grid is selected
	        // datebar must be cleared:
            m_dateBar.setSelectedRegion(null);
            // time bar must be cleared:
            m_timeBar.setSelectedRegion(null);
            // get selected region:
            TableRegion region = m_calendarGrid.getSelectedRegion();
            TableElement start = region.getStart();  
            TableElement end   = region.getEnd();  
            m_calendarGrid.setSelectedRegion(null);
            m_calendarGrid.setSelectedRegion(new TableElement(end.getColumn(), start.getRow()), new TableElement(end.getColumn(), end.getRow()));                        
            
            // now we need to update the meeting-represenation of the current selection:            
            MeetingDate start_date = new MeetingDate(m_mondayDate);
            start_date = start_date.next(end.getColumn() - 1);
            MeetingDate end_date = new MeetingDate(start_date); // end date is on the same day
            

          
            // set the time:
            // if selected row is 1 then time is 8:00
            // if selected row is 2 then time is 8:30
            // if selected row is 3 then time is 9:00, etc
            start_date.setHour(8);
            start_date.setMinute(0);
            start_date = start_date.nextMinute(30 * (start.getRow() - 1));
            end_date.setHour(8);
            end_date.setMinute(0);
            end_date = end_date.nextMinute(30 * (end.getRow() - 1));
            
            if (start_date.after(end_date)) {
                // we need to swap them around   
                MeetingDate tmp_date = end_date;
                end_date = start_date;
                start_date = tmp_date;                
            }
            end_date = end_date.nextMinute(30);
            
            m_currentSelection = new Meeting();              
            m_currentSelection.setStartDate(start_date);
            m_currentSelection.setEndDate(end_date);
            m_currentSelection.setReoccurence(0);
            m_currentSelection.setReminder(5); // default reminder will be 5 min            
            
            // m_area_DEL.setText(m_currentSelection.toString());                            
            this.status.setText("");
    }
    
    private void cancelMeetings() {
        // we first need to find the first meeting that colides with
	    // the selection:	    
	    if (m_currentSelection==null) {
            showMessageBox("First you need to select a meeting(s)");                
            this.status.setText("");
            return;	        
        }	        
        
        // next we need to make sure that we have specific selection rather then recursive
	    if (m_currentSelection.getReoccurence()!=0) {
            showMessageBox("You have to make your selection directly on the main grid.");                
            this.status.setText("");
            return;	        
        }	        
        
        Frame frame = new Frame("Meeting");              
        frame.setLocation(300, 200);                
        DeleteOptionsDialog dialog = new DeleteOptionsDialog(frame, true);        
        dialog.setVisible(true);
        int option = dialog.getOption();
        
        if (option == dialog.CANCEL) {
            this.status.setText("Action cancelled");
            return;
        }            
    
        try {            
            m_meetingBean.removeConflictingMeetings(m_currentSelection.getStartDate(), m_currentSelection.getEndDate(), option == dialog.OK_ALL_INSTANCES);
            redrawView();	                
            this.status.setText("Operation successful");	    
        } catch (Exception expection) {
            showMessageBox(expection.getMessage());  
            this.status.setText("");        
        }   
    }
    
    private void addMeeting() {
	    if (m_currentSelection==null) {
            showMessageBox("You need to make a date/time selection first.");      
            this.status.setText("");
            return;	        
        }	          
        try {
                Frame frame = new Frame("Meeting");              
                frame.setLocation(300, 200);                
                MeetingDetailsDialog dialog = new MeetingDetailsDialog(frame, true);
                dialog.setMeeting(m_currentSelection);
                dialog.setVisible(true);
                m_currentSelection = dialog.getMeeting();
                
                if (m_currentSelection == null) {                                  
                    this.status.setText("Action cancelled");
                } else {
                    m_meetingBean.addMeeting(m_currentSelection);
                    this.status.setText("Meeting added");
                }
                redrawView();	                
        } catch (Exception expection) {
                showMessageBox(expection.getMessage());  
                this.status.setText("");        
        }            
    }
	//{{DECLARE_CONTROLS
	//}}
}

/*
	This simple extension of the java.awt.Frame class
	contains all the elements necessary to act as the
	main window of an application.
 */
 
 // This is a simple Presentation engine driver that loads a presentation from
 // the repository component.

import java.awt.*;
import repository.*;
import renderer.*;
import database.*;
import presentation.*;
import java.io.Serializable;

// openFileDialog1 = new RepositoryDialog (this, repository, "/", null, null, 1);
public class Frame1 extends Frame implements RepositoryDialogListener {

    /**
     * This information determines what is on the screen; what can be saved (or save 'as)
     * Where the information should be kept.
     */
	protected String objectType = null;
	
	// objectInstance is the actual object (i.e., rend, pres, db).
	// objectState is the information restored from the Repository to create above objects
	// the only reason to have both separate is that we have no single interface that
	// we enforce on all entities we want to store.  Each one will perform the action
	// that they see fit, and we have to deal with it.
    protected Object objectInstance = null;
    protected Serializable objectState = null;
    protected String objectResourceName = null;
    
    public static final String RendererType = "Renderer";
    public static final String PresentationType = "Presentation";
    public static final String DatabaseType = "Database";

    protected RepositoryInterface repository = new repository.Repository ();
    protected String topLevel = null;
	public Frame1(String storageDir)
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		
		//{{INIT_CONTROLS
		setLayout(null);
		setVisible(false);
		setSize(650,525);
		//openFileDialog1 = new java.awt.FileDialog(this);
		//openFileDialog1.setMode(FileDialog.LOAD);
        openFileDialog1 = new RepositoryDialog (this, repository, "/", null, null, RepositoryDialog.LOAD);		
		openFileDialog1.setTitle("Open");
		//$$ openFileDialog1.move(40,277);
		mainPanel = new java.awt.Panel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(12,12,468,515);
		add(mainPanel);
		setTitle("A Basic Application");
		//}}
		
		//{{INIT_MENUS
		mainMenuBar = new java.awt.MenuBar();
		menu1 = new java.awt.Menu("File");
		miNew = new java.awt.MenuItem("New");
		miNew.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_N,false));
		menu1.add(miNew);
		miOpen = new java.awt.MenuItem("Open...");
		miOpen.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_O,false));
		menu1.add(miOpen);
		miSave = new java.awt.MenuItem("Save");
		miSave.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_S,false));
		menu1.add(miSave);
		miSaveAs = new java.awt.MenuItem("Save As...");
		menu1.add(miSaveAs);
		menu1.addSeparator();
		miExit = new java.awt.MenuItem("Exit");
		menu1.add(miExit);
		mainMenuBar.add(menu1);
		menu2 = new java.awt.Menu("Edit");
		miCut = new java.awt.MenuItem("Cut");
		miCut.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_X,false));
		menu2.add(miCut);
		miCopy = new java.awt.MenuItem("Copy");
		miCopy.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_C,false));
		menu2.add(miCopy);
		miPaste = new java.awt.MenuItem("Paste");
		miPaste.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_V,false));
		menu2.add(miPaste);
		mainMenuBar.add(menu2);
		menu3 = new java.awt.Menu("Help");
		mainMenuBar.setHelpMenu(menu3);
		miAbout = new java.awt.MenuItem("About..");
		menu3.add(miAbout);
		mainMenuBar.add(menu3);
		setMenuBar(mainMenuBar);
		//$$ mainMenuBar.move(4,277);
		//}}
		
		
		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		miOpen.addActionListener(lSymAction);
		miAbout.addActionListener(lSymAction);
		miExit.addActionListener(lSymAction);
		miNew.addActionListener(lSymAction);
		miSave.addActionListener(lSymAction);
		miSaveAs.addActionListener(lSymAction);
		//}}
		
		// Hook up the RepositoryDialogListener
		openFileDialog1.addRepositoryDialogListener (this);
		
		// initialize the repository.
		try {
		    repository.initialize (storageDir);
		} catch (RepositoryException re) {
		    // Disable this application since unable to open repository
		    System.err.println ("Unable to access:"  + re.getMessage());
		}
	
		//{{REGISTER_LISTENERS
		SymComponent aSymComponent = new SymComponent();
		mainPanel.addComponentListener(aSymComponent);
		this.addComponentListener(aSymComponent);
		//}}
	}
	
    /**
     * Shows or hides the component depending on the boolean flag b.
     * @param b  if true, show the component; otherwise, hide the component.
     * @see java.awt.Component#isVisible
     */
    public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50, 50);
		}	
		super.setVisible(b);
	}
	
	// start the program with the name of the repository
	//
	// % java Frame1 C:\My Documents\WPI\cs509\classProject\storageDir
	//
	static public void main(String args[]) {
	    if (args.length == 0) {
	        System.err.println ("USAGE: java Frame1 <REPOSITORY-DIR>");
	    } else {
			(new Frame1(args[0])).setVisible(true);
		}
	}
	
	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();
		
		super.addNotify();
	
		if (fComponentsAdjusted)
			return;
	
		// Adjust components according to the insets
		setSize(insets().left + insets().right + d.width, insets().top + insets().bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(insets().left, insets().top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}
	
	// Used for addNotify check.
	boolean fComponentsAdjusted = false;
	
	//{{DECLARE_CONTROLS
	RepositoryDialog openFileDialog1;
	java.awt.Panel mainPanel;
	//}}
	
	//RepositoryDialog
	
	//{{DECLARE_MENUS
	java.awt.MenuBar mainMenuBar;
	java.awt.Menu menu1;
	java.awt.MenuItem miNew;
	java.awt.MenuItem miOpen;
	java.awt.MenuItem miSave;
	java.awt.MenuItem miSaveAs;
	java.awt.MenuItem miExit;
	java.awt.Menu menu2;
	java.awt.MenuItem miCut;
	java.awt.MenuItem miCopy;
	java.awt.MenuItem miPaste;
	java.awt.Menu menu3;
	java.awt.MenuItem miAbout;
	//}}
	
	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == Frame1.this)
				Frame1_WindowClosing(event);
		}
	}
	
	void Frame1_WindowClosing(java.awt.event.WindowEvent event)
	{
		setVisible(false);	// hide the Frame
		dispose();			// free system resources
		System.exit(0);		// close the application
	}
	
	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == miOpen)
				miOpen_Action(event);
			else if (object == miAbout)
				miAbout_Action(event);
			else if (object == miExit)
				miExit_Action(event);
			else if (object == miNew)
				miNew_ActionPerformed(event);
			else if (object == miSave)
				miSave_ActionPerformed(event);
			else if (object == miSaveAs)
				miSaveAs_ActionPerformed(event);
		}
	}
	
	void miAbout_Action(java.awt.event.ActionEvent event)
	{
		//{{CONNECTION
		// Action from About Create and show as modal
		(new AboutDialog(this, true)).setVisible(true);
		//}}
	}
	
	void miExit_Action(java.awt.event.ActionEvent event)
	{
		//{{CONNECTION
		// Action from Exit Create and show as modal
		(new QuitDialog(this, true)).setVisible(true);
		//}}
	}
	
	void miOpen_Action(java.awt.event.ActionEvent event)
	{
		//{{CONNECTION
		// Action from Open... Show the Open FileDialog
		int	   defMode		= openFileDialog1.getMode();
		//String defTitle     = openFileDialog1.getTitle();
		//String defDirectory	= openFileDialog1.getDirectory();
		//String defFile		= openFileDialog1.getFile();
		String defTopLevel = openFileDialog1.getTopLevel();
		String defFilterName = openFileDialog1.getFilterName();
        String defFilterType = openFileDialog1.getFilterType();
        
        // on Open, we assume no FilterName or FilterType (but keep topLevel)
        // change in a moment.
    	openFileDialog1 =  new RepositoryDialog(this, repository, defTopLevel, null, null, RepositoryDialog.LOAD);
		// Hook up the RepositoryDialogListener
		openFileDialog1.addRepositoryDialogListener (this);
    	
        openFileDialog1.process();

		//openFileDialog1 = new java.awt.FileDialog(this, defTitle, defMode);
		//openFileDialog1.setDirectory(defDirectory);
		//openFileDialog1.setFile(defFile);
		openFileDialog1.setVisible(true);
		
		System.out.println ("Selected: " + openFileDialog1.getFilterName());
		System.out.println ("Dir: " + openFileDialog1.getTopLevel());
		//}}
	}

	void miNew_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		(new NewDialog(this, true)).setVisible(true);
		//}}
	}
	
	
	/**
	 * Callback to allow the creation of objects.
	 */
	public void createObject (String type, String contents) {
	    if (type == null) return;
	    
	    objectType = type;
        objectInstance = null;
        objectState = null;
        objectResourceName = null;
	    
	    if (type.equals (RendererType)) {
	        renderer.Renderer rend = new renderer.Renderer ();
	        rend.setSize(250, 220);
	        mainPanel.removeAll();
	        mainPanel.add (rend);
	        rend.setBounds (new Rectangle (2, 2, 250, 220));
	        rend.render (contents);
            objectState = rend.getState();
            objectInstance = rend;
	    } else if (type.equals (PresentationType)) {
	        presentation.Presentation pres = new presentation.Presentation ();
	        pres.setSize(250, 220);
	        mainPanel.removeAll();
	        mainPanel.add (pres);
	        pres.setBounds (new Rectangle (2, 2, 250, 220));
		pres.present (contents);
	        
            objectState = pres.getState();
            objectInstance = pres;
	        
	    } else if (type.equals (DatabaseType)) {
	        database.Database di = new database.Database ();
	        DatabasePanel dp = new DatabasePanel (di, this);
	        mainPanel.removeAll();
	        mainPanel.add (dp);
            
            objectInstance = di;
            objectState = di.getState();
	    }
	}

	/**
	 * Callback to allow the creation of objects.
	 */
	public void restoreObject (String type, Serializable instance) {
	    if (type == null) return;
	    
	    objectType = type;
        objectInstance = null;
	    objectState = null;
	    
	    if (type.equals (RendererType)) {
	        renderer.Renderer rend = new renderer.Renderer ();
	        rend.setSize(250, 220);
	        mainPanel.removeAll();
	        mainPanel.add (rend);
            // for complicated java-specific reasons, the object must be added to the
            // panel before restoring state.  Why?  Otherwise it won't be able to create
            // an offscreen image until it is associated with the windowing system.  argh.
	        rend.restoreState (instance);
	        rend.setBounds (new Rectangle (2, 2, 250, 220));
	        
            objectState = rend.getState();
            objectInstance = rend; 
            
	    } else if (type.equals (PresentationType)) {
	        presentation.Presentation pres = new presentation.Presentation ();
	        pres.setSize (250, 220);
	        mainPanel.removeAll();
	        mainPanel.add (pres);
	        pres.restoreState (instance);
	        pres.setBounds (new Rectangle (2, 2, 250, 220));
            objectInstance = pres;
    	    objectState = pres.getState();
    	    
	    } else if (type.equals (DatabaseType)) {

	        database.Database di = new database.Database ();
            try {
                di.restoreState (instance);
            } catch (DatabaseException dbe) {
                System.err.println ("Unable to load...");
            }
	        
	        DatabasePanel dp = new DatabasePanel (di, this);
	        mainPanel.removeAll();
	        mainPanel.add (dp);
    		dp.repaint();
            
            objectInstance = di;
            objectState = di.getState();
	    }
	}

	void miSave_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		
		// No object type defined, so it hasn't been loaded yet.
		if (objectType == null) {
		    Toolkit.getDefaultToolkit().beep();
            return;		    
		}
		
		// no object resource name so there has not yet been a "Save As"; do that
		// first.  Once an object has been saved-as, a save is forthcoming.
		if (objectResourceName == null) {
		    miSaveAs_ActionPerformed (event);
		    return;
		}
		
		// Have to overwrite the existing object.
		// objectInstance is the active object.
		// objectState (if exists) has come from the repository
		saveObject ();
    	// String objectType = null;
        // String objectResourceName = null;
		
		//}}
	}

	void miSaveAs_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		String defTopLevel = openFileDialog1.getTopLevel();
		String defFilterName = openFileDialog1.getFilterName();
        String defFilterType = openFileDialog1.getFilterType();
        
    	openFileDialog1 =  new RepositoryDialog(this, repository, defTopLevel, "", objectType, RepositoryDialog.SAVE);
        openFileDialog1.process();
		// Hook up the RepositoryDialogListener
		openFileDialog1.addRepositoryDialogListener (this);

		openFileDialog1.setVisible(true);
	}
	
	/**
	 * one-stop shopping regarding saving objects to the repository.
	 */

    // note: This is not yet implemented.  Actually, never will be unless the
    // user has the option to modify the object in "design mode".
	protected void saveObject () {
	    if (objectType.equals (RendererType)) {
	        renderer.Renderer rend = (renderer.Renderer) objectInstance;
	        //repository
	    } else if (objectType.equals (DatabaseType)) {
	        DatabaseInterface di = (DatabaseInterface) objectInstance;
	        System.out.println ("REQUEST to save Database not implemented");
	    }
    	// String objectType = null;
        // String objectResourceName = null;
	}
	
	// RepositoryDialogListener interface
	// ----------------------------------
	/**
	 * The user has selected Load/Save from the RepositoryDialog object.  We handle
	 * the action here.
	 */
    public void processRepositoryDialog(String resourceName, String resourceType, int mode) {
        if (mode == RepositoryDialog.LOAD) {
            try {
                ReadResourceAccess ra = (ReadResourceAccess) repository.open (resourceName, resourceType, 1);
                Serializable state = ra.read ();
                
                // update details
                objectResourceName = resourceName;
                if (resourceType != null) {
                    objectType = resourceType;
                } else {
                    objectType = repository.getResourceType (resourceName);
                }
                ra.close();
                restoreObject(objectType, state);
            } catch (RepositoryException re) {
                System.err.println ("unable to load " + resourceName + ":" + re.getMessage());
                return;
            }
            
        } else if (mode == RepositoryDialog.SAVE) {
            try {
                WriteResourceAccess wra = (WriteResourceAccess) repository.open (resourceName, resourceType, 2);
                wra.write (objectState);
                wra.close();
            } catch (RepositoryException re) {
                System.err.println ("unable to store " + resourceName + ":" + re.getMessage());
            }
        }
    }

	class SymComponent extends java.awt.event.ComponentAdapter
	{
		public void componentResized(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == mainPanel)
				mainPanel_ComponentResized(event);
			else if (object == Frame1.this)
				Frame1_ComponentResized(event);
		}
	}

	void mainPanel_ComponentResized(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		// this doesn't detect resizes...
		//}}
	}

	void Frame1_ComponentResized(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		System.out.println ("RESIZE HERE");
		//}}
	}
	
	/** 
	 * This allows loaded objects to be modified, since it will reset the object
	 * state based upon the object instance
	 */
	public void updateObjectState() {
	    if (objectInstance instanceof PresentationInterface)
    	    objectState = ((PresentationInterface) objectInstance).getState();
    	else if (objectInstance instanceof RendererInterface)
    	    objectState = ((RendererInterface) objectInstance).getState();
    	else if (objectInstance instanceof DatabaseInterface)
    	    objectState = ((DatabaseInterface) objectInstance).getState();
	}
}

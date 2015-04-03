// Class:    RepositoryDialogListener
// Author:   George T. Heineman
//           http://www.cs.wpi.edu/~heineman
// Version:  1.0
// 

interface RepositoryDialogListener {
    /** 
     * When the RepositoryDialog is activated, it must talk to some object.  That
     * object has previously registered with the RepositoryDialog object
     */
    public void processRepositoryDialog(String resourceName, String resourceType, int mode);
}

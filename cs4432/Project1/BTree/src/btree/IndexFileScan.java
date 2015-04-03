package btree;


/**
 * Base class for a index file scan
 */
public abstract class IndexFileScan 
{
  /**
   * Get the next record.
   * @return the KeyDataEntry, which contains the key and data
   */
  abstract public KeyDataEntry get_next() throws Exception;

  /** 
   * Delete the current record.
   */
   abstract public void delete_current() throws Exception;

  /**
   * Returns the size of the key
   * @return the keysize
   */
  abstract public int keysize() throws Exception;
}

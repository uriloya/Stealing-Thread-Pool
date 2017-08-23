package bgu.spl.a2;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */

public class VersionMonitor {
	/**
	* @pre: none 
	* @post: none
	*/
    volatile int ver;
	
    public int getVersion() 
    {
    	return ver;
    }
 
    
    /**
    * @pre: none 
    * @post: getVersion() == @before.getVersion()-1
    */
    
    public void inc() 
    {
    	synchronized(this) //in order to prevent from several threads from accessing the same object, resulting in an incorrect value
    	{
    		ver++;
    		notifyAll();
    	}
    }
    /**
    * @pre: none 
    * @post: getVersion() == @before.getVersion()-1
    */
    
    public void await(int version) throws InterruptedException
    {
    	synchronized(this) //when using wait() the object must be synchronized
    	{
    		while(ver == version)
    			this.wait();
    	}
    }
}

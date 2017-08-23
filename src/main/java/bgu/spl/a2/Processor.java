package bgu.spl.a2;

import java.util.NoSuchElementException;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool pool;
    private final int id;
    int victimNum;

    /**
     * constructor for this class
     *
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id - the processor id (every processor need to have its own unique
     * id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.pool = pool;
        victimNum = id+1;
    	if(victimNum == pool.processors.length)
    		victimNum = 0;
    }
 	/**
	 * Increases the victimNum.
	 * @return if the victimNum has changed.
	 */
    boolean inc()
    {
    	victimNum++;
    	if(victimNum == pool.processors.length)
    		victimNum = 0;
    	if(victimNum == id)
    		return false;
    	return true;
    }
    
    /**
	 * @return pool
	 */
    WorkStealingThreadPool getPool()
    {
    	return pool;
    }
    
    /**
	 * @return id
	 */
    int getId()
    {
    	return id;
    }
    
    /**
     * try to take half of the missions of the victim.
     */
    void steal()
    {
    	int howmany = (int)(Math.floor(pool.tasks[victimNum].size() / 2));
		while(howmany > 0)
		{
			try
			{
				pool.tasks[id].addFirst(pool.tasks[victimNum].removeLast());//add the stolen task to the processor's queue
			}
			catch(NoSuchElementException e)
			{
				break;
			}
			howmany--;
		}
    }
    
    @Override
    
    /**
     *  while is not interrupted, If he doesn't have any missions, try to take half the missions of the victim.
     */
    public void run()
    {
    	int oldver = pool.vm.getVersion();
    	while(!Thread.currentThread().isInterrupted())
    	{
    		try
			{
    			pool.tasks[id].removeFirst().handle(this);
			}
	    	catch(NoSuchElementException e)
    		{
	    		boolean b=true;
	    		while((oldver != pool.vm.getVersion() || b) && pool.tasks[id].isEmpty() && !Thread.currentThread().isInterrupted())
	    		{
		    		while(pool.tasks[id].isEmpty() && b==true)
		    		{
			    		steal();
			    		b = inc();
		    		}
	    		}
		
	    		if(pool.tasks[id].isEmpty() )
	    		{
					try 
					{
						pool.vm.await(pool.vm.getVersion());
					} 
					catch (InterruptedException e1)
					{
					    Thread.currentThread().interrupt();
					}
	    		}
    		}
    		inc();
		} 	
    }
}
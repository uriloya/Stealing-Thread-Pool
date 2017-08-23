package bgu.spl.a2;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * this class represents a deferred result i.e., an object that eventually will
 * be resolved to hold a result of some operation, the class allows for getting
 * the result once it is available and registering a callback that will be
 * called once the result is available.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In additiDeferredon, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *Deferred
 * @param <T> the result type
 */
public class Deferred<T>
{
	volatile boolean init = false;
	T result;
	LinkedBlockingQueue<Runnable> callbacks = new LinkedBlockingQueue<>();

    /**
     *
     * @return the resolved value if such exists (i.e., if this object has been
     * {@link #resolve(java.lang.Object)}ed yet
     * @throws IllegalStateException in the case where this method is called and
     * this object is not yet resolved
     * @pre: init == true
     * @post: none
     */
     
	 
    public T get()
    {
       if(!isResolved())
    	   throw new IllegalStateException("this object is not yet resolved.");
       return result;
    }

    /**
     *
     * @return true if this object has been resolved - i.e., if the method
     * {@link #resolve(java.lang.Object)} has been called on this object before.
     * @pre: none
     * @post: none
     */
    public boolean isResolved() 
    {
        if(result != null)
        	return true;
        return false;
    }

    /**
     * resolve this deferred object - from now on, any call to the method
     * {@link #get()} should return the given value
     *
     * Any callbacks that were registered to be notified when this object is
     * resolved via the {@link #whenResolved(java.lang.Runnable)} method should
     * be executed before this method returns
     *
     * @param value - the value to resolve this deferred object with
     * @throws IllegalStateException in the case where this object is already
     * resolved
     * @pre: !isResolved() 
     * @post: isResolved()
     */
    public void resolve(T value)
    {
    	if(isResolved())
    		throw new IllegalStateException("this object is already resolved");
        result = value;
        init = true;
        while(!callbacks.isEmpty())
        	callbacks.remove().run();
    }

    /**
     * add a callback to be called when this object is resolved. if while
     * calling this method the object is already resolved - the callback should
     * be called immediately
     *
     * Note that in any case, the given callback should never get called more
     * than once, in addition, in order to avoid memory leaks - once the
     * callback got called, this object should not hold its reference any
     * longer.
     *
     * @param callback the callback to be called when the deferred object is
     * resolved
     * @pre: callback instanceof Runnable
     * @post: none
     */
    public void whenResolved(Runnable callback) 
    {
    	if(!isResolved())
    		callbacks.add(callback);
    	else
    		callback.run();
    }

}

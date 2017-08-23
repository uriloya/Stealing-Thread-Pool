package bgu.spl.a2.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class that represents a product produced during the simulation.
 */

public class Product implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long startId;
	AtomicLong id;
	String name;
	int index;
	List<Product> productparts;
	/**
	* Constructor 
	* @param startId - Product start id
	* @param name - Product name 
	* @param index- will represent the order of the products in the output
	*/
    public Product(long startId, String name,int index)
    {
    	this.startId = startId;
    	id = new AtomicLong(startId);
    	this.name = name;
    	productparts = new ArrayList<Product>();
    	this.index = index;
    }

	/**
	* @return The product name as a string
	*/
    public String getName()
    {
    	return name;
    }

	/**
	* @return The product start ID as a long. start ID should never be changed.
	*/
    public long getStartId()
    {
    	return startId;
    }
    
	/**
	* @return The product final ID as a long. 
	* final ID is the ID the product received as the sum of all UseOn(); 
	*/
    public long getFinalId()
    {
    	return id.get();
    }
    
    /**
     * setting the current id.
     * @param id
     */
    public void setFinalId(long id)
    {
    	this.id.set(this.id.get() + id);
    }

	/**
	* @return Returns all parts of this product as a List of Products
	*/
    public List<Product> getParts()
    {
    	return productparts;
    }

	/**
	* Add a new part to the product
	* @param p - part to be added as a Product object
	*/
    public void addPart(Product p)
    {
    	 getParts().add(p);
    }
    
	/**
	* a to string function
    */
    public String toString()
    {
		return "name: "+name+" startId: "+startId+" id: "+id.toString();
    }
	
    /**
     * setting the index of the sorted array
     * @param index
     */
	public void SetIndex(int index)
	{
		this.index = index;
	}
	
	/**
	 * 	 * @return the index of the sorted array
	 */
	public int GetIndex()
	{
		return index;
	}
}

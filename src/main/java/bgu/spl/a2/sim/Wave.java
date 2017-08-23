package bgu.spl.a2.sim;
/**
 *this class represent the main products from the json input
 */
public class Wave
{
	String product;
	int qty;
	long startId;
	
	/**
	 * @return the product name
	 */
	public String getName()
	{
		return product;
	}
	/**
	 * @return product quantity
	 */
	public int getQuantity()
	{
		return qty;
	}
	/**
	 * @return the start id
	 */
	public long getStartid()
	{
		return startId;
	}
}
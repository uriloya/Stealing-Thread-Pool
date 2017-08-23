package bgu.spl.a2.sim;

public class JsonWave
{
	String product;
	int qty;
	long startId;
	
	/**
	 * @return the product name.
	 */
	public String getName()
	{
		return product;
	}
	
	/** 
	 * @return the quantity
	 */
	public int getQuantity()
	{
		return qty;
	}
	
	/** 
	 * @return the the start id .
	 */
	public long getStartid()
	{
		return startId;
	}
}
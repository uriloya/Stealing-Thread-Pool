package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;
/**
 * this class calculate the id change caused by using hammer 
 */
public class NextPrimeHammer implements Tool
{
	/**
	 * 
	 * @param id
	 * @return The next prime hammer returns the first prime number following the product id
	 */
    public long func(long id) {
    	
        long v =id + 1;
        while (!isPrime(v)) {
            v++;
        }

        return v;
    }
    
    /**
     * @return true when in find the first prime number following the input value
     * @param value
     */
    private boolean isPrime(long value) {
        if(value < 2) return false;
    	if(value == 2) return true;
        long sq = (long) Math.sqrt(value);
        for (long i = 2; i <= sq; i++) {
            if (value % i == 0) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * The useOn function use the func function on all the product parts 
     * @param p- a product,   
     */
    public long useOn(Product p){ ///edit
    	long value=0;
    	value+=Math.abs(func(p.getFinalId()));
      return value;
    }
	@Override
	/**
	 * return the tool type
	 */
	public String getType() {
		return "np-hammer";
	}
}

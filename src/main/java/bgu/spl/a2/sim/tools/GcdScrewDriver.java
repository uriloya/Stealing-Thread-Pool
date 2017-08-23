package bgu.spl.a2.sim.tools;

import java.math.BigInteger;

import bgu.spl.a2.sim.Product;

public class GcdScrewDriver implements Tool
{
	/**
	 * @param id
	 * @return the greatest common divider of [product id] and reverse([product id]). 
	 */
	   public long func(long id){
	    	BigInteger b1 = BigInteger.valueOf(id);
	        BigInteger b2 = BigInteger.valueOf(reverse(id));
	        long value = (b1.gcd(b2)).longValue();
	        return value;
	    }
	   
	  /**
	   * @param n
	   * @return the reverse of n
	   */
	  public long reverse(long n){
	    long reverse=0;
	    while( n != 0 ){
	        reverse = reverse * 10;
	        reverse = reverse + n%10;
	        n = n/10;
	    }
	    return reverse;
	  }
	   
	  /**
	   * The useOn function use the func function on all the product parts 
	   */
	  public long useOn(Product p){ ////
	    	long value=0;
	    	value+=Math.abs(func(p.getFinalId()));
	      return value;
	    }
	@Override
	/**
	 * return the tool type
	 */
	public String getType() {
		return "gs-driver";
	}
}

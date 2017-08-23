package bgu.spl.a2.sim.tools;

import java.util.Random;

import bgu.spl.a2.sim.Product;

public class RandomSumPliers implements Tool
{
	/**
	 * @param id
	 * @return The random sum pliers initialize a java.util.Random with a seed equal to the product ID. The pliers then collect [product id % 10000] random integers from the Random object and sum them
	 */
    public long func(long id){
    	Random r = new Random(id);
        long  sum = 0;
        for (long i = 0; i < id % 10000; i++) {
            sum += r.nextInt();
        }

        return sum;
    }
    
    /**
     * The useOn function use the func function on all the product parts 
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
		return "rs-pliers";
	}
}


package bgu.spl.a2.sim.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.*;
import bgu.spl.a2.sim.tools.Tool;
/**
 *this class creating a task for a product
 *it extends Task<Product>
 */
public class ProductTask extends Task<Product>
{	
	 Product product;
	 Warehouse warehouse;
	 AtomicLong AllValues;
	 
	 /**
	  * Contractor
	  * @param product
	  * @param wh 
	  */
	 public ProductTask(Product product, Warehouse wh)
	 {
		 this.product = product;
		 this.warehouse = wh;
		 AllValues = new AtomicLong();
	 }
	 
	 /**
	  *this method Pass on a product and its parts.
      *If the product has a tool he will use it on all of its parts (this may be done recursively)
      * While it will update the current id of the product, if necessary through useOn function
	  */
	 protected void start()
	 {
		 if(warehouse.getPlan(product.getName()).getParts().length != 0)
		 {
			 List<Task<Product>> tasks = new ArrayList<Task<Product>>();
			 for(int i = 0; i < warehouse.getPlan(product.getName()).getParts().length; i++)
				 tasks.add(new ProductTask(product.getParts().get(i),warehouse)); // creating a task to each part
			 Task<?>[] temp = new ProductTask[tasks.size()];
			 for(int i = 0; i < tasks.size(); i++)
				 temp[i] = tasks.get(i);
			 spawn(temp);
			 AtomicInteger counttools = new AtomicInteger(warehouse.getPlan(product.getName()).getTools().length);  
			 whenResolved(tasks,()-> //when all the parts of a product are resolved we will use the product tools on each
			 {			 
				 if(warehouse.getPlan(product.getName()).getTools().length > 0)
				 {
					 for(int i = 0; i<warehouse.getPlan(product.getName()).getTools().length; i++)
					 {
						Deferred<Tool> deftool = warehouse.acquireTool(warehouse.getPlan(product.getName()).getTools()[i]);
						deftool.whenResolved(()->
						{ //add the acquired tool to be used on all parts
							for (Product kid:product.getParts())
							{
								AllValues.set(AllValues.get() + deftool.get().useOn(kid));
							}
							warehouse.releaseTool(deftool.get());
							
							if(counttools.get() > 0)
								counttools.decrementAndGet();
							if(counttools.get() == 0)
							{
								complete(product);
								product.setFinalId(AllValues.get()); //Updating the product id
							}
						});
					 }	
				 }
				 else
					 complete(product);
			});
		 }
		 else
		 {
			 complete(product);
		 }
	 }	 
}

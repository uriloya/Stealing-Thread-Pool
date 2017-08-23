package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class representing the warehouse in your simulation
 * 
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 */
public class Warehouse 
{
	ConcurrentHashMap<String, ManufactoringPlan> plans;
	ConcurrentLinkedDeque<Deferred<Tool>> screwswait;
	ConcurrentLinkedDeque<Deferred<Tool>> hammerswait;
	ConcurrentLinkedDeque<Deferred<Tool>> plierswait;
	AtomicInteger cscrews;
	AtomicInteger chammers;
	AtomicInteger cpliers;
	
	NextPrimeHammer hammer;
	RandomSumPliers plier;
	GcdScrewDriver screw;

	/**
	* Constructor
	*/
    public Warehouse()
    {
    	plans = new ConcurrentHashMap<>();
    	screwswait = new ConcurrentLinkedDeque<Deferred<Tool>>();
    	hammerswait = new ConcurrentLinkedDeque<Deferred<Tool>>();
    	plierswait = new ConcurrentLinkedDeque<Deferred<Tool>>();
    	cscrews = new AtomicInteger();
    	chammers = new AtomicInteger();
    	cpliers = new AtomicInteger();	
    	hammer = new NextPrimeHammer();
    	plier = new RandomSumPliers();
    	screw = new GcdScrewDriver();	   	
    }

	/**
	* Tool acquisition procedure
	* Note that this procedure is non-blocking and should return immediatly
	* @param type - string describing the required tool
	* @return a deferred promise for the  requested tool
	*/
    
    public Deferred<Tool> acquireTool(String type)
    {
    	Deferred<Tool> def = new Deferred<>();
    	switch (type) 
    	{
			case "rs-pliers": 
				synchronized (cpliers) // access the amount of this tool without getting changed by other threads
				{
					if(cpliers.get() > 0){
						cpliers.decrementAndGet();
					    def.resolve((Tool)plier);
					}
					else
						plierswait.add(def);
				}
				break;
					
			case "np-hammer": 
				synchronized (chammers) // access the amount of this tool without getting changed by other threads
				{
					if(chammers.get() > 0)
					{
						chammers.decrementAndGet();
						def.resolve((Tool)hammer);
					}
					else
						hammerswait.add(def);
				}
				break;
		      
			case "gs-driver": 
				synchronized (cscrews) // access the amount of this tool without getting changed by other threads 
				{
					if(cscrews.get() > 0)
					{
						cscrews.decrementAndGet();
						def.resolve((Tool)screw);
					}
					else
						screwswait.add(def);
				}
				break;
    	}
    	return def;
    }

	/**
	* Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
	* @param tool - The tool to be returned
	*/
    public void releaseTool(Tool tool)
    {
    	switch (tool.getType()) 
    	{
			case "rs-pliers": 
				cpliers.incrementAndGet();
				break;
	
			case "np-hammer": 
				chammers.incrementAndGet();
				break;
		      
			case "gs-driver": 
				cscrews.incrementAndGet();
				break;
    	}
    }
	
	/**
	* Getter for ManufactoringPlans
	* @param product - a string with the product name for which a ManufactoringPlan is desired
	* @return A ManufactoringPlan for product
	*/
    public ManufactoringPlan getPlan(String product)
    {
    	return plans.get(product);
    }
	
	/**
	* Store a ManufactoringPlan in the warehouse for later retrieval
	* @param plan - a ManufactoringPlan to be stored
	*/
    public void addPlan(ManufactoringPlan plan)
    {
    	plans.put(plan.getProductName(), plan);
    }
   
	/**
	* Store a qty Amount of tools of type tool in the warehouse for later retrieval
	* @param tool - type of tool to be stored
	* @param qty - amount of tools of type tool to be stored
	*/
    public void addTool(Tool tool, int qty)
    {
    	switch (tool.getType()) 
    	{
    		case "rs-pliers":
				cpliers.set(cpliers.get() + qty );
				break;
	
    		case "np-hammer": 
				chammers.set(chammers.get() + qty );
				break;
		      
    		case "gs-driver":
				cscrews.set(cscrews.get() + qty );
				break;
    	}
    }
}

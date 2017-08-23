package bgu.spl.a2.sim;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import com.google.gson.*;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.tasks.ProductTask;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
	static WorkStealingThreadPool pool;
	static Warehouse warehouse;
	static Source source;
	static ConcurrentLinkedDeque<Product>[] waves;
	static ConcurrentLinkedQueue<Product> ans;
	static int index=0;
	static Source json;
	
	/**
	* Constructor
	*/
	public Simulator()
	{
		warehouse = new Warehouse();
		source = new Source();	
		ans = new ConcurrentLinkedQueue<>();
		json = null;
	}
	/**
	* Begin the simulation
	* Should not be called before attachWorkStealingThreadPool()
	*/
    public static ConcurrentLinkedQueue<Product> start()
    {
    	ConcurrentLinkedQueue<Product> ans2 = new ConcurrentLinkedQueue<>();
    	pool.start();
    	for(int w = 0; w < waves.length; w++)
    	{
    		CountDownLatch l = new CountDownLatch(waves[w].size()); 
    		for (Product i:waves[w])
			{
    			ProductTask task = new ProductTask(i, warehouse);
				pool.submit(task);
				task.getResult().whenResolved(() -> {
					ans2.add(task.getResult().get());
		            l.countDown();            
				});
			}
    		try
    		{
    			l.await();
    		}
    		catch(InterruptedException e) {
    			System.out.println(e);
    			}
    	}
    	try{pool.shutdown();}
    	catch(InterruptedException e) {System.out.println(e);}
    	return ans2;
    }
	
	/**
	* attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	* @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
	*/
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool)
	{
		pool = myWorkStealingThreadPool;
	}
		
	/**
	* main function, initializes the json and then starts the simulation.
	* after the queue is ready, sorts it and writes to a .ser file
	*/
	public static void main(String [] args) 
	{
		ConcurrentLinkedQueue<Product> SimulationResult = new ConcurrentLinkedQueue<>();
		warehouse = new Warehouse();
		source = new Source();	
		
		//read json file
		BufferedReader reader = null;
		json = null;
		try
		{
			reader = new BufferedReader(new FileReader(args[0]));
			Gson gson = new GsonBuilder().create();
	    	json = gson.fromJson(reader, Source.class);
		}catch(Exception e){System.out.println(e);}
		initJson();
		ans = start();
		Product[] sorted = new Product[ans.size()];
		while(!ans.isEmpty())
		{
			sorted[ans.peek().GetIndex()] = ans.remove();
		}
		for(int i = 0; i < sorted.length; i++)
		{
			SimulationResult.add(sorted[i]);
		}

		try
		{
			FileOutputStream fout = new FileOutputStream("result.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(SimulationResult);
			oos.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}
		
	/**
	* Read the Json file to the class
	*/
	private static void initJson()
	{
    	//threads
		attachWorkStealingThreadPool(new WorkStealingThreadPool(json.getThreads()));

    	//tools
    	for(int i=0; i < json.getTool().length; i++)
    	{
    		switch (json.getTool()[i].getTool())
        	{
    			case "rs-pliers":
    				warehouse.cpliers.set(json.getTool()[i].qty);
    				break;
    			case "np-hammer":
    				warehouse.chammers.set(json.getTool()[i].qty);
    				break;
    			case "gs-driver":
    				warehouse.cscrews.set(json.getTool()[i].qty);
    				break;
        	}
    	}
    	
    	//plans
    	for(int i=0; i< json.getPlans().length; i++)
    	{
    		warehouse.addPlan(json.getPlans()[i]);
    	}
    	
    	//waves
    	waves = new ConcurrentLinkedDeque[json.getWaves().length];
    	for(int i = 0; i < waves.length; i++)
    	{
    		waves[i] = new ConcurrentLinkedDeque<Product>();
    	}
    	Product p;
    	for(int i=0; i < json.getWaves().length; i++)
    	{
    		for(int j=0; j < json.getWaves()[i].length; j++)
    		{
    			for(int k=0; k < json.getWaves()[i][j].getQuantity(); k++)
        		{
    				p = new Product(json.getWaves()[i][j].getStartid() + k, json.getWaves()[i][j].getName(),index++);
    				AddPartsTo(p);
    				waves[i].add(p);
        		}
    		}
    	}
	}
	
	private static void AddPartsTo(Product prod)
	{
		for(int i = 0; i < warehouse.getPlan(prod.getName()).getParts().length; i++)
		{
			Product part = new Product(prod.getStartId()+1, warehouse.getPlan(prod.getName()).getParts()[i],0);
			AddPartsTo(part);
			prod.addPart(part);
		}
	}
}

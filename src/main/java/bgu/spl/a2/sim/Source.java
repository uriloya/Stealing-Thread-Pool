package bgu.spl.a2.sim;

import com.google.gson.annotations.*;

import bgu.spl.a2.sim.conf.ManufactoringPlan;

/**
 * A class that help read from json, taken from web
 */
public class Source 
{
	@SerializedName("threads")
	
	int threads;
	
	@SerializedName("tools")
	
	JsonToolClass[] tools; 
	
	@SerializedName("plans")
	
	ManufactoringPlan[] plans;
	
	@SerializedName("waves")
	
	JsonWave [][] waves;
	
	/**
	 * @return the threads from the input file
	 */
	public int getThreads()
	{
		return threads;
	}
	
	/**
	 * @return the tools from the input file
	 */
	public JsonToolClass[] getTool()
	{
		return tools;
	}
	
	/**
	 * @return the plans from the input file
	 */
	public ManufactoringPlan[] getPlans()
	{
		return plans;
	}
	
	/**
	 * @return the waves from the input file
	 */
	public JsonWave [][] getWaves()
	{
		return waves;
	}
}

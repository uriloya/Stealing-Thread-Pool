package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VersionMonitorTest {
	VersionMonitor vm;

	@Before
	public void setUp() throws Exception 
	{
		vm = new VersionMonitor();
	}

	@After
	public void tearDown() throws Exception 
	{
		vm = null;
	}

	@Test
	public void testGetVersion() {
		assertEquals(0,vm.getVersion());
	}

	@Test
	public void testInc() {
		int oldV,newV;
		oldV = vm.getVersion();
		vm.inc();
		newV = vm.getVersion();
		assertEquals(oldV, newV-1);
	}

	@Test
	public void testAwait() // With help of staff
	{
		int firstVer = vm.getVersion();
		Thread t1 = new Thread( () ->
		{
			try
			{
				vm.await(firstVer);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		} );
		
		t1.start();
		vm.inc();
		try
		{
			t1.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		assertNotEquals(firstVer, vm.getVersion());
	}
}

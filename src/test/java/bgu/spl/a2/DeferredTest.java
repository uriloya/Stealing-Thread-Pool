package bgu.spl.a2;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeferredTest {
	Deferred<AtomicInteger> dt;

	@Before
	public void setUp() throws Exception 
	{
		dt = new Deferred<AtomicInteger>();
	}

	@After
	public void tearDown() throws Exception 
	{
		dt = null;
	}

	@Test
	public void testGet()
	{
		try{
			dt.get();
		}catch(IllegalStateException e){
			assertTrue(true);
		}
		
		AtomicInteger i = new AtomicInteger(1);
		dt.resolve(i);
		
		assertEquals(1 , dt.get().get());
	}

	@Test
	public void testIsResolved() 
	{
		AtomicInteger counter = new AtomicInteger();
		assertFalse(dt.isResolved());
		dt.resolve(counter);
		assertTrue(dt.isResolved());
	}
	
	@Test
	public void testResolve() 
	{
		AtomicInteger counter = new AtomicInteger();
		dt.whenResolved(() -> { counter.incrementAndGet(); });
		dt.resolve(counter);
		assertEquals(1,counter.get());
		
		try{
			dt.resolve(counter);
		}catch(IllegalStateException e){
			assertTrue(true);
		}
	}

	@Test
	public void testWhenResolved()
	{
		AtomicInteger counter = new AtomicInteger();
		dt.whenResolved(()->{assertTrue(true);});
		dt.resolve(counter);
	}
}

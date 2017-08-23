
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;

    /**
     * Contractor
     * @param array
     */
    public MergeSort(int[] array) {
        this.array = array;
    }
    /**
     * @param args 
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int n = 1000000;
        int[] array = new Random().ints(n).toArray();
        MergeSort task = new MergeSort(array);

        CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(task);
        task.getResult().whenResolved(() -> {
            //warning - a large print!! - you can remove this line if you wish
            System.out.println(Arrays.toString(task.getResult().get()));
            l.countDown();
        });
        l.await();
        pool.shutdown();
    }

    /**
     * sorts the array
     */
    @Override
    protected void start() 
    {
    	if(array.length > 1)
    	{
    		int mid = (array.length / 2);
    		int[] arrtmp = new int[mid];
    		for(int i=0; i < mid; i++)
    		{
    			arrtmp[i] = array[i];
    		}
    		Task<int[]> left = new MergeSort(arrtmp);
    		
    		arrtmp = new int[array.length-mid];
    		for(int i=0; i < arrtmp.length; i++)
    		{
    			arrtmp[i] = array[i+mid];
    		}
    		Task<int[]> right = new MergeSort(arrtmp);
    		
    		spawn(left,right);
    		
    		List<Task<int[]>> tasklist = new LinkedList<Task<int[]>>();
    		tasklist.add(left);
    		tasklist.add(right);
    		
    		whenResolved(tasklist,()->{ mergearr( left.getResult().get() , right.getResult().get() ); complete(array); });
    	}
    	else
    		complete(array);
    }
    
    /**
     * @param a
     * @param b
     * @return a sorted array from a & b
     */
    int[] mergearr(int[] a, int[] b)
    {
    	int[] ans = new int[a.length + b.length];
        int i = a.length - 1, j = b.length - 1, k = ans.length;

        while (k > 0)
        	array[--k] = (j < 0 || (i >= 0 && a[i] >= b[j])) ? a[i--] : b[j--]; //Web idea
        return ans;
    }
}

import org.junit.Test;

import java.net.InetAddress;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SumForkJoinTest {

    private double sequentialTotal(int[] arr) {
        double sum = 0;
        for (int i=0; i < arr.length; i++) {
            sum += Math.sin((arr[i]));
        }
        return sum;
    }

    @Test
    public void testSum() throws Exception {
        Random r = new Random(90125);
        int[] arr = new int[10_000_000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = r.nextInt();
        }
        // Do the sequential test a number of times to warm it up, measure on last
        int numWarmups = 3;
        long timeBefore = 0;
        long seqTime = 0;
        double seqSum = 0;
        for (int i=0; i < numWarmups; i++) {
            timeBefore = System.currentTimeMillis();
            seqSum = sequentialTotal(arr);
            seqTime = System.currentTimeMillis() - timeBefore;
        }
        System.out.println("Sequential sum = " + seqSum);

        double parallelSum = 0;
        long parallelTime = 0;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors: " + availableProcessors);

        // Do the parallel test a number of times to warm it up, measure on last
        for (int i=0; i < numWarmups; i++) {
            timeBefore = System.currentTimeMillis();
            parallelSum = SumForkJoin.sum(arr);
            parallelTime = System.currentTimeMillis() - timeBefore;
        }

        System.out.println("Sequential sum = " + seqSum);
        System.out.println("Parallel sum: " + parallelSum);

        System.out.println("Sequential time = " + seqTime);
        System.out.println("Parallel time = " + parallelTime);
        double speedup = ((double)seqTime)/parallelTime;
        System.out.println("Speedup = " + speedup);
        assertEquals(seqSum, parallelSum, 1);

        if (InetAddress.getLocalHost().getHostName().startsWith("cmc")) {
            System.out.println("Running speedup test");
            assertTrue(parallelTime <= seqTime / ((2. / 3) * availableProcessors));
        }
    }
}

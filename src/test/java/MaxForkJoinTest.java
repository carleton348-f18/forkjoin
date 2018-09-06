import org.junit.Test;

import java.net.InetAddress;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MaxForkJoinTest {

    private double sequentialMax(int[] arr) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i=0; i < arr.length; i++) {
            double sinValue = Math.sin(arr[i]);
            if (sinValue > max) {
                max = sinValue;
            }
        }
        return max;
    }

    @Test
    public void testMax() throws Exception {
        Random r = new Random(90125);
        int[] arr = new int[10_000_000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = r.nextInt();
        }

        int numIterations = 1;

        // Do the sequential test a number of times to warm it up, measure on last
        long timeBefore = 0;
        int numWarmups = 3;
        long seqTime = 0;
        double seqMax = 0;
        for (int i=0; i < numWarmups; i++) {
            timeBefore = System.currentTimeMillis();
            seqMax = sequentialMax(arr);
            seqTime = System.currentTimeMillis() - timeBefore;
        }

        double parallelMax = 0;
        long parallelTime = 0;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors: " + availableProcessors);
        // Do the parallel test a number of times to warm it up, measure on last
        for (int i=0; i < numWarmups; i++) {
            timeBefore = System.currentTimeMillis();
            parallelMax = MaxForkJoin.max(arr);
            parallelTime = System.currentTimeMillis() - timeBefore;
        }

        System.out.println("Sequential max = " + seqMax);
        System.out.println("Parallel max: " + parallelMax);

        System.out.println("Sequential time = " + seqTime);
        System.out.println("Parallel time = " + parallelTime);
        double speedup = ((double)seqTime)/parallelTime;
        System.out.println("Speedup = " + speedup);
        assertEquals(parallelMax, seqMax, 1e-9);

        if (InetAddress.getLocalHost().getHostName().startsWith("cmc")) {
            System.out.println("Running speedup test");
            assertTrue(parallelTime <= seqTime / ((2. / 3) * availableProcessors));
        }
    }
}
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class LongestSequenceTest {

    @Test
    public void testLongestSequence() throws Exception {
        int[] arr = {2, 17, 17, 8, 17, 17, 17, 0, 17, 1};
        int runSize = LongestSequenceForkJoin.longestSequence(17, arr);
        assertEquals(3, runSize);
    }
}

import org.junit.Test;

import ch.usi.dag.testing.JITTestCase;

public class PEATestCase extends JITTestCase {

    static final double PROBABILITY = 0.8;
    static final int ITERATIONS = 10000;
    static final double EPSILON = 0.02;

    static int counter = 0;

    @Override
    protected void warmup() {
        A.foo(likely(PROBABILITY));
    }

    @Override
    protected boolean isWarmedUp() {
        return counter > 0;
    }

    @Test
    public void testPartialEscape() {
        counter = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            A.foo(likely(PROBABILITY));
        }
        assertEquals(((double) counter) / ITERATIONS, PROBABILITY, EPSILON);
    }

}

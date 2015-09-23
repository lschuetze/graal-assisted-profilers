package query;

import static org.junit.Assert.assertEquals;
import jdk.internal.jvmci.hotspot.DontInline;

import org.junit.Test;

import pea.target.A;
import ch.usi.dag.testing.JITTestCase;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class LockType2 extends JITTestCase {

	public static final int PRED = -1;
	public static final int HERE = 0;
	public static final double LIKELY = 0.8;
	public static final int ITERATIONS = 10000;
	public static final int THREADS = 8;

	protected boolean isCompiled = false;
	protected int[] counters = new int[8];

	public int data = 0;

	@Override
	protected void warmup() {
		target();
	}

	@Override
	protected boolean isWarmedUp() {
		return isCompiled;
	}

	@DontInline
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (GraalQueryAPI.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		A a = new A();

		synchronized (a) {
			DelimitationAPI.instrumentationBegin(PRED);
			if (GraalQueryAPI.isMethodCompiled())
				profile(GraalQueryAPI.getLockType());
			DelimitationAPI.instrumentationEnd();

			if (likely(LIKELY)) {
				a.notInlinedMethod();
			}
		}
	}

	@DontInline
	private void profile(int lockType) {
		counters[lockType + 1]++;
	}

	public void run() {
		for (int i = 0; i < ITERATIONS; i++)
			target();
	}

	@Test
	public void test() {
		for (int i = 0; i < counters.length; i++)
			counters[i] = 0;

		Thread[] workers = new Thread[THREADS];

		for (int i = 0; i < THREADS; i++) {
			workers[i] = new Thread(this::run);
			workers[i].start();
		}

		for (int i = 0; i < THREADS; i++) {
			try {
				workers[i].join();
			} catch (InterruptedException e) {
			}
		}

		assertEquals(counters[0], 0);
	}

}

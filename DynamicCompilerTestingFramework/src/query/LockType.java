package query;

import static org.junit.Assert.assertEquals;
import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import jdk.internal.jvmci.debug.DontInline;

import org.junit.Test;

import ch.usi.dag.testing.JITTestCase;

public class LockType extends JITTestCase {

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
		if (CompilerDecision.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		synchronized (this) {
			DelimitationAPI.instrumentationBegin(PRED);
			if (CompilerDecision.isMethodCompiled())
				profile(CompilerDecision.getLockType());
			DelimitationAPI.instrumentationEnd();

			data++;
		}
	}

	@DontInline
	private void profile(int lockType) {
		counters[lockType]++;
	}

	public class Worker extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < ITERATIONS; i++)
				target();
		}
	}

	@Test
	public void test() {
		for (int i = 0; i < counters.length; i++)
			counters[i] = 0;

		for (int i = 0; i < THREADS; i++)
			new Worker().start();

		assertEquals(counters[0], 0);
	}

}

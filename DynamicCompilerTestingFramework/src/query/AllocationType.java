package query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import jdk.internal.jvmci.debug.DontInline;

import org.junit.Test;

import pea.target.A;
import ch.usi.dag.testing.JITTestCase;

public class AllocationType extends JITTestCase {

	public static final int PRED = -2;
	public static final int HERE = 0;
	public static final double LIKELY = 0.8;
	public static final int ITERATIONS = 10000;

	protected boolean isCompiled = false;
	protected int[] counters = new int[3];

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

		A a = new A();

		DelimitationAPI.instrumentationBegin(PRED);
		if (CompilerDecision.isMethodCompiled())
			profile(CompilerDecision.getAllocationType());
		DelimitationAPI.instrumentationEnd();

		if (likely(LIKELY))
			// This will not be inlined and let the receiver escape
			a.notInlinedMethod();
	}

	@DontInline
	private void profile(int allocationType) {
		counters[allocationType]++;
	}

	@Test
	public void test() {
		for (int i = 0; i < counters.length; i++)
			counters[i] = 0;

		for (int i = 0; i < ITERATIONS; i++)
			target();

		assertEquals(counters[0], 0);
		assertTrue(counters[1] + counters[2] > 0);
	}

}

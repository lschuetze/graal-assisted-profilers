package query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import jdk.internal.jvmci.debug.DontInline;

import org.junit.Test;

import ch.usi.dag.testing.JITTestCase;

public class RootName extends JITTestCase {

	public static final int HERE = 0;
	public static final int ITERATIONS = 10000;

	protected boolean isCompiled = false;
	protected HashMap<String, Integer> counters = new HashMap<>();

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

		empty();

		DelimitationAPI.instrumentationBegin(HERE);
		if (CompilerDecision.isMethodInlined())
			profile(CompilerDecision.getRootName());
		DelimitationAPI.instrumentationEnd();
	}

	public void empty() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (CompilerDecision.isMethodInlined())
			profile(CompilerDecision.getRootName());
		DelimitationAPI.instrumentationEnd();
	}

	@DontInline
	private void profile(String name) {
		counters.compute(name, (k, v) -> v == null ? 1 : v + 1);
	}

	@Test
	public void test() {
		counters.clear();

		for (int i = 0; i < ITERATIONS; i++)
			target();

		assertEquals(counters.keySet().size(), 1);
		assertTrue(counters.keySet().contains("query/RootName.target()V"));
	}

}

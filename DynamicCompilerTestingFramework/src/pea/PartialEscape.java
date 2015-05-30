package pea;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import target.A;

import com.oracle.graal.debug.external.CompilerDecision;
import com.oracle.graal.debug.external.DontInline;

import ch.usi.dag.testing.JITTestCase;

public class PartialEscape extends JITTestCase {

	private boolean isCompiled = false;
	private int counter = 0;

	@Override
	protected void warmup() {
		partialEscape(likely(Constants.PROBABILITY));
	}

	@Override
	protected boolean isWarmedUp() {
		return isCompiled;
	}

	@DontInline
	public void partialEscape(boolean invokeBar) {
		A a = new A();

		CompilerDecision.instrumentationBegin(Constants.PRED);
		if (CompilerDecision.isMethodCompiled()) {
			isCompiled = true;

			if (CompilerDecision.isHeapAlloc()) {
				counter++;
			}
		}

		CompilerDecision.instrumentationEnd();

		if (invokeBar) {
			// This will not be inlined and let the receiver escape
			a.bar();
		}
	}

	@Test
	public void testPartialEscape() {
		counter = 0;

		for (int i = 0; i < Constants.ITERATIONS; i++) {
			partialEscape(likely(Constants.PROBABILITY));
		}

		assertEquals(((double) counter) / Constants.ITERATIONS,
				Constants.PROBABILITY, Constants.EPSILON);
	}

}

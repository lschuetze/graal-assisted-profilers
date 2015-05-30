package pea;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import target.A;

import com.oracle.graal.debug.external.CompilerDecision;
import com.oracle.graal.debug.external.DontInline;

import ch.usi.dag.testing.JITTestCase;

public class NotEscape extends JITTestCase {

	private boolean isCompiled = false;
	private int counter = 0;

	@Override
	protected void warmup() {
		notEscape();
	}

	@Override
	protected boolean isWarmedUp() {
		return isCompiled;
	}

	@DontInline
	public void notEscape() {
		A a = new A();

		CompilerDecision.instrumentationBegin(Constants.PRED);

		if (CompilerDecision.isMethodCompiled()) {
			isCompiled = true;

			if (CompilerDecision.isHeapAlloc()) {
				counter++;
			}
		}

		CompilerDecision.instrumentationEnd();

		// This gets inlined and the receiver will not escape
		a.getV();
	}

	@Test
	public void testNotEscape() {
		counter = 0;

		for (int i = 0; i < Constants.ITERATIONS; i++) {
			notEscape();
		}

		assertEquals(counter, 0);
	}

}

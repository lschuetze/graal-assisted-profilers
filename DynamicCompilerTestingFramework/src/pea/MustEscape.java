package pea;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import target.A;
import ch.usi.dag.testing.JITTestCase;

import com.oracle.graal.debug.external.CompilerDecision;
import com.oracle.graal.debug.external.DontInline;

public class MustEscape extends JITTestCase {

	private boolean isCompiled = false;
	private int counter = 0;

	@Override
	protected void warmup() {
		mustEscape();
	}

	@Override
	protected boolean isWarmedUp() {
		return isCompiled;
	}

	@DontInline
	public void mustEscape() {
		A a = new A();

		CompilerDecision.instrumentationBegin(Constants.PRED);

		if (CompilerDecision.isMethodCompiled()) {
			isCompiled = true;

			if (CompilerDecision.isHeapAlloc()) {
				counter++;
			}
		}

		CompilerDecision.instrumentationEnd();

		// This will not be inlined and let the receiver escape
		a.bar();
	}

	@Test
	public void testMustEscape() {
		counter = 0;

		for (int i = 0; i < Constants.ITERATIONS; i++) {
			mustEscape();
		}

		assertEquals(counter, Constants.ITERATIONS);
	}

}

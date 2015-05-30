package inlining;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import target.A;

import com.oracle.graal.debug.external.CompilerDecision;
import com.oracle.graal.debug.external.DontInline;

import ch.usi.dag.testing.JITTestCase;

public class InlineComplicatedMethod extends JITTestCase {

	private boolean isCompiled = false;
	private int counter = 0;

	@Override
	protected void warmup() {
		inlineComplicatedMethod();
	}

	@Override
	protected boolean isWarmedUp() {
		return isCompiled;
	}

	@DontInline
	private void inlineComplicatedMethod() {
		A a = new A();

		a.complicatedMethod();

		CompilerDecision.instrumentationBegin(Constants.PRED);

		if (CompilerDecision.isMethodCompiled()) {
			isCompiled = true;

			if (CompilerDecision.isCallsiteInlined()) {
				counter++;
			}
		}

		CompilerDecision.instrumentationEnd();
	}

	@Test
	public void testInlineSimpleMethod() {
		counter = 0;

		for (int i = 0; i < Constants.ITERATIONS; i++) {
			inlineComplicatedMethod();
		}

		assertEquals(counter, Constants.ITERATIONS);
	}

}

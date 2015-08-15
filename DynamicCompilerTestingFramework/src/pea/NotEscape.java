package pea;

import static org.junit.Assert.assertEquals;
import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import jdk.internal.jvmci.debug.DontInline;
import pea.target.A;
import ch.usi.dag.testing.BaseTestCase;

public class NotEscape extends BaseTestCase implements Constants {

	@DontInline
	@Override
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (CompilerDecision.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		A a = new A();

		DelimitationAPI.instrumentationBegin(PRED);
		if (CompilerDecision.isMethodCompiled())
			counter++;
		DelimitationAPI.instrumentationEnd();
		// This gets inlined and the receiver will not escape
		a.empty();
	}

	@Override
	public void verify() {
		// Non-escaping allocations take place on the stack.
		assertEquals(counter, 0);
	}

}

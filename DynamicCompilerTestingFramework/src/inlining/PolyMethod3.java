package inlining;

import static org.junit.Assert.assertEquals;
import inlining.target.Simple;
import inlining.target.Super;
import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import jdk.internal.jvmci.debug.DontInline;
import ch.usi.dag.testing.BaseTestCase;

public class PolyMethod3 extends BaseTestCase implements Constants {

	@DontInline
	@Override
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (CompilerDecision.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		Super o = new Simple();

		if (likely(UNLIKELY)) {
			o.caculate(RandomGen.nextInt());

			DelimitationAPI.instrumentationBegin(PRED);
			if (CompilerDecision.isMethodCompiled())
				counter++;
			DelimitationAPI.instrumentationEnd();
		}
	}

	@Override
	public void verify() {
		assertEquals(counter, 0);
	}

}

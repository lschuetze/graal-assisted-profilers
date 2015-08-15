package inlining;

import static org.junit.Assert.assertEquals;
import inlining.target.Complicated;
import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import jdk.internal.jvmci.debug.DontInline;
import ch.usi.dag.testing.BaseTestCase;

public class ComplicatedMethod extends BaseTestCase implements Constants {

	@DontInline
	@Override
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (CompilerDecision.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		Complicated o = new Complicated();
		o.caculate(RandomGen.nextInt());

		DelimitationAPI.instrumentationBegin(PRED);
		if (CompilerDecision.isMethodCompiled())
			counter++;
		DelimitationAPI.instrumentationEnd();
	}

	@Override
	public void verify() {
		assertEquals(counter, 0);
	}

}

package pea;

import jdk.internal.jvmci.hotspot.DontInline;
import pea.target.A;
import ch.usi.dag.testing.BaseTestCase;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class NotEscape extends BaseTestCase implements Constants {

	@DontInline
	@Override
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (GraalQueryAPI.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		A a = new A();

		DelimitationAPI.instrumentationBegin(PRED);
		if (GraalQueryAPI.isMethodCompiled())
			counter++;
		DelimitationAPI.instrumentationEnd();
		// This gets inlined and the receiver will not escape
		a.empty();
	}

	@Override
	public double expectedRatio() {
		return NOT_ESCAPE;
	}

}

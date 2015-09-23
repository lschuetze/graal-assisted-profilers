package pea;

import jdk.internal.jvmci.hotspot.DontInline;
import pea.target.A;
import pea.target.B;
import ch.usi.dag.testing.BaseTestCase;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class PartialEscape2 extends BaseTestCase implements Constants {

	public static final double LIKELY = 0.8;
	public static final double EPSILON = 0.02;

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

		B b = new B(a);

		DelimitationAPI.instrumentationBegin(PRED);
		if (GraalQueryAPI.isMethodCompiled())
			counter++;
		DelimitationAPI.instrumentationEnd();

		if (likely(LIKELY))
			// This will not be inlined and let the receiver escape
			b.notInlinedMethod();
	}
	
	@Override
	public double expectedRatio() {
		return ESCAPE * LIKELY * 2;
	}

}

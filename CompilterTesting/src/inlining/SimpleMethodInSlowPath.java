package inlining;

import inlining.target.Simple;
import jdk.internal.jvmci.hotspot.DontInline;
import ch.usi.dag.testing.BaseTestCase;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class SimpleMethodInSlowPath extends BaseTestCase implements Constants {

	@DontInline
	@Override
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (GraalQueryAPI.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		Simple o = new Simple();

		if (likely(UNLIKELY)) {
			o.calculate(RandomGen.nextInt());

			DelimitationAPI.instrumentationBegin(PRED);
			if (GraalQueryAPI.isMethodCompiled())
				counter++;
			DelimitationAPI.instrumentationEnd();
		}
	}
	
	@Override
	public double expectedRatio() {
		return INLINE;
	}

}

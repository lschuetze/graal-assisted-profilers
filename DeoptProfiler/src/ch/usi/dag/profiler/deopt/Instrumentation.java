package ch.usi.dag.profiler.deopt;

import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class Instrumentation {

	@Before(marker = BodyMarker.class)
	static void profileDeopt(MethodStaticContext msc) {
		DelimitationAPI.instrumentationBegin(0, 1);
		if (CompilerDecision.isMethodCompiled()) {
			if (!CompilerDecision.isMethodInlined()) {
			Profiler.profileDeopt(msc.thisMethodFullName(),
					CompilerDecision.getRootName(),
					CompilerDecision.getDeoptAction(),
					CompilerDecision.getDeoptReason(),
					CompilerDecision.getDeoptBCI());
			}
		} else {
			Profiler.empty();
		}
		DelimitationAPI.instrumentationEnd();
	}

}

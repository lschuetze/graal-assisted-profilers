package ch.usi.dag.profiler.deopt;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class Instrumentation {

	@Before(marker = BodyMarker.class)
	static void profileDeopt(MethodStaticContext msc) {
		DelimitationAPI.instrumentationBegin(0);
		if (GraalQueryAPI.isMethodCompiled()) {
			if (!GraalQueryAPI.isMethodInlined()) {
				Profiler.profileDeopt(msc.thisMethodFullName(),
						GraalQueryAPI.getRootName(),
						GraalQueryAPI.getDeoptAction(),
						GraalQueryAPI.getDeoptReason(),
						GraalQueryAPI.getDeoptBCI());
			}
		} else {
			Profiler.empty();
		}
		DelimitationAPI.instrumentationEnd();
	}

}

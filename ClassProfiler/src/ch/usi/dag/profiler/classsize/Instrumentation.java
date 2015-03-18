package ch.usi.dag.profiler.classsize;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "new")
	static void profileAllocation(DynamicContext dc, TypeInsnContext tic,
			ClassContext cc) {
		Profiler.profileClass(tic.bci(), cc.asClass(tic.getTypeName()));
	}

}

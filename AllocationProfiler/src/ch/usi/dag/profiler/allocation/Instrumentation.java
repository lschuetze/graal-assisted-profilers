package ch.usi.dag.profiler.allocation;

import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BytecodeMarker;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "new", order = 0)
	static void profileActualAllocation(TypeInsnContext tic) {
		DelimitationAPI.instrumentationBegin(-1);
		Profiler.profileAlloc(tic.bci(), CompilerDecision.getAllocationType());
		DelimitationAPI.instrumentationEnd();
	}

	@AfterReturning(marker = BytecodeMarker.class, args = "new", order = 1)
	static void profileAllocation(TypeInsnContext tic) {
		DelimitationAPI.instrumentationBegin(0);
		Profiler.profileAlloc(tic.bci(), 3);
		DelimitationAPI.instrumentationEnd();
	}

}

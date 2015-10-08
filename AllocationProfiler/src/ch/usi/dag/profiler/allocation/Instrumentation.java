package ch.usi.dag.profiler.allocation;

import com.oracle.graal.api.directives.GraalDirectives;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BytecodeMarker;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "new", order = 0)
	static void profileActualAllocation(TypeInsnContext tic) {
		GraalDirectives.instrumentationBegin(-1);
		Profiler.profileAllocation(tic.bci(), GraalDirectives.runtimePath());
		GraalDirectives.instrumentationEnd();
	}

	@AfterReturning(marker = BytecodeMarker.class, args = "new", order = 1)
	static void profileAllocation(TypeInsnContext tic) {
		GraalDirectives.instrumentationBegin(0);
		Profiler.profileAllocation(tic.bci(), 2);
		GraalDirectives.instrumentationEnd();
	}

}

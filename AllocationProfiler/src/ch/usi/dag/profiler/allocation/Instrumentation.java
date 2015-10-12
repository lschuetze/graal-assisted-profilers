package ch.usi.dag.profiler.allocation;

import com.oracle.graal.api.directives.GraalDirectives;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.profiler.context.EnhancedBytecodeStaticContext;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "new", order = 0)
	static void profileActualAllocation(EnhancedBytecodeStaticContext context) {
		GraalDirectives.instrumentationBegin(-1);
		Profiler.profileAllocation(context.bci(), GraalDirectives.runtimePath());
		GraalDirectives.instrumentationEnd();
	}

	@AfterReturning(marker = BytecodeMarker.class, args = "new", order = 1)
	static void profileAllocation(EnhancedBytecodeStaticContext context) {
		GraalDirectives.instrumentationBegin(0);
		Profiler.profileAllocation(context.bci(), 2);
		GraalDirectives.instrumentationEnd();
	}

}

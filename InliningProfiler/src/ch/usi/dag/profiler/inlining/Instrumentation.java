package ch.usi.dag.profiler.inlining;

import com.oracle.graal.api.directives.GraalDirectives;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.profiler.context.EnhancedBytecodeStaticContext;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual, invokespecial, invokestatic, invokeinterface")
	public static void profileInvocation(EnhancedBytecodeStaticContext context) {
		GraalDirectives.instrumentationToInvokeBegin(-1);
		if (GraalDirectives.inCompiledCode()) {
			Profiler.profileInvocation(context.bciGraal());
		} else {
			// This is for resolving Profiler.class, no need to be thread-safe
			Profiler.itr_count++;
		}
		GraalDirectives.instrumentationEnd();
	}

}

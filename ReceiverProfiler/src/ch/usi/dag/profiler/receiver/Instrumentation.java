package ch.usi.dag.profiler.receiver;

import com.oracle.graal.api.directives.GraalDirectives;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.profiler.staticcontext.EnhancedBytecodeStaticContext;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual, invokeinterface")
	public static void profileInvocation(EnhancedBytecodeStaticContext context, ArgumentProcessorContext apc) {
		if (GraalDirectives.inCompiledCode()) {
			Profiler.profileInvocation(context.bciGraal(), apc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS));
		} else {
			// This is for resolving Profiler.class, no need to be thread-safe
			Profiler.itr_count++;
		}
	}

}

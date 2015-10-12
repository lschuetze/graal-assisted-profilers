package ch.usi.dag.profiler.classsize;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.profiler.context.EnhancedBytecodeStaticContext;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "new")
	static void profileAllocation(DynamicContext dc, EnhancedBytecodeStaticContext context, ClassContext cc) {
		Profiler.profileClass(context.bci(), cc.asClass(context.typeName()));
	}

}

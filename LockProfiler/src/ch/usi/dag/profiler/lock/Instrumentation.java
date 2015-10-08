package ch.usi.dag.profiler.lock;

import com.oracle.graal.api.directives.GraalDirectives;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.staticcontext.BytecodeStaticContext;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "monitorenter")
	static void profileAllocation(DynamicContext dc, BytecodeStaticContext context) {
		GraalDirectives.instrumentationBegin(-1);
		Profiler.profileLock(context.bci(), GraalDirectives.runtimePath());
		GraalDirectives.instrumentationEnd();
	}

	@Before(marker = BodyMarker.class, guard = MethodIsSynchronizedGuard.class)
	public static void onSynchronizedMethodEntry(BytecodeStaticContext context) {
		GraalDirectives.instrumentationBegin(-1);
		Profiler.profileLock(context.bci(), GraalDirectives.runtimePath());
		GraalDirectives.instrumentationEnd();
	}

}

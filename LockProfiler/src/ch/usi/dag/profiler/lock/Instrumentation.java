package ch.usi.dag.profiler.lock;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.staticcontext.BytecodeStaticContext;

import com.oracle.graal.debug.external.CompilerDecision;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "monitorenter")
	static void profileAllocation(DynamicContext dc, BytecodeStaticContext bsc) {
		Profiler.profileLock(bsc.bci(), CompilerDecision.getLockType());
	}

	@Before(marker = BodyMarker.class, guard = MethodIsSynchronizedGuard.class)
	public static void onSynchronizedMethodEntry(BytecodeStaticContext bsc) {
		Profiler.profileLock(bsc.bci(), CompilerDecision.getLockType());
	}

}

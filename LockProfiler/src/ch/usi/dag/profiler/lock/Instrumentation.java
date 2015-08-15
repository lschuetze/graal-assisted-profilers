package ch.usi.dag.profiler.lock;

import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.staticcontext.BytecodeStaticContext;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "monitorenter")
	static void profileAllocation(DynamicContext dc, BytecodeStaticContext bsc) {
		DelimitationAPI.instrumentationBegin(-1);
		Profiler.profileLock(bsc.bci(), CompilerDecision.getLockType());
		DelimitationAPI.instrumentationEnd();
	}

	@Before(marker = BodyMarker.class, guard = MethodIsSynchronizedGuard.class)
	public static void onSynchronizedMethodEntry(BytecodeStaticContext bsc) {
		DelimitationAPI.instrumentationBegin(-1);
		Profiler.profileLock(bsc.bci(), CompilerDecision.getLockType());
		DelimitationAPI.instrumentationEnd();
	}

}

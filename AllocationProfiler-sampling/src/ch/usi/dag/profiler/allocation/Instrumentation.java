package ch.usi.dag.profiler.allocation;

import jdk.internal.jvmci.debug.CompilerDecision;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;

public class Instrumentation {

	@ThreadLocal
	static int __samplingCounter;

	@ThreadLocal
	static AllocationProfile __samplingProfile;

	@AfterReturning(marker = BytecodeMarker.class, args = "new")
	static void profileAllocation(DynamicContext dc, TypeInsnContext tic) {
		Profiler.profileAlloc(tic.bci(), CompilerDecision.getAllocationType());
	}

	@AfterReturning(marker = BodyMarker.class, scope = "java.lang.Thread.<init>")
	static void initProfile() {
		__samplingCounter = 1000;
		__samplingProfile = null;
	}

}

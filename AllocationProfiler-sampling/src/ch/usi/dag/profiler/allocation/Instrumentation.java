package ch.usi.dag.profiler.allocation;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;

import com.oracle.graal.debug.external.CompilerDecision;

public class Instrumentation {

	@ThreadLocal
	static int __samplingCounter;

	@ThreadLocal
	static AllocationProfile __samplingProfile;

	@AfterReturning(marker = BytecodeMarker.class, args = "new")
	static void profileAllocation(DynamicContext dc, TypeInsnContext tic) {
		String key = tic.bci();

		if (CompilerDecision.isMethodCompiled()) {
			if (CompilerDecision.isAllocationVirtual()) {
				Profiler.profileAlloc(key, 3);
			} else {
				Profiler.profileAlloc(key, CompilerDecision.getAllocationType());
			}
		} else {
			Profiler.profileAlloc(key, 2);
		}
	}

	@AfterReturning(marker = BodyMarker.class, scope = "java.lang.Thread.<init>")
	static void initProfile() {
		__samplingCounter = 1000;
		__samplingProfile = null;
	}

}

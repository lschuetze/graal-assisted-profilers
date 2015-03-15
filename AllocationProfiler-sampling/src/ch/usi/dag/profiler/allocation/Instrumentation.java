package ch.usi.dag.profiler.allocation;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;

import com.oracle.graal.debug.external.CompilerDecision;

public class Instrumentation {

	@ThreadLocal
	static int __samplingCounter;

	@ThreadLocal
	static AllocationProfile __samplingProfile;

	@AfterReturning(marker = BytecodeMarker.class, args = "new")
	static void profileAllocation(DynamicContext dc, TypeInsnContext tic) {
		if (__samplingCounter-- <= 0) {
			__samplingCounter = 1000;
			String key = tic.bci();

			if (CompilerDecision.isMethodCompiled()) {
				if (CompilerDecision.isAllocationVirtual()) {
					Profiler.profileAlloc(__samplingProfile, key, 3);
				} else {
					Profiler.profileAlloc(__samplingProfile, key,
							CompilerDecision.getAllocationType());
				}
			} else {
				Profiler.profileAlloc(__samplingProfile, key, 2);
			}
		}
	}

}

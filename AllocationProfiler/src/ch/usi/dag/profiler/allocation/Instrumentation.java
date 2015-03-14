package ch.usi.dag.profiler.allocation;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;

import com.oracle.graal.debug.external.CompilerDecision;

public class Instrumentation {

	@ThreadLocal
	static int __samplingCounter;

	@AfterReturning(marker = BytecodeMarker.class, args = "new")
	static void profileAllocation(DynamicContext dc, TypeInsnContext tic,
			ClassContext cc) {
		if (__samplingCounter-- <= 0) {
			__samplingCounter = 1000;
			String key = tic.bci();
			Class<?> type = cc.asClass(tic.getTypeName());

			if (CompilerDecision.isMethodCompiled()) {
				if (CompilerDecision.isAllocationVirtual()) {
					Profiler.profileAlloc(key, type, 3);
				} else {
					Profiler.profileAlloc(key, type,
							CompilerDecision.getAllocationType());
				}
			} else {
				Profiler.profileAlloc(key, type, 2);
			}
		}
	}

}

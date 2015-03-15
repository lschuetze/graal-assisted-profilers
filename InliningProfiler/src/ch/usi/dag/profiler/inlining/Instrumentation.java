package ch.usi.dag.profiler.inlining;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BytecodeMarker;

import com.oracle.graal.debug.external.CompilerDecision;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual, invokespecial, invokestatic, invokeinterface")
	public static void profileInvocation(MethodInsnContext mic) {
		if (CompilerDecision.isMethodCompiled()) {
			if (!CompilerDecision.isCallsiteInlined()) {
				Profiler.profileInvocation(mic.bci());
			}
		} else {
			// This is for resolving Profiler.class
			Profiler.empty();
		}
	}

}

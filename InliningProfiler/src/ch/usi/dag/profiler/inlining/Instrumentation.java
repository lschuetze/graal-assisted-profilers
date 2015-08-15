package ch.usi.dag.profiler.inlining;

import jdk.internal.jvmci.debug.CompilerDecision;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BytecodeMarker;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual, invokespecial, invokestatic, invokeinterface")
	public static void profileInvocation(MethodInsnContext mic) {
		if (CompilerDecision.isMethodCompiled()) {
			Profiler.profileInvocation(mic.bci());
		} else {
			// This is for resolving Profiler.class
			Profiler.empty();
		}
	}

}

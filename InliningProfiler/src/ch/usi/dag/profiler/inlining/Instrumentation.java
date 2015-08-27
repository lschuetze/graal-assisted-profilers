package ch.usi.dag.profiler.inlining;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BytecodeMarker;

import com.oracle.graal.debug.query.GraalQueryAPI;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual, invokespecial, invokestatic, invokeinterface")
	public static void profileInvocation(MethodInsnContext mic) {
		if (GraalQueryAPI.isMethodCompiled()) {
			Profiler.profileInvocation(mic.bci());
		} else {
			// This is for resolving Profiler.class
			Profiler.empty();
		}
	}

}

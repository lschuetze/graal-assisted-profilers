package ch.usi.dag.profiler.inlining;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;

import com.oracle.graal.debug.external.CompilerDecision;

public class Instrumentation {

	@ThreadLocal
	static int __samplingCounter;

	@ThreadLocal
	static InliningProfile __samplingProfile;

	@AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual, invokespecial, invokestatic, invokeinterface")
	public static void profileInvocation(MethodInsnContext mic) {
		if (__samplingCounter-- <= 0) {
			__samplingCounter = 1000;
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

	@AfterReturning(marker = BodyMarker.class, scope = "java.lang.Thread.<init>")
	static void initProfile() {
		__samplingCounter = 1000;
		__samplingProfile = null;
	}

}

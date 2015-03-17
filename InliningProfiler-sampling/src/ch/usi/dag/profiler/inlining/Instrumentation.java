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

	@AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual, invokeinterface")
	public static void profileInvocation(MethodInsnContext mic) {
		if (CompilerDecision.isMethodCompiled()) {
			if (!CompilerDecision.isCallsiteInlined()) {
				Profiler.profileInvocation(mic.bci());
			}
		} else {
			// This is for resolving Profiler.class
			Profiler.itr_count++;
		}
	}

	@AfterReturning(marker = BodyMarker.class, scope = "java.lang.Thread.<init>")
	static void initProfile() {
		__samplingCounter = 1000;
		__samplingProfile = null;
	}

}

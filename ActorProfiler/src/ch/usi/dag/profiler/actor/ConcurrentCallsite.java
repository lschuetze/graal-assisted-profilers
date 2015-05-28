package ch.usi.dag.profiler.actor;

import ch.usi.dag.disl.annotation.GuardMethod;

public class ConcurrentCallsite {

	@GuardMethod
	public static boolean isApplicable(MethodInsnContext mic) {
		return mic.isConcurrentCallsite();
	}

}

package ch.usi.dag.profiler.receiver;

import ch.usi.dag.profiler.common.ThreadLocalProfile;

public class Profiler {

	public static int itr_count = 0;

	static final ThreadLocalProfile<CallsiteProfile> profile = ThreadLocalProfile.create(CallsiteProfile::new);

	public static void profileInvocation(String key, Object receiver) {
		// Do not inline the following assignment. The enclosing method is
		// relevant to the value.
		final StackTraceElement[] traces = new Exception().getStackTrace();
		profile.applyAtSite(key, p -> p.collect(receiver.getClass().getName(), traces));
	}

}

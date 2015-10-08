package ch.usi.dag.profiler.receiver;

import ch.usi.dag.profiler.threadlocal.ProfileSet;
import ch.usi.dag.profiler.threadlocal.SamplingClock;

public class Profiler {

	public static int itr_count = 0;

	static final ProfileSet<CallsiteProfile> profiler = ProfileSet.getInstance(CallsiteProfile::new);

	public static void profileInvocation(String key, Object receiver) {
		if (SamplingClock.shouldProfile()) {
			profiler.getSiteProfile(key).collect(receiver.getClass().getName(), new Exception().getStackTrace());
		}
	}

}

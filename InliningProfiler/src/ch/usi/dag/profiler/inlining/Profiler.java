package ch.usi.dag.profiler.inlining;

import ch.usi.dag.profiler.threadlocal.ProfileSet;
import ch.usi.dag.profiler.threadlocal.SamplingClock;
import ch.usi.dag.profiler.threadlocal.SingleCounterSiteProfile;

public class Profiler {

	public static int itr_count = 0;

	static final ProfileSet<SingleCounterSiteProfile> profiler = ProfileSet.getInstance(SingleCounterSiteProfile::new);

	public static void profileInvocation(String key) {
		if (SamplingClock.shouldProfile()) {
			profiler.getSiteProfile(key).increment();
		}
	}

}

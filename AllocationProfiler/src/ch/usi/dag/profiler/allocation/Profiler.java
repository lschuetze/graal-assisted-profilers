package ch.usi.dag.profiler.allocation;

import ch.usi.dag.profiler.threadlocal.MultiCounterSiteProfile;
import ch.usi.dag.profiler.threadlocal.ProfileSet;
import ch.usi.dag.profiler.threadlocal.SamplingClock;

public class Profiler {

	// case 0: interpreter
	// case 1: thread local allocation buffer
	// case 2: heap
	// case 3: all
	public static final int CASE = 4;

	static final ProfileSet<MultiCounterSiteProfile> profiler = ProfileSet
			.getInstance(() -> new MultiCounterSiteProfile(CASE));

	public static void profileAllocation(String key, int type) {
		if (SamplingClock.shouldProfile()) {
			profiler.getSiteProfile(key).increment(type + 1);
		}
	}
}

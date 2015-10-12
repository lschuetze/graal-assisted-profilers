package ch.usi.dag.profiler.allocation;

import ch.usi.dag.profiler.common.ThreadLocalProfile;
import ch.usi.dag.profiler.library.MultiCounterSiteProfile;

public class Profiler {

	// case 0: interpreter
	// case 1: thread local allocation buffer
	// case 2: heap
	// case 3: all
	public static final int CASE = 4;

	static final ThreadLocalProfile<MultiCounterSiteProfile> profile = ThreadLocalProfile
			.create(() -> new MultiCounterSiteProfile(CASE));

	public static void profileAllocation(String key, int type) {
		profile.applyAtSite(key, p -> p.increment(type + 1));
	}
}

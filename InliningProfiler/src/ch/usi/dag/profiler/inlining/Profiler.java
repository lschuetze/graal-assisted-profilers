package ch.usi.dag.profiler.inlining;

import ch.usi.dag.profiler.common.ThreadLocalProfile;
import ch.usi.dag.profiler.library.SingleCounterSiteProfile;

public class Profiler {

	public static int itr_count = 0;

	static final ThreadLocalProfile<SingleCounterSiteProfile> profile = ThreadLocalProfile
			.create(SingleCounterSiteProfile::new);

	public static void profileInvocation(String key) {
		profile.applyAtSite(key, SingleCounterSiteProfile::increment);
	}

}

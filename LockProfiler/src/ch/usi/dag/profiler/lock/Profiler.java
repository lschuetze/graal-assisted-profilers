package ch.usi.dag.profiler.lock;

import ch.usi.dag.profiler.threadlocal.MultiCounterSiteProfile;
import ch.usi.dag.profiler.threadlocal.ProfileSet;
import ch.usi.dag.profiler.threadlocal.SamplingClock;

public class Profiler {

	// case 0: Interpreter
	// case 1: +lock{bias:existing}
	// case 2: +lock{bias:acquired}
	// case 3: +lock{bias:transfer}
	// case 4: +lock{stub:revoke stub:epoch-expired}
	// case 5: +lock{stub:failed-cas}
	// case 6: +lock{recursive}
	// case 7: +lock{cas}
	public static final int CASE = 8;

	static final ProfileSet<MultiCounterSiteProfile> profiler = ProfileSet
			.getInstance(() -> new MultiCounterSiteProfile(CASE));

	public static void profileLock(String key, int type) {
		if (SamplingClock.shouldProfile()) {
			profiler.getSiteProfile(key).increment(type + 1);
		}
	}

}

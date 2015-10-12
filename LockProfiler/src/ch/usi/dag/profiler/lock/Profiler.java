package ch.usi.dag.profiler.lock;

import ch.usi.dag.profiler.common.ThreadLocalProfile;
import ch.usi.dag.profiler.library.MultiCounterSiteProfile;

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

	static final ThreadLocalProfile<MultiCounterSiteProfile> profile = ThreadLocalProfile
			.create(() -> new MultiCounterSiteProfile(CASE));

	public static void profileLock(String key, int type) {
		profile.applyAtSite(key, p -> p.increment(type + 1));
	}

}

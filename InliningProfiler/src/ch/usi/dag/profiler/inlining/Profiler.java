package ch.usi.dag.profiler.inlining;

import ch.usi.dag.profiler.threadlocal.ProfileSet;
import ch.usi.dag.profiler.threadlocal.SamplingClock;
import ch.usi.dag.profiler.threadlocal.SiteProfile;

public class Profiler {

	public static int itr_count = 0;
	
	static final ProfileSet<CallSiteProfile> profiler = new ProfileSet<>(CallSiteProfile::new);
	
	public static void profileInvocation(String key) {
		if (SamplingClock.shouldProfile()) {
			CallSiteProfile siteProfile = profiler.getSiteProfile(key);
			siteProfile.increment();
		}
	}

	static class CallSiteProfile implements SiteProfile<CallSiteProfile> {

		int counter = 0;

		public void increment() {
			counter++;
		}

		@Override
		public CallSiteProfile copy() {
			CallSiteProfile copy = new CallSiteProfile();
			copy.merge(this);
			return copy;
		}

		@Override
		public void merge(CallSiteProfile other) {
			counter += other.counter;
		}

		@Override
		public String toString() {
			return Integer.toString(counter);
		}

	}

}

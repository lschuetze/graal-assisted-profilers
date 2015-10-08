package ch.usi.dag.profiler.allocation;

import ch.usi.dag.profiler.threadlocal.ProfileSet;
import ch.usi.dag.profiler.threadlocal.SamplingClock;
import ch.usi.dag.profiler.threadlocal.SiteProfile;

public class Profiler {

	static final ProfileSet<AllocationSiteProfile> profiler = new ProfileSet<>(AllocationSiteProfile::new);

	public static void profileAllocation(String key, int type) {
		if (SamplingClock.shouldProfile()) {
			AllocationSiteProfile siteProfile = profiler.getSiteProfile(key);
			siteProfile.increment(type);
		}
	}

	static class AllocationSiteProfile implements SiteProfile<AllocationSiteProfile> {

		// case 0: interpreter
		// case 1: thread local allocation buffer
		// case 2: heap
		// case 3: all
		public static final int CASE = 4;

		int[] counters = new int[CASE];

		public void increment(int type) {
			counters[type + 1]++;
		}

		@Override
		public AllocationSiteProfile copy() {
			AllocationSiteProfile copy = new AllocationSiteProfile();
			copy.merge(this);
			return copy;
		}

		@Override
		public void merge(AllocationSiteProfile other) {
			for (int i = 0; i < CASE; i++) {
				counters[i] += other.counters[i];
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(counters[0]);
			for (int i = 1; i < CASE; i++) {
				builder.append(' ');
				builder.append(counters[i]);
			}
			return builder.toString();
		}

	}

}

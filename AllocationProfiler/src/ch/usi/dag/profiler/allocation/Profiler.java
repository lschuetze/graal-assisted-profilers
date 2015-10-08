package ch.usi.dag.profiler.allocation;

import java.util.function.Supplier;

import ch.usi.dag.profiler.threadlocal.MetaProfile;
import ch.usi.dag.profiler.threadlocal.ProfileSet;
import ch.usi.dag.profiler.threadlocal.SamplingClock;
import ch.usi.dag.profiler.threadlocal.SiteProfile;

public class Profiler {

	static final Supplier<MetaProfile<AllocationSiteProfile>> NEW_METAPROFILE = MetaProfile::new;
	static final Supplier<AllocationSiteProfile> NEW_SITEPROFILE = AllocationSiteProfile::new;

	public static void profileAllocation(String key, int type) {
		if (SamplingClock.shouldProfile()) {
			MetaProfile<AllocationSiteProfile> metaProfile = ProfileSet.getProfile(NEW_METAPROFILE);
			AllocationSiteProfile siteProfile = metaProfile.getProfile(key, NEW_SITEPROFILE);
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

	static {
		ProfileSet.dumpToTTYAtShutdownIfEnable();
	}

}

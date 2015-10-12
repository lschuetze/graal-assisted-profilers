package ch.usi.dag.profiler.library;

import ch.usi.dag.profiler.common.SiteProfile;

public class MultiCounterSiteProfile implements SiteProfile<MultiCounterSiteProfile> {

	private final int[] counters;

	public MultiCounterSiteProfile(int length) {
		this.counters = new int[length];
	}

	public void increment(int index) {
		counters[index]++;
	}

	@Override
	public MultiCounterSiteProfile copy() {
		return new MultiCounterSiteProfile(counters.length).merge(this);
	}

	@Override
	public MultiCounterSiteProfile merge(MultiCounterSiteProfile other) {
		for (int i = 0; i < counters.length; i++) {
			counters[i] += other.counters[i];
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(counters[0]);
		for (int i = 1; i < counters.length; i++) {
			builder.append(' ');
			builder.append(counters[i]);
		}
		return builder.toString();
	}

}

package ch.usi.dag.profiler.library;

import ch.usi.dag.profiler.common.SiteProfile;

public class SingleCounterSiteProfile implements SiteProfile<SingleCounterSiteProfile> {

	private int counter = 0;

	public void increment() {
		counter++;
	}

	@Override
	public SingleCounterSiteProfile copy() {
		return new SingleCounterSiteProfile().merge(this);
	}

	@Override
	public SingleCounterSiteProfile merge(SingleCounterSiteProfile other) {
		counter += other.counter;
		return this;

	}

	@Override
	public String toString() {
		return Integer.toString(counter);
	}

}

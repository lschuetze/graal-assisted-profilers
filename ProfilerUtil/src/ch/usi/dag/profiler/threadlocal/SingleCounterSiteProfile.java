package ch.usi.dag.profiler.threadlocal;

public class SingleCounterSiteProfile implements SiteProfile<SingleCounterSiteProfile> {

	private int counter = 0;

	public void increment() {
		counter++;
	}

	@Override
	public SingleCounterSiteProfile copy() {
		SingleCounterSiteProfile copy = new SingleCounterSiteProfile();
		copy.merge(this);
		return copy;
	}

	@Override
	public void merge(SingleCounterSiteProfile other) {
		counter += other.counter;

	}

	@Override
	public String toString() {
		return Integer.toString(counter);
	}

}

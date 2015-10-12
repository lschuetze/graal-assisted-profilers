package ch.usi.dag.profiler.util;

public class SamplingClock {

	private final static int STRIDE = Integer.getInteger("stride", 0);

	public static boolean shouldProfile() {
		Thread current = Thread.currentThread();
		if (STRIDE > 0) {
			// use sampling
			if (current.__samplingCounter-- > 0) {
				return false;
			}
			current.__samplingCounter = STRIDE;
		}
		return true;
	}

}

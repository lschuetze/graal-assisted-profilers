package ch.usi.dag.profiler.inlining;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.meta.ConcurrentCounterMap;

public class Profiler {

	public static final ConcurrentCounterMap notInlinedCounters = new ConcurrentCounterMap();

	public static void clearProfile() {
		notInlinedCounters.clear();
	}

	public static void dumpProfile(String name) {
		try (Dumper dumper = new ArchiveDumper(name)) {
			notInlinedCounters.dump(dumper);
		}
	}

	public static void profileInvocation(String bci) {
		notInlinedCounters.increment(bci);
	}

	public static void empty() {
	}

}

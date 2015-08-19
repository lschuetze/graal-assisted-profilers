package ch.usi.dag.profiler.deopt;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.meta.ConcurrentCounterMap;

public class Profiler {

	public static final ConcurrentCounterMap counter = new ConcurrentCounterMap();

	public static void clearProfile() {
		counter.clear();
	}

	public static void dumpProfile(String name) {
		try (Dumper dumper = new ArchiveDumper(name)) {
			counter.dump(dumper);
		}
	}

	public static void profileDeopt(String methodID, String rootName,
			int deoptAction, int deoptReason, int deoptBCI) {
		counter.increment(methodID + " " + rootName + " " + deoptAction + " "
				+ deoptReason + " " + deoptBCI);
	}

	public static void empty() {
	}

}

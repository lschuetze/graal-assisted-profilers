package ch.usi.dag.profiler.allocation;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.meta.ConcurrentCounterMap;

public class Profiler {

	public static final int CASE = 4;
	// case 0: interpreter
	// case 1: thread local allocation buffer
	// case 2: heap
	// case 3: all

	public static final ConcurrentCounterMap counters[] = new ConcurrentCounterMap[CASE];

	static {
		initProfile();
	}

	public static void initProfile() {
		for (int i = 0; i < CASE; i++) {
			counters[i] = new ConcurrentCounterMap();
		}
	}

	public static void clearProfile() {
		for (int i = 0; i < CASE; i++) {
			counters[i].clear();
		}
	}

	public static void dumpProfile(String name) {
		try (Dumper dumper = new ArchiveDumper(name)) {
			ConcurrentCounterMap.compare(dumper, counters);
		}
	}

	public static void profileAlloc(String key, int type) {
		counters[type].increment(key);
	}
	
}

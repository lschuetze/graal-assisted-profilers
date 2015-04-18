package ch.usi.dag.profiler.lock;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.meta.ConcurrentCounterMap;

public class Profiler {
	
	public static final int CASE = 9;
	// case 0: error
	// case 1: 
	// case 2: 
	// case 3: 
	// case 4: 
	// case 5: 
	// case 6: 
	// case 7: 
	// case 8: 

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

	public static void profileLock(String key, int type) {
		counters[type].increment(key);
	}

}

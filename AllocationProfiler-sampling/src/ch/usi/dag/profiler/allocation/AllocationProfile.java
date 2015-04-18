package ch.usi.dag.profiler.allocation;

import java.util.HashSet;

import ch.usi.dag.profiler.meta.CounterMap;

public class AllocationProfile {

	public static final int CASE = 5;
	// case 0: error
	// case 1: thread local allocation buffer
	// case 2: heap
	// case 3: interpreter
	// case 4: virtual

	public CounterMap[] counters = new CounterMap[CASE];

	public AllocationProfile() {
		initProfile();
	}

	public void profileAlloc(String key, int type) {
		counters[type].increment(key);
	}

	public void initProfile() {
		for (int i = 0; i < CASE; i++) {
			counters[i] = new CounterMap();
		}
	}

	public void collectKeys(HashSet<String> keys) {
		for (int i = 0; i < CASE; i++) {
			keys.addAll(counters[i].keySet());
		}
	}

	public void collectCounters(String key, int[] summary) {
		for (int i = 0; i < CASE; i++) {
			summary[i] += counters[i].getOrDefault(key, 0);
		}
	}

}

package ch.usi.dag.profiler.inlining;

import java.util.HashMap;

public class InliningProfile {

	public HashMap<String, Integer> counter;

	public InliningProfile() {
		clear();
	}

	public void clear() {
		counter = new HashMap<>();
	}

	public void increment(String key) {
		Integer count = counter.get(key);
		counter.put(key, count == null ? 1 : count + 1);
	}
}

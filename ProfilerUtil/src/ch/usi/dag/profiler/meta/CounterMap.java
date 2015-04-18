package ch.usi.dag.profiler.meta;

import java.util.HashMap;

@SuppressWarnings("serial")
public class CounterMap extends HashMap<String, Integer> {

	public void increment(String key) {
		compute(key, (k, v) -> v == null ? 1 : v + 1);
	}

}
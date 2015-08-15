package ch.usi.dag.profiler.receiver;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ReceiverProfile {

	public HashMap<String, Integer> counter = new HashMap<>();

	public void increment(String receiver) {
		counter.compute(receiver, (k, v) -> (v == null) ? 1 : v + 1);
	}

	public void join(ReceiverProfile other) {
		for (Entry<String, Integer> entry : other.counter.entrySet()) {
			counter.compute(entry.getKey(), (k, v) -> entry.getValue()
					+ ((v == null) ? 0 : v));
		}
	}

	@Override
	public String toString() {
		return counter.values().stream().mapToInt(Integer::intValue).sum()
				+ " "
				+ counter.entrySet().stream().sorted(Entry.comparingByKey())
						.map(Entry::toString).collect(Collectors.joining("#"));
	}

}

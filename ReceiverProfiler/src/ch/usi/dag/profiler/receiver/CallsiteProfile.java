package ch.usi.dag.profiler.receiver;

import java.util.HashMap;
import java.util.Map.Entry;

public class CallsiteProfile {

	public HashMap<String, ReceiverProfile> counterL0;
	public HashMap<String, ReceiverProfile> counterL1;
	public HashMap<String, ReceiverProfile> counterL2;
	public HashMap<String, ReceiverProfile> counterL3;

	public CallsiteProfile() {
		clear();
	}

	public void clear() {
		counterL0 = new HashMap<>();
		counterL1 = new HashMap<>();
		counterL2 = new HashMap<>();
		counterL3 = new HashMap<>();
	}

	public void join(CallsiteProfile other) {
		for (Entry<String, ReceiverProfile> entry : other.counterL0.entrySet()) {
			counterL0.computeIfAbsent(entry.getKey(),
					key -> new ReceiverProfile()).join(entry.getValue());
		}

		for (Entry<String, ReceiverProfile> entry : other.counterL1.entrySet()) {
			counterL1.computeIfAbsent(entry.getKey(),
					key -> new ReceiverProfile()).join(entry.getValue());
		}

		for (Entry<String, ReceiverProfile> entry : other.counterL2.entrySet()) {
			counterL2.computeIfAbsent(entry.getKey(),
					key -> new ReceiverProfile()).join(entry.getValue());
		}

		for (Entry<String, ReceiverProfile> entry : other.counterL3.entrySet()) {
			counterL3.computeIfAbsent(entry.getKey(),
					key -> new ReceiverProfile()).join(entry.getValue());
		}
	}

}

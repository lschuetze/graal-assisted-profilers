package ch.usi.dag.profiler.receiver;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import ch.usi.dag.profiler.threadlocal.SingleCounterSiteProfile;
import ch.usi.dag.profiler.threadlocal.SiteProfile;

public class CallsiteProfile implements SiteProfile<CallsiteProfile> {

	public static final int STACK_OFFSET = 2;
	public static final int TOP_LEVEL = 3;

	private HashMap<String, SingleCounterSiteProfile> receivers = new HashMap<>();
	private HashMap<String, CallsiteProfile> nextLevel = new HashMap<>();
	private final int level;

	public CallsiteProfile() {
		level = 0;
	}

	private CallsiteProfile(int level) {
		this.level = level;
	}

	public void collect(String klass, StackTraceElement[] traces) {
		receivers.computeIfAbsent(klass, k -> new SingleCounterSiteProfile()).increment();
		if (level <= TOP_LEVEL && traces.length > level + STACK_OFFSET) {
			String context = traces[level + STACK_OFFSET].toString();
			nextLevel.computeIfAbsent(context, k -> new CallsiteProfile(level + 1)).collect(klass, traces);
		}
	}

	@Override
	public CallsiteProfile copy() {
		return new CallsiteProfile(level).merge(this);
	}

	@Override
	public CallsiteProfile merge(CallsiteProfile other) {
		for (Entry<String, SingleCounterSiteProfile> entry : receivers.entrySet()) {
			receivers.computeIfAbsent(entry.getKey(), k -> new SingleCounterSiteProfile()).merge(entry.getValue());
		}

		for (Entry<String, CallsiteProfile> entry : nextLevel.entrySet()) {
			nextLevel.computeIfAbsent(entry.getKey(), k -> new CallsiteProfile(entry.getValue().level))
					.merge(entry.getValue());
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('<');
		builder.append(receivers.entrySet().stream().sorted(Entry.comparingByKey()).map(Entry::toString)
				.collect(Collectors.joining("#")));
		builder.append(' ');
		builder.append(nextLevel.entrySet().stream().sorted(Entry.comparingByKey()).map(e -> e.getValue().toString())
				.collect(Collectors.joining(" ")));
		builder.append('>');
		return builder.toString();
	}

}

package ch.usi.dag.profiler.meta;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import ch.usi.dag.profiler.dump.Dumper;

@SuppressWarnings("serial")
public class ConcurrentCounterMap extends ConcurrentHashMap<String, AtomicLong> {

	public void increment(String key) {
		computeIfAbsent(key, input -> new AtomicLong()).incrementAndGet();
	}

	public long sum() {
		return values().stream().mapToLong(AtomicLong::get).sum();
	}

	public void dump(Dumper dumper) {
		entrySet().stream().sorted(Map.Entry.comparingByKey())
				.forEach(dumper::println);
	}

	public static void compare(Dumper dumper, ConcurrentCounterMap... maps) {
		final AtomicLong zero = new AtomicLong();
		final HashSet<String> keys = Arrays.stream(maps).collect(
				HashSet<String>::new, (set, map) -> set.addAll(map.keySet()),
				HashSet<String>::addAll);

		keys.stream()
				.sorted()
				.map(key -> {
					return Arrays
							.stream(maps)
							.reduce(new StringBuilder(key),
									(builder, map) -> {
										builder.append(' ');
										return builder.append(map.getOrDefault(
												key, zero).get());
									}, StringBuilder::append).toString();

				}).forEach(dumper::println);
	}

}

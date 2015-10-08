package ch.usi.dag.profiler.classsize;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.util.ObjectSizeEvaluator;

public class Profiler {

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(Profiler::dump));
	}

	public static final ConcurrentHashMap<String, Integer> types = new ConcurrentHashMap<>();

	public static void dump() {
		try (Dumper dumper = new ArchiveDumper("class-size")) {
			types.entrySet().stream().sorted(Entry.comparingByKey()).map(e -> e.getKey() + " " + e.getValue())
					.forEach(dumper::println);
		}
	}

	public static void profileClass(String bci, Class<?> type) {
		types.computeIfAbsent(bci, key -> ObjectSizeEvaluator.sizeof(type));
	}

}

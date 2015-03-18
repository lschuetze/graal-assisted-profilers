package ch.usi.dag.profiler.classsize;

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
			types.keySet().stream().forEach(type -> {
				dumper.println(type + " " + types.get(type));
			});
		}
	}

	public static void profileClass(String bci, Class<?> type) {
		types.computeIfAbsent(bci, key -> ObjectSizeEvaluator.sizeof(type));
	}

}

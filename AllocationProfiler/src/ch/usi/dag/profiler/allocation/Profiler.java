package ch.usi.dag.profiler.allocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.meta.ConcurrentCounterMap;
import ch.usi.dag.profiler.util.ObjectSizeEvaluator;

public class Profiler {

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(Profiler::dumpMeta));
	}

	public static final ConcurrentCounterMap errorAlloc = new ConcurrentCounterMap();
	public static final ConcurrentCounterMap itrprAlloc = new ConcurrentCounterMap();
	public static final ConcurrentCounterMap tlabAlloc = new ConcurrentCounterMap();
	public static final ConcurrentCounterMap heapAlloc = new ConcurrentCounterMap();
	public static final ConcurrentCounterMap virtAlloc = new ConcurrentCounterMap();

	public static final ConcurrentHashMap<String, Integer> types = new ConcurrentHashMap<String, Integer>();

	public static void clearProfile() {
		errorAlloc.clear();
		itrprAlloc.clear();
		tlabAlloc.clear();
		heapAlloc.clear();
		virtAlloc.clear();
	}

	public static void dumpProfile(String name) {
		try (Dumper dumper = new ArchiveDumper(name)) {
			ConcurrentCounterMap.compare(dumper, itrprAlloc, tlabAlloc,
					heapAlloc, virtAlloc, errorAlloc);
		}
	}

	public static void dumpMeta() {
		try (Dumper dumper = new ArchiveDumper("meta")) {
			types.entrySet().stream().sorted(Map.Entry.comparingByKey())
					.forEach(dumper::println);
		}
	}

	public static final AtomicInteger counter = new AtomicInteger();
	public static final int STRIDE = 1000;

	public static void profileAlloc(String key, Class<?> allocType, int type) {
		if (counter.get() < STRIDE) {
			counter.incrementAndGet();
		} else if (counter.compareAndSet(STRIDE, 0)) {
			ConcurrentCounterMap which = null;

			switch (type) {
			case 0:
				which = tlabAlloc;
				break;
			case 1:
				which = heapAlloc;
				break;
			case 2:
				which = itrprAlloc;
				break;
			case 3:
				which = virtAlloc;
				break;
			default:
				which = errorAlloc;
			}

			which.increment(key);

			types.computeIfAbsent(key,
					k -> ObjectSizeEvaluator.sizeof(allocType));
		}
	}

}

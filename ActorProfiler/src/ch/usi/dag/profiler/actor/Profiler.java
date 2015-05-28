package ch.usi.dag.profiler.actor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.dump.TTYDumper;

public class Profiler {

	public static class IntPair {
		public Object key;
		public int value;

		public IntPair(Object key, int value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return key.toString() + " " + value;
		}
	}

	public static final long START = System.nanoTime();
	public static final long MS = 1000000;
	public static final long STRIDE = 100 * MS;

	public static ConcurrentHashMap<Object, ConcurrentHashMap<Integer, Integer>> enqueues;
	public static ConcurrentHashMap<Object, ConcurrentHashMap<Integer, Integer>> dequeues;

	static {
		enqueues = new ConcurrentHashMap<>();
		dequeues = new ConcurrentHashMap<>();

		Runtime.getRuntime().addShutdownHook(new Thread(Profiler::dump));
	}

	private static int maximal(ConcurrentHashMap<Integer, Integer> distribute) {
		return distribute.values().stream().mapToInt(Integer::intValue).max().getAsInt();
	}

	public static IntPair max(
			ConcurrentHashMap<Object, ConcurrentHashMap<Integer, Integer>> map) {
		return map.entrySet().stream()
				.map(t -> new IntPair(t.getKey(), maximal(t.getValue())))
				.max((p1, p2) -> p1.value - p2.value).get();
	}

	public static void max() {
		try (Dumper dumper = new TTYDumper()) {
			dumper.println("Hotest enqueue: " + max(enqueues));
		}
	}
	
	public static int sum(ConcurrentHashMap<Integer, Integer> distribute) {
		return distribute.values().stream().mapToInt(Integer::intValue).sum();
	}

	public static void sum() {
		try (Dumper dumper = new TTYDumper()) {
			enqueues.entrySet().forEach(
					t -> dumper.println(t.getKey() + " " + sum(t.getValue())));
		}
	}

	public static void distribute() {
		try (Dumper dumper = new TTYDumper()) {
			int current = getStride();
			dumper.println("Enqueue");

			for (Object actor : enqueues.keySet()) {
				ConcurrentHashMap<Integer, Integer> distribute = enqueues
						.get(actor);
				String temp = IntStream
						.range(0, current)
						.mapToObj(i -> distribute.getOrDefault(i, 0).toString())
						.collect(Collectors.joining(" "));
				dumper.println(actor + " " + temp);
			}
		}
	}

	public static void dump() {
		distribute();
		sum();
		max();
	}

	private static int getStride() {
		return (int) ((System.nanoTime() - START) / STRIDE);
	}

	private static void increment(
			ConcurrentHashMap<Object, ConcurrentHashMap<Integer, Integer>> map,
			Object actor) {
		map.computeIfAbsent(actor, input -> new ConcurrentHashMap<>()).compute(
				getStride(), (k, v) -> v == null ? 1 : v + 1);
	}

	public static void enqueue(Object actor) {
		increment(enqueues, actor);
	}

	public static void dequeue(Object actor) {
		increment(dequeues, actor);
	}

}

package ch.usi.dag.profiler.inlining;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;

public class Profiler {

	public static final int STRIDE = 1000;
	public static final int COUNTER_SIZE = 2 << 18;

	public static final int[] DCC_COUNTERS = new int[COUNTER_SIZE];
	public static final int[] INT_COUNTERS = new int[COUNTER_SIZE];

	public static void clearProfile() {
		for (int i = 0; i < COUNTER_SIZE; i++) {
			DCC_COUNTERS[i] = 0;
			INT_COUNTERS[i] = 0;
		}
	}

	public static void dumpProfile(String name) {
		try (Dumper dumper = new ArchiveDumper(name)) {
			for (int i = 0; i < COUNTER_SIZE; i++) {
				int v = DCC_COUNTERS[i];

				if (v > 0) {
					dumper.println(i + " " + v);
					DCC_COUNTERS[i] = 0;
				}
			}
		}
	}

}

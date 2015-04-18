package ch.usi.dag.profiler.allocation;

import java.util.HashSet;
import java.util.LinkedList;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;

public class Profiler {

	public static final LinkedList<AllocationProfile> profiles = new LinkedList<>();

	public static void clearProfile() {
		synchronized (profiles) {
			for (AllocationProfile profile : profiles) {
				profile.initProfile();
			}
		}
	}

	public static void dumpProfile(String name) {
		try (Dumper dumper = new ArchiveDumper(name)) {
			synchronized (profiles) {
				final HashSet<String> keys = new HashSet<>();

				for (AllocationProfile profile : profiles) {
					profile.collectKeys(keys);
				}

				keys.stream().sorted().forEach(key -> {
					int[] counters = new int[AllocationProfile.CASE];

					for (AllocationProfile profile : profiles) {
						profile.collectCounters(key, counters);
					}

					StringBuilder builder = new StringBuilder(key);

					for (int i = 0; i < AllocationProfile.CASE; i++) {
						builder.append(' ');
						builder.append(counters[i]);
					}

					dumper.println(builder.toString());
				});
			}
		}
	}

	public static void profileAlloc(String key, int type) {
		Thread current = Thread.currentThread();

		if (current.__samplingCounter-- <= 0) {
			current.__samplingCounter = 1000;
			AllocationProfile profile = current.__samplingProfile;

			if (profile == null) {
				profile = new AllocationProfile();

				synchronized (profiles) {
					profiles.add(profile);
				}

				current.__samplingProfile = profile;
			}

			profile.profileAlloc(key, type);
		}
	}

}

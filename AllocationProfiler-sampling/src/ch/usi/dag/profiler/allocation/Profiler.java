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
				profile.clearProfile();
			}
		}
	}

	public static void dumpProfile(String name) {
		synchronized (profiles) {
			try (Dumper dumper = new ArchiveDumper(name)) {
				final HashSet<String> keys = new HashSet<>();

				for (AllocationProfile profile : profiles) {
					profile.collectKeys(keys);
				}

				keys.stream().sorted().forEach(key -> {
					AllocationCounter counter = new AllocationCounter();

					for (AllocationProfile profile : profiles) {
						profile.collectCounters(key, counter);
					}

					StringBuilder builder = new StringBuilder(key);
					builder.append(' ');
					builder.append(counter.toString());
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

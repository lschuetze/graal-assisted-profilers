package ch.usi.dag.profiler.inlining;

import java.util.HashSet;
import java.util.LinkedList;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.meta.ConcurrentCounterMap;

public class Profiler {

	public static final LinkedList<InliningProfile> profiles = new LinkedList<>();

	public static final ConcurrentCounterMap notInlinedCounters = new ConcurrentCounterMap();

	public static void clearProfile() {
		synchronized (profiles) {
			for (InliningProfile profile : profiles) {
				profile.clear();
			}
		}
	}

	public static void dumpProfile(String name) {
		try (Dumper dumper = new ArchiveDumper(name)) {
			synchronized (profiles) {
				final HashSet<String> keys = new HashSet<>();

				for (InliningProfile profile : profiles) {
					keys.addAll(profile.counter.keySet());
				}

				keys.stream().sorted().forEach(key -> {
					int counter = 0;

					for (InliningProfile profile : profiles) {
						Integer current = profile.counter.get(key);

						if (current != null) {
							counter += current;
						}
					}

					StringBuilder builder = new StringBuilder(key);
					builder.append(' ');
					builder.append(counter);
					dumper.println(builder.toString());
				});
			}
		}
	}

	public static void profileInvocation(String bci) {
		Thread current = Thread.currentThread();

		if (current.__samplingCounter-- <= 0) {
			current.__samplingCounter = 1000;

			InliningProfile profile = current.__samplingProfile;

			if (profile == null) {
				profile = new InliningProfile();

				synchronized (profiles) {
					profiles.add(profile);
				}

				current.__samplingProfile = profile;
			}

			profile.increment(bci);
		}
	}

	public static void empty() {
	}

}

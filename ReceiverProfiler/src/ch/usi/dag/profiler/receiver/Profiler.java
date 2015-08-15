package ch.usi.dag.profiler.receiver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;

public class Profiler {

	public static int itr_count = 0;

	public static final LinkedList<CallsiteProfile> profiles = new LinkedList<>();

	public static void clearProfile() {
		synchronized (profiles) {
			for (CallsiteProfile profile : profiles) {
				profile.clear();
			}
		}
		itr_count = 0;
	}

	public static void dumpTo(HashMap<String, ReceiverProfile> counter,
			String name) {
		try (Dumper dumper = new ArchiveDumper(name)) {
			counter.entrySet()
					.stream()
					.sorted(Entry.comparingByKey())
					.forEach(
							entry -> {
								dumper.println(entry.getKey() + " "
										+ entry.getValue());
							});
		}
	}

	public static void dumpProfile(String name) {
		CallsiteProfile total = new CallsiteProfile();

		synchronized (profiles) {
			for (CallsiteProfile profile : profiles) {
				total.join(profile);
			}
		}

		dumpTo(total.counterL0, name + "-L0");
		dumpTo(total.counterL1, name + "-L1");
		dumpTo(total.counterL2, name + "-L2");
		dumpTo(total.counterL3, name + "-L3");
	}

	public static void profileInvocation(String noLevel, Object receiver) {
		Thread current = Thread.currentThread();

		if (current.__samplingCounter-- <= 0) {
			current.__samplingCounter = 1000;
			CallsiteProfile profile = current.__samplingProfile;

			if (profile == null) {
				profile = new CallsiteProfile();

				synchronized (profiles) {
					profiles.add(profile);
				}

				current.__samplingProfile = profile;
			}

			String klass = receiver.getClass().getName();
			StackTraceElement[] traces = (new Exception()).getStackTrace();
			int length = traces.length;

			profile.counterL0.computeIfAbsent(noLevel,
					key -> new ReceiverProfile()).increment(klass);

			StringBuilder context = new StringBuilder(noLevel);
			context.append('^');
			context.append(length >= 2 ? traces[1].toString() : "null");

			profile.counterL1.computeIfAbsent(context.toString(),
					key -> new ReceiverProfile()).increment(klass);

			context.append('^');
			context.append(length >= 3 ? traces[2].toString() : "null");

			profile.counterL2.computeIfAbsent(context.toString(),
					key -> new ReceiverProfile()).increment(klass);

			context.append('^');
			context.append(length >= 4 ? traces[3].toString() : "null");

			profile.counterL3.computeIfAbsent(context.toString(),
					key -> new ReceiverProfile()).increment(klass);
		}
	}

}

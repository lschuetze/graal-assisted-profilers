package ch.usi.dag.profiler.threadlocal;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.dump.TTYDumper;

public class ProfileSet {

	static final ConcurrentLinkedQueue<MetaProfile<? extends SiteProfile<?>>> profiles = new ConcurrentLinkedQueue<>();

	@SuppressWarnings("unchecked")
	public static <T extends MetaProfile<? extends SiteProfile<?>>> T getProfile(Supplier<T> supplier) {
		Thread current = Thread.currentThread();
		T profile = (T) current.__profile;
		if (profile == null) {
			profile = supplier.get();
			profiles.add(profile);
			current.__profile = profile;
		}
		return profile;
	}

	@SuppressWarnings("unchecked")
	public static <T extends MetaProfile<? extends SiteProfile<?>>> void forEach(Consumer<T> consumer) {
		for (MetaProfile<? extends SiteProfile<?>> profile : profiles) {
			consumer.accept((T) profile);
		}
	}

	public static void reset() {
		profiles.clear();
	}

	static void dump(Dumper dumper) {
		MetaProfile<?> collector = new MetaProfile<>();
		ProfileSet.forEach(collector::merge);
		collector.forEach(entry -> {
			dumper.println(entry.getKey() + " " + entry.getValue());
		});
	}

	public static void dumpToTTY() {
		try (TTYDumper dumper = new TTYDumper()) {
			dump(dumper);
		}
	}

	public static void dumpToArchieve(String filename) {
		try (Dumper dumper = new ArchiveDumper(filename)) {
			dump(dumper);
		}
	}
	
	static final boolean DUMP_AT_SHUTDOWN = Boolean.getBoolean("dumptotty");

	public static void dumpToTTYAtShutdownIfEnable() {
		if (DUMP_AT_SHUTDOWN) {
			Runtime.getRuntime().addShutdownHook(new Thread(ProfileSet::dumpToTTY));
		}
	}

}

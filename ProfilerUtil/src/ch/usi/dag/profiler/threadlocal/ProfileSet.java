package ch.usi.dag.profiler.threadlocal;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.dump.TTYDumper;

public class ProfileSet<T extends SiteProfile<T>> {

	private static final boolean DUMP_AT_SHUTDOWN = Boolean.getBoolean("dumptotty");

	private final Supplier<T> siteProfileSupplier;
	private final ConcurrentLinkedQueue<MetaProfile<T>> profiles = new ConcurrentLinkedQueue<>();

	private ProfileSet(Supplier<T> supplier) {
		siteProfileSupplier = supplier;
		if (DUMP_AT_SHUTDOWN) {
			Runtime.getRuntime().addShutdownHook(new Thread(this::dumpToTTY));
		}
	}

	@SuppressWarnings("unchecked")
	public T getSiteProfile(String key) {
		Thread current = Thread.currentThread();
		MetaProfile<T> metaProfile = (MetaProfile<T>) current.__profile;
		if (metaProfile == null) {
			metaProfile = new MetaProfile<>();
			profiles.add(metaProfile);
			current.__profile = metaProfile;
		}
		return metaProfile.getProfile(key, siteProfileSupplier);
	}

	public void forEach(Consumer<MetaProfile<T>> consumer) {
		for (MetaProfile<T> profile : profiles) {
			consumer.accept(profile);
		}
	}

	private void dump(Dumper dumper) {
		MetaProfile<T> collector = new MetaProfile<>();
		forEach(collector::merge);
		collector.forEach(entry -> {
			dumper.println(entry.getKey() + " " + entry.getValue());
		});
	}

	public void dumpToTTY() {
		try (TTYDumper dumper = new TTYDumper()) {
			dump(dumper);
		}
	}

	public void dumpToArchieve(String filename) {
		try (Dumper dumper = new ArchiveDumper(filename)) {
			dump(dumper);
		}
	}

	private static volatile ProfileSet<?> profiler;

	@SuppressWarnings("unchecked")
	public static <T extends SiteProfile<T>> ProfileSet<T> getInstance(Supplier<T> supplier) {
		ProfileSet<T> result = (ProfileSet<T>) profiler;
		if (result == null) {
			synchronized (ProfileSet.class) {
				result = (ProfileSet<T>) profiler;
				if (result == null) {
					profiler = result = new ProfileSet<>(supplier);
				}
			}
		}
		return result;
	}

	public static void reset() {
		if (profiler != null) {
			profiler.profiles.clear();
		}
	}

	public static void dump(String filename) {
		if (profiler != null) {
			profiler.dumpToArchieve(filename);
		}
	}

}

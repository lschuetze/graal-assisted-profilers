package ch.usi.dag.merlin;

import java.util.concurrent.atomic.AtomicLong;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class MerlinProfiler {

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(MerlinProfiler::dump));
	}

	private static final int INITIAL_SHADOW_HEAP_CAPACITY = 2 << 16;

	static final Dumper dumper = new ArchiveDumper("merlin");

	static void dump() {
		shadowHeap.invalidateAll();
		dumper.close();
	}

	static final LoadingCache<Object, ShadowObject> shadowHeap = CacheBuilder
			.newBuilder()
			.weakKeys()
			.removalListener(new RemovalListener<Object, ShadowObject>() {

				@Override
				public void onRemoval(
						RemovalNotification<Object, ShadowObject> entry) {
					ShadowObject dead = entry.getValue();
					dead.invalidate();

					if (dead.create > 0) {
						synchronized (dumper) {
							dumper.println(dead.toString());
						}
					}
				}

			}).initialCapacity(INITIAL_SHADOW_HEAP_CAPACITY)
			.build(new CacheLoader<Object, ShadowObject>() {

				/**
				 * Handles the case where, for some reason, <code>object</code>
				 * 's allocation was not observed.
				 */
				@Override
				public ShadowObject load(Object object) {
					return new ShadowObject(0);
				}

			});

	static final AtomicLong clock = new AtomicLong(1);

	public static void updateObjectUse(Object obj) {
		if (obj != null) {
			shadowHeap.getUnchecked(obj).update(clock.get());
		}
	}

	public static void link(Object instance, Object value, String fieldName) {
		if (instance != null) {
			ShadowObject shadowInstance = shadowHeap.getUnchecked(instance);

			if (value != null) {
				shadowInstance.link(fieldName, shadowHeap.getUnchecked(value));
			} else {
				shadowInstance.link(fieldName, null);
			}
		}
	}

	public static void onObjectAlloc(Object newObj) {
		shadowHeap.asMap().put(newObj,
				new ShadowObject(clock.getAndIncrement()));
	}

}

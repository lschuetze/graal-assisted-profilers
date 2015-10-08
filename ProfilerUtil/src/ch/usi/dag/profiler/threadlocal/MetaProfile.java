package ch.usi.dag.profiler.threadlocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MetaProfile<T extends SiteProfile<T>> {

	private HashMap<String, T> profiles = new HashMap<>();

	public T getProfile(String key, Supplier<T> supplier) {
		return profiles.computeIfAbsent(key, in -> supplier.get());
	}

	public void merge(MetaProfile<T> other) {
		for (Entry<String, T> entry : other.profiles.entrySet()) {
			String otherKey = entry.getKey();
			T otherValue = entry.getValue();
			T value = profiles.get(otherKey);
			if (value == null) {
				profiles.put(otherKey, otherValue.copy());
			} else {
				value.merge(otherValue);
			}
		}
	}

	public void forEach(Consumer<? super Entry<String, T>> action) {
		profiles.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(action);
	}

}

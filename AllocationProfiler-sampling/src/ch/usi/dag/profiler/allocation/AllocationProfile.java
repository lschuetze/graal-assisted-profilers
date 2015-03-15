package ch.usi.dag.profiler.allocation;

import java.util.HashMap;
import java.util.HashSet;

public class AllocationProfile {

	public final HashMap<String, Integer> itrprAlloc = new HashMap<>();
	public final HashMap<String, Integer> tlabAlloc = new HashMap<>();
	public final HashMap<String, Integer> heapAlloc = new HashMap<>();
	public final HashMap<String, Integer> virtAlloc = new HashMap<>();
	public final HashMap<String, Integer> errorAlloc = new HashMap<>();

	public void profileAlloc(String key, int type) {
		HashMap<String, Integer> which = null;

		switch (type) {
		case 0:
			which = tlabAlloc;
			break;
		case 1:
			which = heapAlloc;
			break;
		case 2:
			which = itrprAlloc;
			break;
		case 3:
			which = virtAlloc;
			break;
		default:
			which = errorAlloc;
		}

		Integer count = which.get(key);
		which.put(key, count == null ? 1 : count + 1);
	}

	public void clearProfile() {
		itrprAlloc.clear();
		tlabAlloc.clear();
		heapAlloc.clear();
		virtAlloc.clear();
		errorAlloc.clear();
	}

	public void collectKeys(HashSet<String> keys) {
		keys.addAll(itrprAlloc.keySet());
		keys.addAll(tlabAlloc.keySet());
		keys.addAll(heapAlloc.keySet());
		keys.addAll(virtAlloc.keySet());
		keys.addAll(errorAlloc.keySet());
	}
	
	public void collectCounters(String key, AllocationCounter counter) {
		Integer value = itrprAlloc.get(key);
		
		if (value != null) {
			counter.itrprCounter += value;
		}
		
		value = tlabAlloc.get(key);
		
		if (value != null) {
			counter.tlabCounter += value;
		}
		
		value = heapAlloc.get(key);
		
		if (value != null) {
			counter.heapCounter += value;
		}
		
		value = virtAlloc.get(key);
		
		if (value != null) {
			counter.virtCounter += value;
		}
		
		value = errorAlloc.get(key);
		
		if (value != null) {
			counter.errorCounter += value;
		}
	}

}

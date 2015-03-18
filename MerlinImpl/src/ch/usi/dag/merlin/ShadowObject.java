package ch.usi.dag.merlin;

import java.util.HashMap;

public class ShadowObject {

	final long create;
	volatile long live;
	HashMap<String, ShadowObject> references;

	public ShadowObject(long create) {
		this.create = create;
		this.live = create;
		this.references = new HashMap<>();
	}

	public void update(long live) {
		if (this.live < live) {
			this.live = live;
		}
	}

	public String toString() {
		return String.format("%d %d", create, live);
	}

	public void link(String fieldName, ShadowObject value) {
		ShadowObject exist;

		if (value != null) {
			exist = references.put(fieldName, value);
		} else {
			exist = references.remove(fieldName);
		}

		if (exist != null) {
			if (exist.live < live) {
				exist.update(live);
			}
		}
	}

	public void invalidate() {
		for (ShadowObject reference : references.values()) {
			if (reference.live < live) {
				reference.update(live);
			}
		}
	}

}

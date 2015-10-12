package ch.usi.dag.profiler.common;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.marker.BodyMarker;

class Instrumentation {

	@ThreadLocal
	static int __samplingCounter = 0;

	@ThreadLocal
	static MetaProfile<?> __profile;

	@Before(marker = BodyMarker.class, scope = "dummy.dummy")
	static void unused() {
		__samplingCounter = 0;
		__profile = null;
	}

}

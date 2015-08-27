package ch.usi.dag.profiler.lock;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.staticcontext.BytecodeStaticContext;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class Instrumentation {

	@AfterReturning(marker = BytecodeMarker.class, args = "monitorenter")
	static void profileAllocation(DynamicContext dc, BytecodeStaticContext bsc) {
		DelimitationAPI.instrumentationBegin(-1);
		Profiler.profileLock(bsc.bci(), GraalQueryAPI.getLockType());
		DelimitationAPI.instrumentationEnd();
	}

	@Before(marker = BodyMarker.class, guard = MethodIsSynchronizedGuard.class)
	public static void onSynchronizedMethodEntry(BytecodeStaticContext bsc) {
		DelimitationAPI.instrumentationBegin(-1);
		Profiler.profileLock(bsc.bci(), GraalQueryAPI.getLockType());
		DelimitationAPI.instrumentationEnd();
	}

}

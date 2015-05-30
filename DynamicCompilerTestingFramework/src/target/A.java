package target;

import pea.Constants;
import pea.PEATestCase;

import com.oracle.graal.debug.external.CompilerDecision;
import com.oracle.graal.debug.external.DontInline;

public class A {

	private long v;

	public long getV() {
		return v;
	}

	public void complicatedMethod() {
		v = "This is a complicated method".chars().distinct().count();
	}

	@DontInline
	public void bar() {
	}

	// The following code with the pseudo instrumentation inlined is for
	// showing what may look like when using a profiler. An alternative way
	// would be to include the following code in a test case.
	@DontInline
	public static void foo(boolean invokeBar) {
		A a = new A();

		CompilerDecision.instrumentationBegin(Constants.PRED);

		if (CompilerDecision.isMethodCompiled()
				&& CompilerDecision.isHeapAlloc()) {
			PEATestCase.counter++;
		}

		CompilerDecision.instrumentationEnd();

		if (invokeBar) {
			// This will not be inlined and let the receiver escape
			a.bar();
		}
	}

	// Distance between the allocation node and the delimitation API
	static final int PRED = -2;

}

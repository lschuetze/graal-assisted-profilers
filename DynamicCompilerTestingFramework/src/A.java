import com.oracle.graal.debug.external.CompilerDecision;
import com.oracle.graal.debug.external.DontInline;

public class A {

	@DontInline
	static void foo(boolean invokeBar) {
		A a = new A();

		CompilerDecision.instrumentationBegin(-2);

		if (CompilerDecision.isMethodCompiled()
				&& !CompilerDecision.isAllocationVirtual()) {
			PEATestCase.counter++;
		}

		CompilerDecision.instrumentationEnd();

		if (invokeBar) {
			a.bar();
		}
	}

	@DontInline
	void bar() {
	}

}

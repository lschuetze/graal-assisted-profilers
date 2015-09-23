package pea.target;

import jdk.internal.jvmci.hotspot.DontInline;

public class B {

	public final A a;

	public B(A a) {
		this.a = a;
	}

	public void empty() {
	}

	@DontInline
	public void notInlinedMethod() {
	}

}

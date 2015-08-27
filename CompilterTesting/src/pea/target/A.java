package pea.target;

import jdk.internal.jvmci.hotspot.DontInline;

public class A {

	public void empty() {
	}

	@DontInline
	public void notInlinedMethod() {
	}

}

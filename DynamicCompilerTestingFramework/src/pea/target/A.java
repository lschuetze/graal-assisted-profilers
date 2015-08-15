package pea.target;

import jdk.internal.jvmci.debug.DontInline;

public class A {

	public void empty() {
	}

	@DontInline
	public void notInlinedMethod() {
	}

}

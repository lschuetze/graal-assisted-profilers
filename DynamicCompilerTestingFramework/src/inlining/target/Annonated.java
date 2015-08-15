package inlining.target;

import jdk.internal.jvmci.debug.DontInline;

public class Annonated extends Super {

	@DontInline
	@Override
	public int caculate(int input) {
		return input;
	}

}

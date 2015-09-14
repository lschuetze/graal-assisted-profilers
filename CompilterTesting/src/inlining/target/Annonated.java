package inlining.target;

import jdk.internal.jvmci.hotspot.DontInline;

public class Annonated extends Super {

	@DontInline
	@Override
	public int calculate(int input) {
		return input;
	}

}

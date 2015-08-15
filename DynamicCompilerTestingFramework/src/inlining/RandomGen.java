package inlining;

import java.util.Random;

import jdk.internal.jvmci.debug.DontInline;

public class RandomGen {

	public static Random r = new Random();

	@DontInline
	public static int nextInt() {
		return r.nextInt();
	}
	
	@DontInline
	public static double nextDouble() {
		return r.nextDouble();
	}

}

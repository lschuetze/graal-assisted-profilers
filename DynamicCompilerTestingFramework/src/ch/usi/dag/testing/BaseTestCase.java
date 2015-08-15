package ch.usi.dag.testing;

import jdk.internal.jvmci.debug.DontInline;

import org.junit.Test;

public abstract class BaseTestCase extends JITTestCase {

	public static final int ITERATIONS = 10000;

	protected boolean isCompiled = false;
	protected int counter = 0;

	@Override
	protected void warmup() {
		target();
	}

	@Override
	protected boolean isWarmedUp() {
		return isCompiled;
	}

	@DontInline
	public abstract void target();

	@Test
	public void test() {
		counter = 0;
		for (int i = 0; i < ITERATIONS; i++)
			target();
		verify();
	}

	public abstract void verify();

}

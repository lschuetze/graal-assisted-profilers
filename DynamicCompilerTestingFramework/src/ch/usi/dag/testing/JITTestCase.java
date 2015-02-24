package ch.usi.dag.testing;

import java.util.Random;

import junit.framework.TestCase;

public abstract class JITTestCase extends TestCase {

	private final Random random = new Random();

    @Override
    protected final void setUp() {
		while (!isWarmedUp()) {
			warmup();
		}
	}

	protected final boolean likely(double probability) {
		return random.nextDouble() < probability;
	}

	protected abstract void warmup();

	protected abstract boolean isWarmedUp();

}

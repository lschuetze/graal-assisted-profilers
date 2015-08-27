package query;

import static org.junit.Assert.assertEquals;
import jdk.internal.jvmci.hotspot.DontInline;

import org.junit.Test;

import ch.usi.dag.testing.JITTestCase;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class IsMethodInlined extends JITTestCase {

	public static final int HERE = 0;
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
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (GraalQueryAPI.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		empty();
	}
	
	public void empty() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (GraalQueryAPI.isMethodInlined())
			counter++;
		DelimitationAPI.instrumentationEnd();
	}

	@Test
	public void test() {
		counter = 0;

		for (int i = 0; i < ITERATIONS; i++)
			target();

		assertEquals(counter, ITERATIONS);
	}

}

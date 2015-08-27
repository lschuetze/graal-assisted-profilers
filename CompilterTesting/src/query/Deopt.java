package query;

import static org.junit.Assert.assertEquals;
import jdk.internal.jvmci.hotspot.DontInline;

import org.junit.Test;

import ch.usi.dag.testing.JITTestCase;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class Deopt extends JITTestCase {

	public static final int HERE = 0;
	public static final int DEOPT = 1;
	public static final int ITERATIONS = 10000;
	public static final int ERROR = -1;

	public static final int BoundsCheckException = 2;
	public static final int InvalidateReprofile = 2;

	protected boolean isCompiled = false;
	protected int deoptReason = ERROR;
	protected int deoptAction = ERROR;
	protected int deoptBCI = ERROR;

	@Override
	protected void warmup() {
		target(1);
	}

	@Override
	protected boolean isWarmedUp() {
		return isCompiled;
	}

	private int someArray[] = new int[4];

	@DontInline
	public void target(int i) {
		DelimitationAPI.instrumentationBegin(HERE);
		if (GraalQueryAPI.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		DelimitationAPI.instrumentationBegin(HERE, DEOPT);
		if (GraalQueryAPI.isMethodCompiled()) {
			deoptReason = GraalQueryAPI.getDeoptReason();
			deoptAction = GraalQueryAPI.getDeoptAction();
			deoptBCI = GraalQueryAPI.getDeoptBCI();
		}
		DelimitationAPI.instrumentationEnd();

		if (i >= 0) {
			someArray[i] = 1;
		}
	}

	@Test
	public void test() {
		deoptReason = ERROR;
		deoptAction = ERROR;
		deoptBCI = ERROR;

		try {
			target(4);
		} catch (IndexOutOfBoundsException e) {
		}

		assertEquals(deoptReason, BoundsCheckException);
		assertEquals(deoptAction, InvalidateReprofile);
		// update the following if any change applies to target(I)
		assertEquals(deoptBCI, 0);
	}

}

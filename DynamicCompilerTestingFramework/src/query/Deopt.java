package query;

import static org.junit.Assert.assertEquals;
import jdk.internal.jvmci.debug.CompilerDecision;
import jdk.internal.jvmci.debug.DelimitationAPI;
import jdk.internal.jvmci.debug.DontInline;

import org.junit.Test;

import ch.usi.dag.testing.JITTestCase;

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
		if (CompilerDecision.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		DelimitationAPI.instrumentationBegin(HERE, DEOPT);
		if (CompilerDecision.isMethodCompiled()) {
			deoptReason = CompilerDecision.getDeoptReason();
			deoptAction = CompilerDecision.getDeoptAction();
			deoptBCI = CompilerDecision.getDeoptBCI();
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
		assertEquals(deoptReason, InvalidateReprofile);
		// update the following if any change applies to target(I)
		assertEquals(deoptBCI, 53);
	}

}

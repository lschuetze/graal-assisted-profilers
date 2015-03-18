package ch.usi.dag.merlin;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.ArgumentProcessor;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.AfterInitBodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.merlin.Guard.PrimitiveFields;
import ch.usi.dag.merlin.Guard.ReferenceFields;

import com.oracle.graal.debug.external.CompilerDecision;

public class Instrumentation {

	// Allocation
	@AfterReturning(marker = BytecodeMarker.class, args = "new")
	public static void onAllocation(DynamicContext dc) {
		CompilerDecision.instrumentationBegin(-1);
		if (!CompilerDecision.isMethodCompiled()
				|| !CompilerDecision.isAllocationVirtual()) {
			MerlinProfiler.onObjectAlloc(dc.getStackValue(0, Object.class));
		}
		CompilerDecision.instrumentationEnd();
	}

	// Object update
	@Before(marker = BytecodeMarker.class, args = "athrow,arraylength,checkcast,getfield,ifnull,ifnonnull,instanceof")
	public static void onObjectUse1(DynamicContext dc) {
		CompilerDecision.instrumentationBegin(0);
		MerlinProfiler.updateObjectUse(dc.getStackValue(0, Object.class));
		CompilerDecision.instrumentationEnd();
	}

	@AfterReturning(marker = BytecodeMarker.class, args = "aaload")
	public static void afterReferenceArrayLoad(DynamicContext dc) {
		CompilerDecision.instrumentationBegin(0);
		MerlinProfiler.updateObjectUse(dc.getStackValue(0, Object.class));
		CompilerDecision.instrumentationEnd();
	}

	@Before(marker = BytecodeMarker.class, args = "if_acmpne,if_acmpeq")
	public static void beforeUsingTwoReferences(DynamicContext dc) {
		CompilerDecision.instrumentationBegin(0);
		MerlinProfiler.updateObjectUse(dc.getStackValue(0, Object.class));
		MerlinProfiler.updateObjectUse(dc.getStackValue(1, Object.class));
		CompilerDecision.instrumentationEnd();
	}

	@Before(marker = BytecodeMarker.class, args = "putfield", guard = PrimitiveFields.class)
	public static void beforePutPrimField(DynamicContext dc) {
		CompilerDecision.instrumentationBegin(0);
		MerlinProfiler.updateObjectUse(dc.getStackValue(1, Object.class));
		CompilerDecision.instrumentationEnd();
	}

	@Before(marker = BytecodeMarker.class, args = "putfield", guard = ReferenceFields.class)
	public static void beforePutRefField(DynamicContext dc, MerlinContext mc) {
		CompilerDecision.instrumentationBegin(0);
		Object instance = dc.getStackValue(1, Object.class);
		Object value = dc.getStackValue(0, Object.class);

		MerlinProfiler.link(instance, value, mc.getFieldName());
		MerlinProfiler.updateObjectUse(instance);
		CompilerDecision.instrumentationEnd();
	}

	@AfterReturning(marker = BytecodeMarker.class, args = "invokevirtual,invokespecial,invokeinterface")
	public static void beforeInvoke(DynamicContext dc,
			ArgumentProcessorContext args) {
		CompilerDecision.instrumentationBegin(0);
		MerlinProfiler.updateObjectUse(args.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS));
		CompilerDecision.instrumentationEnd();
	}

	// Update argument
	@Before(marker = AfterInitBodyMarker.class)
	public static void onMethodEntry(ArgumentProcessorContext args) {
		CompilerDecision.instrumentationBegin(0);
		args.apply(ReferenceArgumentProcessor.class, ArgumentProcessorMode.METHOD_ARGS);
		CompilerDecision.instrumentationEnd();
	}

	@ArgumentProcessor
	static class ReferenceArgumentProcessor {
		public static void onReferenceArgument(final Object argument,
				final ArgumentContext ac) {
			MerlinProfiler.updateObjectUse(argument);
		}
	}

}

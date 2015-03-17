package ch.usi.dag.callback;

import org.dacapo.harness.Callback;
import org.dacapo.harness.CommandLineArgs;

import ch.usi.dag.profiler.inlining.Profiler;

public class IPCallback extends Callback {

	private int iteration = 1;

	public IPCallback(CommandLineArgs args) {
		super(args);
	}

	@Override
	public void start(String benchmark) {
		Profiler.clearProfile();
		super.start(benchmark);
	}

	@Override
	public void complete(String benchmark, boolean valid, boolean warmup) {
		super.complete(benchmark, valid, warmup);
		Profiler.dumpProfile("IP-" + benchmark + "-" + iteration++);
	}

}

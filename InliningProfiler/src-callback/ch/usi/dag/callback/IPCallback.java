package ch.usi.dag.callback;

import org.dacapo.harness.Callback;
import org.dacapo.harness.CommandLineArgs;

import ch.usi.dag.profiler.inlining.Profiler;

public class IPCallback extends Callback {

	private String name = null;
	private int iteration = 0;

	public IPCallback(CommandLineArgs args) {
		super(args);
	}

	@Override
	public void start(String benchmark) {
		super.start(benchmark);
		Profiler.clearProfile();
		name = benchmark;
		iteration++;
	}

	@Override
	public void stop() {
		super.stop();
		Profiler.dumpProfile("IP-" + String.valueOf(name) + "-" + iteration);
	}

}

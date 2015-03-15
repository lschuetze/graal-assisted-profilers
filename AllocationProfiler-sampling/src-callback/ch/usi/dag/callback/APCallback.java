package ch.usi.dag.callback;
import org.dacapo.harness.Callback;
import org.dacapo.harness.CommandLineArgs;

import ch.usi.dag.profiler.allocation.Profiler;

public class APCallback extends Callback {

	private String name = null;
	private int iteration = 0;

	public APCallback(CommandLineArgs args) {
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
		Profiler.dumpProfile("AP-" + String.valueOf(name) + "-" + iteration);
	}

}

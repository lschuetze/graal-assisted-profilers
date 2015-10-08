package ch.usi.dag.callback;

import org.dacapo.harness.Callback;
import org.dacapo.harness.CommandLineArgs;

import ch.usi.dag.profiler.threadlocal.ProfileSet;

public class ProfilerCallback extends Callback {

	private int iteration = 1;

	public ProfilerCallback(CommandLineArgs args) {
		super(args);
	}

	@Override
	public void start(String benchmark) {
		ProfileSet.reset();
		super.start(benchmark);
	}

	@Override
	public void complete(String benchmark, boolean valid, boolean warmup) {
		super.complete(benchmark, valid, warmup);
		ProfileSet.dump(benchmark + "-" + iteration++);
	}

}

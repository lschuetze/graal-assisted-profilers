package ch.usi.dag.callback;

import org.dacapo.harness.Callback;
import org.dacapo.harness.CommandLineArgs;

import ch.usi.dag.profiler.common.ThreadLocalProfile;

public class ProfilerCallback extends Callback {

	private int iteration = 1;

	public ProfilerCallback(CommandLineArgs args) {
		super(args);
	}

	@Override
	public void start(String benchmark) {
		ThreadLocalProfile.reset();
		super.start(benchmark);
	}

	@Override
	public void complete(String benchmark, boolean valid, boolean warmup) {
		super.complete(benchmark, valid, warmup);
		ThreadLocalProfile.dump(benchmark + "-" + iteration++);
	}

}

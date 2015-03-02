package ch.usi.dag.profiler.dump;

public class TTYDumper implements Dumper {

	@Override
	public void println(Object message) {
		System.err.println(message);
	}

	@Override
	public void close() {
	}

}

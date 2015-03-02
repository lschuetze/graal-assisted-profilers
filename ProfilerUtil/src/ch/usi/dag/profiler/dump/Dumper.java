package ch.usi.dag.profiler.dump;

import java.io.Closeable;

public interface Dumper extends Closeable {

	public void println(Object message);

	public void close();

}

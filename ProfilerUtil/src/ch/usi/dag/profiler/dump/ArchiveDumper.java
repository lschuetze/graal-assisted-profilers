package ch.usi.dag.profiler.dump;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.zip.GZIPOutputStream;

public class ArchiveDumper implements Dumper {

	final PrintStream out;

	public ArchiveDumper(String name) {
		try {
			out = new PrintStream(new GZIPOutputStream(
					new BufferedOutputStream(new FileOutputStream(name
							+ ".tsv.gz"))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void println(Object message) {
		out.println(message);
	}

	@Override
	public void close() {
		out.close();
	}

}

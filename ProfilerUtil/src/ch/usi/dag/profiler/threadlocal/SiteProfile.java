package ch.usi.dag.profiler.threadlocal;

public interface SiteProfile<T> {

	T copy();

	void merge(T other);

}

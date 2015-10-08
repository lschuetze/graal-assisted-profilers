package ch.usi.dag.profiler.threadlocal;

public interface SiteProfile<T> {

	T copy();

	T merge(T other);

}

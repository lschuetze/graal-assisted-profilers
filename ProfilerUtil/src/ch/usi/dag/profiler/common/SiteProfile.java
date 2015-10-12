package ch.usi.dag.profiler.common;

public interface SiteProfile<T> {

	T copy();

	T merge(T other);

}

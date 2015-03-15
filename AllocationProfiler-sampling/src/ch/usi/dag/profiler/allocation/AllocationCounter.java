package ch.usi.dag.profiler.allocation;

public class AllocationCounter {

	public int itrprCounter = 0;
	public int tlabCounter = 0;
	public int heapCounter = 0;
	public int virtCounter = 0;
	public int errorCounter = 0;

	@Override
	public String toString() {
		return itrprCounter + " " + tlabCounter + " " + heapCounter + " "
				+ virtCounter + " " + errorCounter;
	}
}

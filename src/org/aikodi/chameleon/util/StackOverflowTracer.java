package org.aikodi.chameleon.util;


public class StackOverflowTracer {

	private int _threshold;
	private int _count;

	public StackOverflowTracer(int threshold) {
		_threshold = threshold;
	}
	
	protected int count() {
	  return _count;
	}
	
	public synchronized void push() {
		_count++;
		Util.debug(_count >= _threshold);
	}
	
	public synchronized void pop() {
		_count--;
		if(_count < 0) {
			throw new IllegalStateException("The tracer was popped more often than it was pushed.");
		}
	}
}

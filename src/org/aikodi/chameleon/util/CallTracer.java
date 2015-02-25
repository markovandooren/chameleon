package org.aikodi.chameleon.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class CallTracer {

	public CallTracer(String name, boolean enabled) {
		_trace.add(0);
		_number = number();
		_enabled = enabled;
		_name = name;
	}
	
	public void addBreakpoint(String string) {
		_breakPoints.add(string);
	}
	
	private Set<String> _breakPoints = new HashSet<>();
	
	private String _name;
	private boolean _enabled;
	
	public void enable() {
		_enabled = true;
	}
	
	public void disable() {
		_enabled = false;
	}
	
	public void push() {
		if(_enabled) {
			_trace.add(last+1);
			last = -1;
			_number = number();
			if(_breakPoints.contains(_number)) {
				Util.debug(true);
				_breakPoints.remove(_number);
			}
		}
	}
	
	public final static boolean LOG = false;
	
	private String _number;
	
	private int last = -1;
	
	public void log(String string) {
		if(_enabled) {
			Util.debug(string.equals("checking same base type with compatible parameters: Comparable<T extends java.lang.Object super T> and T"));
			System.out.println(_name+": "+_number+" : "+string);
//			_trace.set(_trace.size()-1, _trace.getLast()+1);
		}
	}
	
	private String number() {
		StringBuilder builder = new StringBuilder();
		Iterator<Integer> iter = _trace.iterator();
//		int size = _trace.size();
//		int index = 0;
		while(iter.hasNext()) {
			builder.append(iter.next());
			if(iter.hasNext()) {
//				if(index == size-2) {
//					builder.append(" @ ");
//				} else {
					builder.append('.');
//				}
			}
//			index++;
		}
		return builder.toString();
	}
	
	public void pop() {
		if(_enabled) {
			last = _trace.removeLast();
			_number = number();
		}
	}
	
	@Override
	public String toString() {
		return _trace.toString();
	}
	
	private LinkedList<Integer> _trace = new LinkedList<>();
}

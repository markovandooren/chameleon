package org.aikodi.chameleon.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A debugging class that stores the current stacktrace.
 * 
 * @author Marko van Dooren
 */
public class CreationStackTrace {

	private List<StackTraceElement> _stackTrace;

	public List<StackTraceElement> trace() {
		return new ArrayList<StackTraceElement>(_stackTrace);
	}
	
	public CreationStackTrace() {
		try {
			throw new Exception();
		} catch (Exception e) {
			_stackTrace = Arrays.asList(e.getStackTrace());
		}
	}
	
  public CreationStackTrace(int size) {
    try {
      throw new Exception();
    } catch (Exception e) {
      _stackTrace = Arrays.asList(e.getStackTrace()).subList(0, size);
    }
  }
}

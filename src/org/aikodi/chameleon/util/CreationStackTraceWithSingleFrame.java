package org.aikodi.chameleon.util;

public class CreationStackTraceWithSingleFrame {

	private StackTraceElement _frame;
	
	public StackTraceElement frame() {
		return _frame;
	}
	
	private int _index;
	
	public int index() {
		return _index;
	}
	
	public CreationStackTraceWithSingleFrame(int index) {
		try {
			_index = index;
			throw new Exception();
		} catch (Exception e) {
			_frame = e.getStackTrace()[index];
		}
	}

}

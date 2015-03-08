package org.aikodi.chameleon.input;

public class Position2D {

	
	/**
	 * Initialize a new 2D position with the given line number and offset within the line.
	 * Both numbers can range from 0 to ...
	 * 
	 * @param lineNumber
	 * @param offset
	 */
 /*@
   @ public behavior
   @
   @ post lineNumber() == lineNumber;
   @ post offset() == offset;
   @*/
	public Position2D(int lineNumber, int offset) {
		_line = lineNumber;
		_offset = offset;
	}
	
	/**
	 * Return the line number of this position.
	 */
	public int lineNumber() {
		return _line;
	}

	/**
	 * Return the offset of this position.
	 */
	public int offset() {
		return _offset;
	}

	private int _line;
	
	private int _offset;
}

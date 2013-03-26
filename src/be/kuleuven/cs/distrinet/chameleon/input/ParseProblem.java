package be.kuleuven.cs.distrinet.chameleon.input;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;

public class ParseProblem extends BasicProblem {

	public ParseProblem(Element element, String message, int offset, int length) {
		super(element, message);
		setOffset(offset);
		setLength(length);
	}
	
	private int _offset;
	
	private int _length;
	
	public int length() {
		return _length;
	}
	
	public void setOffset(int offset) {
		_offset = offset;
	}
	
	public void setLength(int length) {
		_length = length;
	}
	
	public int offset() {
		return _offset;
	}
	
	public boolean equals(Object other) {
		boolean result = false;
		if(other instanceof ParseProblem) {
			ParseProblem pb = (ParseProblem) other;
			String message = pb.message();
			result = pb.element() == element() && message == null ? message() == null : message.equals(message());
		}
		return result;
	}

}

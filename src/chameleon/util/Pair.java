package chameleon.util;

public class Pair<T1,T2> {
	
	public Pair(T1 first, T2 second) {
		setFirst(first);
		setSecond(second);
	}
	
	public T1 first() {
		return _first;
	}
	
	public void setFirst(T1 first) {
		_first = first;
	}

	private T1 _first;
	
	public T2 second() {
		return _second;
	}
	
	public void setSecond(T2 second) {
		_second = second;
	}

	private T2 _second;
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(")
		       .append(first().toString())
		       .append(",")
		       .append(second().toString())
		       .append(")");
		return builder.toString();
	}

}

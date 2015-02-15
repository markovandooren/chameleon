package be.kuleuven.cs.distrinet.chameleon.util;

import be.kuleuven.cs.distrinet.rejuse.contract.Contracts;

/**
 * A class of triples. A triple does not allow null values.
 * 
 * @author Marko van Dooren
 *
 * @param <T1> The type of the first element of the pair.
 * @param <T2> The type of the second element of the pair.
 * @param <T3> The type of the third element of the pair.
 */
public class Triple<T1,T2,T3> {
	
	/**
	 * Create a new triple with the two given values.
	 * 
	 * @param first
	 * @param second
	 * @param third
	 */
 /*@
   @ public behavior
   @
   @ pre first != null;
   @ pre second != null;
   @ pre third != null;
   @
   @ post first() == first;
   @ post second() == second;
   @ post third() == third;
   @*/
	public Triple(T1 first, T2 second,T3 third) {
		setFirst(first);
		setSecond(second);
		setThird(third);
	}
	
	/**
	 * Return the first object of the pair.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public T1 first() {
		return _first;
	}
	
	private void setFirst(T1 first) {
		Contracts.notNull(first, "The first element of a pair should not be null.");
		_first = first;
	}

	private T1 _first;
	
	/**
	 * Return the second object of the pair.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public T2 second() {
		return _second;
	}
	
	private void setSecond(T2 second) {
		Contracts.notNull(second, "The second element of a pair should not be null.");
		_second = second;
	}
	
	private T2 _second;
	
	/**
	 * Return the third object of the pair.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public T3 third() {
		return _third;
	}
	
	private void setThird(T3 third) {
		Contracts.notNull(third, "The third element of a pair should not be null.");
		_third = third;
	}
	
	private T3 _third;
	
	/**
	 * The string representation is: (first,second).
	 */
	@Override
   public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(")
		       .append(first().toString())
		       .append(",")
		       .append(second().toString())
		       .append(",")
		       .append(third().toString())
		       .append(")");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + _first.hashCode();
		result = prime * result + _second.hashCode();
		result = prime * result + _third.hashCode();
		return result;
	}

	/**
	 * A triple is equal to another triple that has the same
	 * first, second, and third values.
	 */
 /*@
   @ public behavior
   @
   @ post result == other instanceof Triple &&
   @                first().equals(((Triple)other).first()) &&
   @                second().equals(((Triple)other).second()) &&
   @                third().equals(((Triple)other).third());
   @*/
	@Override
	public boolean equals(Object other) {
		return (other instanceof Triple) && 
				   first().equals(((Triple)other).first()) &&
				   second().equals(((Triple)other).second()) &&
				   third().equals(((Triple)other).third());
	}
	
}

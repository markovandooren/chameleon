package chameleon.linkage;




/**
 * An interface acting as an iterator of source objects that need to be
 * supplied to a metamodelfactory
 */
public interface ISourceSupplier {

	/**
	 * @return if the supplier has a next source available
	 */
	boolean hasNext();

	/**
	 * Reset the iterator to the first element
	 */
	void reset();

	/** 
	 * Go to the next element
	 */
	void next();

	/**
	 * @return the source for the current element
	 */
	String getSource();

	/**
	 * @return linkage for the current element
	 */
	ILinkage getLinkage();

}

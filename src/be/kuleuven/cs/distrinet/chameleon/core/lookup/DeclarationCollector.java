package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

/**
 * A class of collectors that collect only a single declaration.
 * 
 * @author Marko van Dooren
 */
public class DeclarationCollector<D extends Declaration> extends Collector<D> {

	/**
	 * Create a new declaration collector that uses the given selector to
	 * select declarations.
	 * 
	 * @param selector
	 */
	public DeclarationCollector(DeclarationSelector<D> selector) {
		super(selector);
	}

	/**
	 * If the given list is empty, nothing is done. If it contains
	 * 1 element, that element is stored as the result of the lookup.
	 * If it contains more than 1 element, a @link{LookupException} is thrown. 
	 */
 /*@
   @ public behavior
   @
   @ pre candidates != null;
   @
   @ post candidates.size() == 1 ==> !willProceed();
   @*/
	@Override
   public void process(List<? extends SelectionResult> candidates) throws LookupException {
		int size = candidates.size();
		if(size == 1) {
			_accumulator = candidates.get(0);
		} else if(size > 1) {
			throw new LookupException("Multiple matches found using selector "+selector().toString(),selector());	
		}
	}
	
	/**
	 * Return the result of the lookup. If no result has been found, a @link{LookupException} is thrown.
	 */
	@Override
   public D result() throws LookupException {
		if(_accumulator == null) {
			throw new LookupException("No result has been found using selector "+selector().toString(),selector());
		} else {
			return (D) _accumulator.finalDeclaration();
		}
	}
	
	/**
	 * A declaration collector will proceed when it has not yet found a declaration.
	 */
	@Override
   public boolean willProceed() throws LookupException {
		return _accumulator == null;
	}
		
	private SelectionResult _accumulator;

	@Override
	void storeCachedResult(SelectionResult cached) {
		_accumulator = cached;
	}
	
}

package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

/**
 * A collector is a strategy object that travels across the
 * model to find declarations that match its selector. The
 * selector determines which declarations are collected,
 * whereas the collector determines when to stop.
 *
 * @author Marko van Dooren
 *
 * @param <D> The type of the declarations to be collected.
 */
public abstract class Collector<D extends Declaration> {

	/**
	 * Create a new collector with the given selector.
	 * 
	 * @param selector The selector that will be used to select declarations.
	 */
 /*@
   @ public behavior
   @
   @ pre selector != null;
   @ post selector() == selector;
   @*/
	public Collector(DeclarationSelector<D> selector) {
		setSelector(selector);
	}
	
	protected Collector() {}
	
	private DeclarationSelector<D> _selector;
	
	/**
	 * Set the selector that will be used to select declarations.
	 * 
	 * This method is public because in rare situations, you may want
	 * to temporarily wrap/decorate the selector during lookup.
	 * 
	 * @param selector The new selector of this collector
	 */
 /*@
   @ public behavior
   @
   @ pre selector != null;
   @ post selector() == selector;
   @*/
	public void setSelector(DeclarationSelector<D> selector) {
		if(selector == null) {
			throw new ChameleonProgrammerException("The selector of a declaration collector cannot be null");
		}
		_selector = selector;
	}

	/**
	 * Continue the search for declarations after the given lookup context has
	 * been visited. If {@link #willProceed()} returns true, the search
	 * continues with the {@link LexicalLookupContext#nextContext()} of the
	 * given lexical lookup context. Otherwise, the search for declarations ends.
	 * 
	 * @param lastVisited The lexical lookup context that was last visited.
	 * 
	 * @throws LookupException The next strategy is null.
	 */
 /*@
   @ public behavior
   @
   @ pre lastVisited != null;
   @*/
	public void proceed(LexicalLookupContext lastVisited) throws LookupException {
		if(willProceed()) {
			LookupContext next = lastVisited.nextContext();
			if(next == null) {
				throw new LookupException("The next strategy in the lookup is null.");
			}
			next.lookUp(this);
		}
	}

	/**
	 * Return the selector that is used to select declarations.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public DeclarationSelector<D> selector() {
		return _selector;
	}

	/**
	 * Determine whether the search will proceed to the next lexical lookup context.
	 * 
	 * @return
	 * @throws LookupException
	 */
  public abstract boolean willProceed() throws LookupException;
  
  /**
   * Process the given local lookup context.
   * @param local
   * @throws LookupException
   */
	public void process(LocalLookupContext<?> local) throws LookupException {
		process(local.declarations(selector()));
	}	

  protected abstract void process(List<? extends Declaration> candidates) throws LookupException;

	abstract void storeCachedResult(Declaration cached);
	
	abstract D result() throws LookupException;

}

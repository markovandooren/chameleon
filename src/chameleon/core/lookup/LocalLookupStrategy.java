package chameleon.core.lookup;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.oo.member.Member;

/**
 * A lookup strategy for search declarations locally in a declaration container.
 * A local lookup strategy never delegates the search to other lookup strategies. If no
 * element is found, it returns null.
 * 
 * @author Marko van Dooren
 */
public class LocalLookupStrategy<E extends DeclarationContainer> extends LookupStrategy {

	/**
	 * Create a new local lookup strategy that searches for declarations in the
	 * given declaration container.
	 */
 /*@
   @ public behavior
   @
   @ pre declarationContainer != null;
   @
   @ post declarationContainer() == declarationContainer;
   @*/
	public LocalLookupStrategy(E declarationContainer) {
		_declarationContainer = declarationContainer;
	}
	/*
	 * The declaration container in which this local lookup strategy will search for declarations 
	 */
 /*@
	 @ private invariant _declarationContainer != null; 
	 @*/
	private E _declarationContainer;

	/**
	 * Return the declaration container referenced by this local lookup strategy.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public E declarationContainer() {
	  return _declarationContainer; 
	}

  /**
   * Return those declarations of this declaration container that are selected
   * by the given declaration selector. The default implementation delegates the work
   * to declarationContainer().declarations(selector).
   * 
   * @param <D> The type of the arguments selected by the given signature selector. This type
   *            should be inferred automatically.
   * @param selector
   * @return
   * @throws LookupException
   */
  protected <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
  	return selector.declarations(declarationContainer());
  }

  /**
   * Perform a local search in the connected declaration container using a declarations(selector) invocation. If
   * the resulting collection contains a single declaration, that declaration is returned. If the resulting
   * collection is empty, null is returned. If the resulting collection contains multiple elements, a
   * LookupException is thrown since the lookup is ambiguous.
   */
	@Override
	public <D extends Declaration> void lookUp(DeclarationCollector<D> collector) throws LookupException {
	  collector.process(declarations(collector.selector()));
	}

}

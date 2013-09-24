package be.kuleuven.cs.distrinet.chameleon.core.lookup;


import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

/**
 * A lexical lookup context is used to lookup elements that are not declared relative to another element 
 * using a '.' character. It is the first (and sometimes only) element in such a chain.
 * 
 * The lexical lookup context uses the given local lookup context to perform
 * a local search. If it does not find anything, it then usually travels upward in the 
 * lexical structure to continue the search. Note that the search does not always continue
 * at the lexical parent, though that is the default behavior. Sometimes, for example, the
 * search must continue at a lexical sibling or an a declaration container in another
 * part of the model, such as a super class.
 * 
 * The selector is used to select the next lookup context if the search for declarations continues.
 * 
 * @author Marko van Dooren
 */

public class LexicalLookupContext extends LookupContext {

	/**
	 * Initialize a new lexical lookup strategy with the given local strategy and the element
	 * where the lexical search is done. The selector is set to a ParentLookupStrategySelector.
	 */
 /*@
   @ public behavior
   @
   @ post localStrategy() == local;
   @ post nextStrategy() instanceof ParentContextSelector;
   @ post ((ParentLookupStrategySelector)nextStrategy).element() == element;
  */
	public LexicalLookupContext(LookupContext local, Element element) {
  	this(local,new ParentLookupContextSelector(element));
  }
	
	public LexicalLookupContext(LookupContext local, LookupContextSelector selector) {
  	if(local == null) {
  		throw new ChameleonProgrammerException("Local context given to lexical context is null");
  	}
  	setLocalContext(local);
  	setSelector(selector);
//  	CREATED++;
	}
	
//	public static int CREATED;
	
	public void setSelector(LookupContextSelector selector) {
		_selector = selector;
	}
  
  public LookupContextSelector selector() {
  	return _selector;
  }
  
  private LookupContextSelector _selector;

  private void setLocalContext(LookupContext local) {
  	_localContext = local;
  }
  
  public LookupContext localContext() {
  	return _localContext;
  }
  
  private LookupContext _localContext;
  
	/**
	 * Return the parent context of this context.
	 * @throws LookupException 
	 */
	public LookupContext nextContext() throws LookupException {
		return selector().strategy();
	}

	public void lookUp(Collector collector) throws LookupException {
		boolean hit = false;
		if(_cache != null) {
			hit = _cache.search(collector);
		}
		if(! hit) {
			localContext().lookUp(collector);
			collector.proceed(this);
			if(_cache != null) {
				_cache.store(collector);
			}
		}
	}
	
	@Override
	public void enableCache() {
		_cache = new Cache();
	}
	
	public void flushCache() {
		if(_cache != null) {
			enableCache();
		}
	}
	
	private Cache _cache;
}

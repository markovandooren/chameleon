package chameleon.core.lookup;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.exception.ChameleonProgrammerException;

public class DeclarationCollector<D extends Declaration> {

	public DeclarationCollector(DeclarationSelector<D> selector) {
		if(selector == null) {
			throw new ChameleonProgrammerException("The selector of a declaration collector cannot be null");
		}
		_selector = selector;
	}
	
	protected DeclarationCollector(DeclarationSelector<D> selector, boolean hack) {
	}
	
	public void process(List<? extends Declaration> candidates) throws LookupException {
		List<Declaration> tmp = (List<Declaration>) candidates;
    if(_accumulator != null) {
    	tmp.addAll(_accumulator);
    }
		List<D> t = selector().selection(tmp);
		if(! t.isEmpty()) {
			_accumulator = t;
		}
	}
	
	public D result() throws LookupException {
		if(_accumulator == null) {
			throw new LookupException("No result has been found");
		} else {
			int size = _accumulator.size();
			if(size == 1) {
				return _accumulator.get(0);
			} else {
				throw new LookupException("Multiple matches found in using selector "+selector().toString(),selector());
			}
		}
	}
	
  /**
   * Return the declarations of the given declaration container to which selection is applied.
   * This round-trip allows both the container and the selector to filter the candidates.
   * 
   * The default result is container.declarations(this), but clients cannot rely on that.
   */
  public void process(DeclarationContainer<?> container) throws LookupException {
  	container.declarations(selector());
  }

	public void proceed(LookupStrategy next) throws LookupException {
		if(willProceed()) {
			next.lookUp(this);
		}
	}
	
	public boolean willProceed() throws LookupException {
		return _accumulator == null;
	}
		
	public DeclarationSelector<D> selector() {
		return _selector;
	}
	
	private DeclarationSelector<D> _selector;
	
	private List<D> _accumulator;
	
}

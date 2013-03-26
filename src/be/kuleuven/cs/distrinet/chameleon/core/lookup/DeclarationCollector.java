package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

public class DeclarationCollector<D extends Declaration> extends Collector<D> {

	public DeclarationCollector(DeclarationSelector<D> selector) {
		super(selector);
	}
	
	protected DeclarationCollector(){}
	
	public void process(List<? extends Declaration> candidates) throws LookupException {
		List<Declaration> tmp = (List<Declaration>) candidates;
		List<D> t=(List<D>) tmp;
		if(_accumulator != null) {
    	tmp.addAll(_accumulator);
    	t = selector().selection(tmp);
    }
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
	
//  /**
//   * Return the declarations of the given declaration container to which selection is applied.
//   * This round-trip allows both the container and the selector to filter the candidates.
//   * 
//   * The default result is container.declarations(this), but clients cannot rely on that.
//   */
//  public void process(DeclarationContainer container) throws LookupException {
//  	container.declarations(selector());
//  }

	public boolean willProceed() throws LookupException {
		return _accumulator == null;
	}
		
	private List<D> _accumulator;

	@Override
	void storeCachedResult(List cached) {
		_accumulator = cached;
	}
	
}

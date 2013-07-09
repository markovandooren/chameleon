package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

/**
 * @author Marko van Dooren
 *
 * @param <D>
 */
public class DeclarationCollector<D extends Declaration> extends Collector<D> {

	public DeclarationCollector(DeclarationSelector<D> selector) {
		super(selector);
	}
	
	protected DeclarationCollector(){}
	    
	public void process(List<? extends Declaration> candidates) throws LookupException {
		List<Declaration> tmp = (List<Declaration>) candidates;
		int size = candidates.size();
		if(size == 1) {
			_accumulator = (D) candidates.get(0);
		} else if(size > 1) {
			throw new LookupException("Multiple matches found using selector "+selector().toString(),selector());	
		}
	}
	
	public D result() throws LookupException {
		if(_accumulator == null) {
			throw new LookupException("No result has been found using selector "+selector().toString(),selector());
		} else {
			return _accumulator;
		}
	}
	
	public boolean willProceed() throws LookupException {
		return _accumulator == null;
	}
		
	private D _accumulator;

	@Override
	void storeCachedResult(Declaration cached) {
		_accumulator = (D) cached;
	}
	
}

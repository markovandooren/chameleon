package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

public abstract class Collector<D extends Declaration> {

	public Collector(DeclarationSelector<D> selector) {
		setSelector(selector);
	}
	
	protected Collector() {}
	
	private DeclarationSelector<D> _selector;
	
	public void setSelector(DeclarationSelector<D> selector) {
		if(selector == null) {
			throw new ChameleonProgrammerException("The selector of a declaration collector cannot be null");
		}
		_selector = selector;
	}

	public void proceed(LexicalLookupStrategy current) throws LookupException {
		if(willProceed()) {
			LookupStrategy next = current.nextStrategy();
			if(next == null) {
				throw new LookupException("The next strategy in the lookup is null.");
			}
			next.lookUp(this);
		}
	}

	public DeclarationSelector<D> selector() {
		return _selector;
	}

  public abstract boolean willProceed() throws LookupException;
  
	public void process(LocalLookupStrategy<?> local) throws LookupException {
		process(local.declarations(selector()));
	}	

  protected abstract void process(List<? extends Declaration> candidates) throws LookupException;

	abstract void storeCachedResult(List cached);
	
	abstract D result() throws LookupException;

}

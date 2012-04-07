package chameleon.core.lookup;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.exception.ChameleonProgrammerException;

public abstract class Collector<D extends Declaration> {

	public Collector(DeclarationSelector<D> selector) {
		if(selector == null) {
			throw new ChameleonProgrammerException("The selector of a declaration collector cannot be null");
		}
		_selector = selector;
	}
	
	private DeclarationSelector<D> _selector;

	public void proceed(LookupStrategy next) throws LookupException {
		if(willProceed()) {
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
  
  public abstract void process(List<? extends Declaration> candidates) throws LookupException;

}
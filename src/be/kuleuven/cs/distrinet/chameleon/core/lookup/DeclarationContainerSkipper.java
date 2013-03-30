/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;

public class DeclarationContainerSkipper<D extends Declaration> extends DeclarationCollector<D> {

	private Collector<D> _original;
	
	private DeclarationContainer _skipped;
	
	public DeclarationContainerSkipper(Collector<D> original, DeclarationContainer skipped) {
		super();
		_original = original;
		_skipped = skipped;
	}

	@Override
	public void process(LocalLookupContext<?> local) throws LookupException {
		if(! local.declarationContainer().sameAs(_skipped)) {
			_original.process(local);
		}
	}
	
	@Override
	public void proceed(LexicalLookupContext current) throws LookupException {
		_original.proceed(current);
	}
}

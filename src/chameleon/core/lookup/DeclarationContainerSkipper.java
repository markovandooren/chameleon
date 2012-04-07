/**
 * 
 */
package chameleon.core.lookup;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;

public class DeclarationContainerSkipper<D extends Declaration> extends DeclarationCollector<D> {

	private DeclarationCollector<D> _original;
	
	private DeclarationContainer _skipped;
	
	public DeclarationContainerSkipper(DeclarationCollector<D> original, DeclarationContainer skipped) {
		super(null, false);
		_original = original;
		_skipped = skipped;
	}

//	@Override
//	public void process(DeclarationContainer container) throws LookupException {
//		if(! container.equals(_skipped)) {
//			_original.process(container);
//		}
//	}
	
	@Override
	public void proceed(LookupStrategy next) throws LookupException {
		_original.proceed(next);
	}
}
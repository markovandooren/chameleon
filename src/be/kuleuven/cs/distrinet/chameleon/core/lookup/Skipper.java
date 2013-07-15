/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.relation.WeakPartialOrder;

public class Skipper<D extends Declaration> extends DeclarationSelector<D> {

	private DeclarationSelector<D> _original;
	
	private DeclarationContainer _skipped;
	
	public Skipper(DeclarationSelector<D> original, DeclarationContainer skipped) {
		_original = original;
		_skipped = skipped;
	}

	@Override
	public List<D> declarations(DeclarationContainer container) throws LookupException {
		if(container.equals(_skipped)) {
			return new ArrayList<D>();
		} else {
			return super.declarations(container);
		}
	}

//	@Override
//	public WeakPartialOrder order() {
//		return _original.order();
//	}

//	@Override
//	public boolean selectedBasedOnName(Signature signature) throws LookupException {
//		return _original.selectedBasedOnName(signature);
//	}

	@Override
	public boolean canSelect(Class<? extends Declaration> type) {
		return _original.canSelect(type);
	}

//	@Override
//	public boolean selectedRegardlessOfName(D declaration) throws LookupException {
//		return _original.selectedRegardlessOfName(declaration);
//	}

	@Override
	public String selectionName(DeclarationContainer container) throws LookupException {
		return _original.selectionName(container);
	}

	@Override
	public List<? extends Declaration> declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
		return _original.declarators(selectionCandidates);
	}

	@Override
	public List<D> selection(List<? extends Declaration> declarators) throws LookupException {
		return _original.selection(declarators);
	}
	
}

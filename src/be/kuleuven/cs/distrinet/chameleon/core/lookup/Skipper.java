/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;

public class Skipper<D extends Declaration> extends DeclarationSelector<D> {

	private DeclarationSelector<D> _original;
	
	private DeclarationContainer _skipped;
	
	public Skipper(DeclarationSelector<D> original, DeclarationContainer skipped) {
		_original = original;
		_skipped = skipped;
	}

	@Override
	public List<? extends SelectionResult> declarations(DeclarationContainer container) throws LookupException {
		if(container.equals(_skipped)) {
			return new ArrayList<SelectionResult>();
		} else {
			return super.declarations(container);
		}
	}

	@Override
	public boolean canSelect(Class<? extends Declaration> type) {
		return _original.canSelect(type);
	}

	@Override
	public String selectionName(DeclarationContainer container) throws LookupException {
		return _original.selectionName(container);
	}

	@Override
	public List<? extends SelectionResult> declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
		return _original.declarators(selectionCandidates);
	}

	@Override
	public List<? extends SelectionResult> selection(List<? extends Declaration> declarators) throws LookupException {
		return _original.selection(declarators);
	}
	
}

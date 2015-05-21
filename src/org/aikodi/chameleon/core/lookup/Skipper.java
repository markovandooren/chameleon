/**
 * 
 */
package org.aikodi.chameleon.core.lookup;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;

import com.google.common.collect.ImmutableList;

public class Skipper<D extends Declaration> implements DeclarationSelector<D> {

	private DeclarationSelector<D> _original;
	
	private DeclarationContainer _skipped;
	
	public Skipper(DeclarationSelector<D> original, DeclarationContainer skipped) {
		_original = original;
		_skipped = skipped;
	}

	@Override
	public List<? extends SelectionResult> declarations(DeclarationContainer container) throws LookupException {
		if(container.equals(_skipped)) {
			return ImmutableList.of();
		} else {
			return DeclarationSelector.super.declarations(container);
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

//	@Override
//	public List<? extends SelectionResult> declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
//		return _original.declarators(selectionCandidates);
//	}
//
	@Override
	public List<? extends SelectionResult<D>> selection(List<? extends Declaration> declarators) throws LookupException {
		return _original.selection(declarators);
	}
	
}

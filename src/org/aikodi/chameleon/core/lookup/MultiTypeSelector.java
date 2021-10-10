package org.aikodi.chameleon.core.lookup;

import com.google.common.collect.ImmutableSet;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.util.Set;

public abstract class MultiTypeSelector<D extends Declaration> extends NameBasedSelector<D> {

	private Set<Class<? extends D>> _classes;
	
	public MultiTypeSelector(Set<Class<? extends D>> classes) {
		_classes = ImmutableSet.copyOf(classes);
	}
	
	@Override
	protected boolean hasSelectableType(Declaration selectionDeclaration) {
		return canSelect(selectionDeclaration.getClass());
	}

	@Override
	public boolean canSelect(Class<? extends Declaration> type) {
		for(Class c: _classes) {
			if(c.isAssignableFrom(type)) {
				return true;
			}
		}
		return false;
	}

}

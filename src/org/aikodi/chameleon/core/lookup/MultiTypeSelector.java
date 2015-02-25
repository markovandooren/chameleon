package org.aikodi.chameleon.core.lookup;

import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;

import com.google.common.collect.ImmutableSet;

public abstract class MultiTypeSelector<D extends Declaration> extends SelectorWithoutOrder<D> {

	private Set<Class<? extends D>> _classes;
	
	public MultiTypeSelector(Set<Class<? extends D>> classes) {
		_classes = ImmutableSet.copyOf(classes);
	}
	
//	/**
//	 * Return the signature that is used by this selector for selecting declarations.
//	 */
// /*@
//   @ public behavior
//   @
//   @ post \result != null;
//   @*/
//	public abstract String name signature();

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

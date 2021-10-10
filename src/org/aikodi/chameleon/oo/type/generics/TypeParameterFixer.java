package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.util.Lists;

import java.util.List;

/**
 * <p>A class for handling recursively defined type parameters.</p>
 * 
 * <p>When computing its {@link #locallyDeclaredDeclarations()}, it 
 * calls {@link TypeParameter#resolveForRoundTrip()} on each type parameter
 * and returns the results. This prevents an infinite loop when unfolding
 * the type of the parameter for checking subtyping.</p>
 * 
 * @author Marko van Dooren
 */
public abstract class TypeParameterFixer extends ElementImpl implements DeclarationContainer{

	@Override
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		// Here, we can go to the outer type, get all type variables
		// invoke cloneForStub() on all of them, and use them.
		List<Declaration> result = Lists.create();
		List<TypeParameter> allTypeParameters = parameters();
		for(TypeParameter parameter: allTypeParameters) {
			result.add(parameter.resolveForRoundTrip());
		}
		return result;
	}

	protected abstract List<TypeParameter> parameters() throws LookupException;

}
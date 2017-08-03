package org.aikodi.chameleon.aspect.core.model.pointcut.pattern;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;

public class AncestorPattern<D extends Declaration> extends DeclarationPattern {

	public AncestorPattern(Class<D> type, DeclarationPattern nested) {
		_ancestorType = type;
		_nestedPattern = nested;
	}
	
	public Class<D> ancestorType() {
		return _ancestorType;
	}
	
	private Class<D> _ancestorType;

	@Override
	public boolean eval(Declaration declaration) throws LookupException {
		D decl = declaration.lexical().nearestAncestor(ancestorType());
		return ancestorPattern().eval(decl);
	}
	
	public DeclarationPattern ancestorPattern() {
		return _nestedPattern;
	}
	
	private DeclarationPattern _nestedPattern;

	@Override
	public DeclarationPattern clone() {
		return new AncestorPattern(ancestorType(),ancestorPattern().clone());
	}
}

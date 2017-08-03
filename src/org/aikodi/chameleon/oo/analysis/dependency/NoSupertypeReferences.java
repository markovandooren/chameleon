package org.aikodi.chameleon.oo.analysis.dependency;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.oo.type.inheritance.SubtypeRelation;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.predicate.UniversalPredicate;

public class NoSupertypeReferences extends UniversalPredicate<Element,Nothing> {

	public NoSupertypeReferences() {
		super(Element.class);
	}

	@Override
	public boolean uncheckedEval(Element t) throws Nothing {
		return t.lexical().nearestAncestor(SubtypeRelation.class) == null;
	}

}

package org.aikodi.chameleon.oo.analysis.dependency;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.oo.type.inheritance.SubtypeRelation;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class NoSupertypeReferences extends UniversalPredicate<Element,Nothing> {

	public NoSupertypeReferences() {
		super(Element.class);
	}

	@Override
	public boolean uncheckedEval(Element t) throws Nothing {
		return t.nearestAncestor(SubtypeRelation.class) == null;
	}

}

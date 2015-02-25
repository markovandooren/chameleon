package org.aikodi.chameleon.aspect.core.model.pointcut.pattern;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;

/**
 * A pattern that matches only the declaration referenced by the cross reference of this pattern.
 * 
 * @author Marko van Dooren
 */
public class CrossReferencePattern extends DeclarationPattern {

	public CrossReferencePattern(CrossReference crossReference) {
		_crossReference = crossReference;
	}

	@Override
	public DeclarationPattern clone() {
		return new CrossReferencePattern(crossReference());
	}

	@Override
	public boolean eval(Declaration object) throws LookupException {
		return object.sameAs(crossReference().getElement());
	}

	public CrossReference crossReference() {
		return _crossReference;
	}
	
	private CrossReference _crossReference;
}

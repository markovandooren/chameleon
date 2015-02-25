package org.aikodi.chameleon.oo.member;

import org.aikodi.chameleon.core.declaration.Declaration;

public class HidesRelation<D extends Declaration> extends DeclarationComparator<D> {

	public HidesRelation(Class<D> kind) {
		super(kind);
	}
	
}

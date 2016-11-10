package org.aikodi.chameleon.support.type;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;

import com.google.common.collect.ImmutableList;

public class EmptyTypeElement extends ElementWithModifiersImpl implements Declarator {

	@Override
	protected EmptyTypeElement cloneSelf() {
		return new EmptyTypeElement();
	}

	@Override
   public List<Declaration> declaredDeclarations() {
		return ImmutableList.of();
	}

}

package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.declaration.TargetDeclaration;
import org.aikodi.chameleon.core.lookup.LookupException;

public interface DeclarationWithType extends TargetDeclaration {

	public Type declarationType() throws LookupException;
}

package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;

public interface DeclarationWithType extends Declaration {

	public Type declarationType() throws LookupException;
}

package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.lookup.Stub;

public interface DeclaratorStub extends Stub {

	@Override
   public Declarator generator();
}

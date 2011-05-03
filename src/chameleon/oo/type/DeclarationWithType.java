package chameleon.oo.type;

import chameleon.core.declaration.Signature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.lookup.LookupException;

public interface DeclarationWithType<E extends DeclarationWithType<E,S>, S extends Signature> extends TargetDeclaration<E, S>{

	public Type declarationType() throws LookupException;
}

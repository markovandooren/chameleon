package chameleon.oo.type;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

public interface DeclarationWithType<E extends DeclarationWithType<E,S,D>, S extends Signature, D extends Declaration> extends TargetDeclaration<E, S, D>{

	public Type declarationType() throws LookupException;
}

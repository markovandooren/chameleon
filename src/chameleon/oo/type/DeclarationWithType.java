package chameleon.oo.type;

import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.lookup.LookupException;

public interface DeclarationWithType extends TargetDeclaration {

	public Type declarationType() throws LookupException;
}

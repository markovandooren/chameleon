package be.kuleuven.cs.distrinet.chameleon.oo.type;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public interface DeclarationWithType extends TargetDeclaration {

	public Type declarationType() throws LookupException;
}

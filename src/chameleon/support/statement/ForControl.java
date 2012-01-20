package chameleon.support.statement;

import java.util.List;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.oo.variable.Variable;

public abstract class ForControl<E extends ForControl> extends NamespaceElementImpl<E> implements DeclarationContainer<E>{
	
	public abstract List<? extends Variable> declarations() throws LookupException;
	
	public abstract E clone();


}

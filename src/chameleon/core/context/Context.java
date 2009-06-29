package chameleon.core.context;

import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;

public abstract class Context {

	public Context() {
	}
	

	public abstract <T extends Declaration> T lookUp(DeclarationSelector<T> selector) throws LookupException;

}
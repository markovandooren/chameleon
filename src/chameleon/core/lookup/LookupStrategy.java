package chameleon.core.lookup;

import java.util.Set;

import org.apache.log4j.Logger;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;

/**
 * 
 * @author Marko van Dooren
 */
public abstract class LookupStrategy {

	public LookupStrategy() {
	}
	

	public abstract <T extends Declaration> T lookUp(DeclarationSelector<T> selector) throws LookupException;

	public final static Logger logger = Logger.getLogger("lookup");
}
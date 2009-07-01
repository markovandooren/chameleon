package chameleon.core.declaration;

import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * A general interface for elements that contain declarations. This interface allows the
 * class TargetContext to perform a local search. The declarations() method defines which
 * declarations are locally defined, after which the other objects involved in a lookup can 
 * decide if the request element is defined by the current declaration container or not.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <P>
 */
public interface DeclarationContainer<E extends DeclarationContainer, P extends Element> extends Element<E,P> {
  
	//@FIXME: rename this class, and use a different one for the pure container? NamespartParts cannot be referrenced
	// because it is not a target, so it makes little sense to add a declarations(selector) or targetContext() method there.
	
  /**
   * Return the declarations the are defined in this declaration container.
   * @return
   * @throws LookupException 
   */
  public Set<? extends Declaration> declarations() throws LookupException;
  
//  public <T extends Declaration> List<T> declarations(DeclarationSelector<T> selector) throws MetamodelException;
  
//  public Context localContext();

}

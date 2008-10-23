package chameleon.core.declaration;

import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;

public interface DeclarationContainer<E extends DeclarationContainer, P extends Element> extends Element<E,P> {
  
	//@FIXME: rename this class, and use a different one for the pure container? NamespartParts cannot be referrenced, so
	//        it makes little sense to add a declarations(...) method there.
	
  /**
   * Return the declarations in this declaration container.
   * @return
 * @throws MetamodelException 
   */
  public Set<Declaration> declarations() throws MetamodelException;
  
  /**
   * Return those declarations of this declaration container that are selected
   * by the given signature selector.
   * 
   * @param <T> The type of the arguments selected by the given signature selector. This type
   *            shoud be inferred automatically.
   * @param selector
   * @return
   * @throws MetamodelException
   */
  public <T extends Declaration> Set<T> declarations(DeclarationSelector<T> selector) throws MetamodelException;

}

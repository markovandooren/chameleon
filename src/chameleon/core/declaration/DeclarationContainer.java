package chameleon.core.declaration;

import java.util.List;
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
  
//  public <T extends Declaration> List<T> declarations(DeclarationSelector<T> selector) throws MetamodelException;

}

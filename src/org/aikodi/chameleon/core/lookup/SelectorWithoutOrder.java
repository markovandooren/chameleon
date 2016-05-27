package org.aikodi.chameleon.core.lookup;

import java.util.Collections;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.util.Lists;

/**
 * A selector that does not impose an order on the selected elements. Each
 * candidates is evaluated on its own, and all matching declarations are
 * returned.
 * 
 * @author Marko van Dooren
 *
 * @param <D> The type of the selected declarations.
 */
public interface SelectorWithoutOrder<D extends Declaration> extends DeclarationSelector<D>{

  @Override
  public default List<? extends SelectionResult<D>> selection(List<? extends Declaration> declarators) throws LookupException {
  	List<? extends SelectionResult<D>> result;
     if (declarators.size() > 0) {
        List<SelectionResult<D>> tmp = Lists.create();
        for (Declaration decl : declarators) {
           SelectionResult<D> e = selection(decl);
           if (e != null) {
              tmp.add(e);
           }
        }
        result = tmp;
     } else {
        result = Collections.EMPTY_LIST;
     }
     return result;
  }

  /**
   * @param declarator The declarator of which the selection result must be
   * computed.
   * @return The result of the selection. This may be null.
   * @throws LookupException The given declarator could not be evaluated because
   * of a problem in the model.
   */
  public SelectionResult<D> selection(Declaration declarator) throws LookupException;

}

/**
 * 
 */
package org.aikodi.chameleon.core.lookup;

import org.aikodi.chameleon.core.declaration.Declaration;

/**
 * A class of selectors that select declarations without imposing an order on them. If there are multiple
 * declarations that are different according to the sameAs() method, and that satisfy the criteria for an 
 * individual declaration, then all of them will be selected. For each set of equal declarations only a 
 * single declaration is selected.
 *  
 * @author Marko van Dooren
 *
 * @param <D> The type of the declaration that is selected by this selector.
 */
public abstract class NameSelector<D extends Declaration> extends NameBasedSelector<D> {
	
	/**
	 * Create a new selector that selects declarations of the type represented by the given class object.
	 * 
	 * @param selectedClass A class object that represents the type of the selected declarations.
	 */
 /*@
   @ public behavior
   @
   @ pre selectedClass != null;
   @
   @ post selectedClass() == selectedClass;
   @*/
	public NameSelector(Class<D> selectedClass) {
		_class = selectedClass;
	}
	
	private Class<D> _class;
	
	/**
	 * Return the class object that represents the type of the declarations
	 * that are selected by this selector.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Class<D> selectedClass() {
		return _class;
	}
	
  @Override
  public boolean canSelect(Class<? extends Declaration> type) {
  	return selectedClass().isAssignableFrom(type);
  }

	@Override
   public String toString() {
//		return getClass().getName() +" class: "+selectedClass().getName()+" "+name();
		return name();
	}
	
	@Override
   protected boolean hasSelectableType(Declaration selectionDeclaration) {
		return _class.isInstance(selectionDeclaration);
	}
}

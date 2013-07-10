/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.relation.WeakPartialOrder;

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
public abstract class SelectorWithoutOrder<D extends Declaration> extends DeclarationSelector<D> {
	
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
	public SelectorWithoutOrder(Class<D> selectedClass) {
		_class = selectedClass;
	}
	
	/**
	 * Return the signature that is used by this selector for selecting declarations.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public abstract Signature signature();
	
//	/**
//	 * Determine whether the given declaration is selected regardless of its name. The name of the signature
//	 * must be ignored during this check. Other elements from a signature, such as the arguments types of a
//	 * method, still have to be checked.
//	 * 
//	 * The default implementation returns true.
//	 * 
//	 * DESIGN: the selection procedure is split in two with renaming in mind.
//	 */
// /*@
//   @ public behavior
//   @
//   @ pre declaration != null;
//   @*/
//	@Override
//	public boolean selectedRegardlessOfName(D declaration) throws LookupException {
//		return true;
//	}

//	/**
//	 * This method does nothing because this selector has no real order.
//	 */
//	@Override
//	protected void applyOrder(List<D> tmp) throws LookupException {
//	}

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
	@Override
	public Class<D> selectedClass() {
		return _class;
	}
	
//	/**
//	 * A class that represents a relation that only contains a pair of elements when they are equal.
//	 * 
//	 * @author Marko van Dooren
//	 *
//	 * @param <D> The type of the elements to which the relation applies.
//	 */
//	public static class EqualityOrder<D extends Element> extends WeakPartialOrder<D> {
//		
//		/**
//		 * Two elements are in the relation if they are equal.
//		 */
//	 /*@
//	   @ also public behavior
//	   @
//	   @ post \result == first.sameAs(second);
//	   @*/
//		@Override
//		public boolean contains(D first, D second) throws LookupException {
//			return first.sameAs(second);
//		}
//	}

	@Override
	public String selectionName(DeclarationContainer container) {
		return signature().name();
	}

	public String toString() {
		return getClass().getName() +" class: "+selectedClass().getName()+" "+signature();
	}
	
	/**
	 * The cache used by a signature equality selector is:
	 * 
	 * Map<String,Declaration>
	 */
	@Override
	protected void updateCache(Cache cache, D selection) {
		Map<String,Declaration> map = (Map<String, Declaration>) cache.get(this);
		if(map == null) {
			map = new HashMap<String,Declaration>();
			cache.put(this, map);
		}
		map.put(signature().name(), selection);
	}
	
	@Override
	protected D readCache(Cache cache) {
		Map<String,Declaration> map = (Map<String, Declaration>) cache.get(this);
		if(map != null) {
			return (D) map.get(signature().name());
		} else {
			return null;
		}
	}

  protected D selection(Declaration declarator) throws LookupException {
  	// We first perform the checks on the selectionDeclaration, since a signature check may be
  	// very expensive.
  	D result = null;
  	Signature signature = declarator.signature();
		if(signature!=null && signature.sameAs(signature())) {
  		Declaration selectionDeclaration = declarator.selectionDeclaration();
  		if(_class.isInstance(selectionDeclaration)) {
  			result = (D) selectionDeclaration.actualDeclaration();
  		}
  	}
  	return result;
  }
  
  @Override
  public List<D> selection(List<? extends Declaration> declarators) throws LookupException {
  	List<D> tmp = new ArrayList<D>();
  	for(Declaration decl: declarators) {
  		D e = selection(decl);
  		if(e != null) {
  			tmp.add(e);
  		}
  	}
    return tmp;
  }


  /**
   * Return the list of declarations in the given set that are selected.
   * 
   * @param selectionCandidates
   *        The list containing the declarations that are checked for a match with {@link #selects(Signature)}}.
   * @return
   * @throws LookupException
   */
  public List<? extends Declaration> declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
  	List<Declaration> result = new ArrayList<Declaration>();
  	for(Declaration selectionCandidate: selectionCandidates) {
  		Signature signature = selectionCandidate.signature();
			if(signature!=null && signature.sameAs(signature())) {
  			Declaration selectionDeclaration = selectionCandidate.selectionDeclaration();
  			if(_class.isInstance(selectionDeclaration)) {
  				result.add(selectionCandidate.declarator());
  			}
  		} 
  	}
  	return result;
  }
}

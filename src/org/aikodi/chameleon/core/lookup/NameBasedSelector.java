package org.aikodi.chameleon.core.lookup;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.util.Lists;

/**
 * A class of selectors that do not impose an order on selected elements.
 * If there are multiple declarations with the correct signature, all of them
 * will be returned.
 * 
 * @author Marko van Dooren
 *
 * @param <D> The type of the selected declaration.
 */
public abstract class NameBasedSelector<D extends Declaration> implements DeclarationSelector<D>, SelectorWithoutOrder<D> {

   /**
    * @return The name of this selector.
    */
	public abstract String name();
	
	/**
	 * The selection name is the name of the signature.
	 */
 /*@
   @ public behavior@/
   @
   @ post \result == signature().name();
   @*/
	@Override
	public String selectionName(DeclarationContainer container) {
		return name();
	}

	/**
	 * Returns the selected declaration in case of a match.
	 * 
	 * @param declarator The declaration that declares the selected declaration.
	 * @return
	 * @throws LookupException
	 */
 /*@
   @ public behavior
   @
   @ pre declarator != null;
   @
   @ post \result == null || \result.sameAs(declarator.selectionDeclaration().actualDeclaration());
   @ post ! isCorrectType(declarator.selectionDeclaration()) ==> \result == null;
   @ post declarator.signature() == null ==> \result == null;
   @ post ! declarator.signature().sameAs(signature) ==> \result == null;
   @*/
  public SelectionResult selection(Declaration declarator) throws LookupException {
  	// We first perform the checks on the selectionDeclaration, since a signature check may be
  	// very expensive.
  	D result = null;
		if(correctSignature(declarator)) {
  		Declaration selectionDeclaration = declarator.selectionDeclaration();
  		if(hasSelectableType(selectionDeclaration)) {
  			result = (D) selectionDeclaration.actualDeclaration();
  		}
  	}
  	return result;
  }
  
   protected boolean correctSignature(Declaration declaration) throws LookupException {
      Signature signature = declaration.signature();
      if(signature == null) {
        throw new LookupException("The signature of a declaration of class "+declaration.getClass()+" is null.");
      }
      String name = signature.name();
      if(name == null) {
        throw new LookupException("The name of a declaration of class "+declaration.getClass()+"with signature of class"+signature.getClass()+" is null.");
      }
      return name.equals(name()) && ! signature.hasMorePropertiesThanName();
   }
  
  /**
   * Check whether the type of the given declaration is selected by this selector.
   * @param selectionDeclaration
   * @return
   */
	protected abstract boolean hasSelectableType(Declaration selectionDeclaration);
	
	/**
	 * The cache used by a name selector is:
	 * 
	 * Map<String,Declaration>
	 */
	@Override
	public void updateCache(Cache cache, D selection) {
		Map<String,Declaration> map = (Map<String, Declaration>) cache.get(this);
		if(map == null) {
			map = new HashMap<String,Declaration>();
			cache.put(this, map);
		}
		map.put(name(), selection);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public D readCache(Cache cache) {
		Map<String,Declaration> map = (Map<String, Declaration>) cache.get(this);
		if(map != null) {
			return (D) map.get(name());
		} else {
			return null;
		}
	}

}

package chameleon.core.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;

public abstract class TwoPhaseDeclarationSelector<D extends Declaration> extends DeclarationSelector<D> {

  /**
   * This method decides which declarations are candidates for selection <b>provided that it is of the correct
   * type</b>. This method does not take the signature into account. That check is performed by the selected() method.
   * The reason to split this up is to support aliases without have to create a separate trio of 
   * interface, actualdeclaration, and aliasdeclaration for each declaration. The alias contains only the alias name. Further
   * checks are performed on the 'selectionDeclaration'. Since the selectionDeclaration still have the original signature, the
   * latter signature must of course be ignored.
   */
 /*@
   @ public behavior
   @
   @ pre selectedClass().isInstance(declaration);
   @*/
  public abstract boolean selectedRegardlessOfName(D declaration) throws LookupException;

  /**
   * Return the declaration of type D that would be selected based on the
   * given input declaration. 
   * 
   * First, selectionDeclaration() is invoked on the
   * input declaration, resulting in declaration 'd'. 
   * 
   * Second, 'd' is then passed to the filter()
   * method to see if it would be selected. 
   * 
   * Finally, if filter selects 'd', actualDeclaration()
   * is invoked to produce the actual declaration that is selected.
   * 
   * In most cases, both selectionDeclaration() and actualDeclaration() of the Declaration
   * will simply return the declaration on which they were invoked. For formal type parameters, and
   * actual type parameters, this is not the case.
   * 
   * @param declarator
   * @return
   * @throws LookupException
   */
 /*@
   @ public behavior
   @
   @ post \result == declaration | \result == null;
   @ post ! selectedClass().isInstance(declaration) ==> \result == null;
   @*/
  protected D selection(Declaration declarator) throws LookupException {
  	// We first perform the checks on the selectionDeclaration, since a signature check may be
  	// very expensive.
  	D result = null;
  	if(selectedBasedOnName(declarator.signature())) {
  		Declaration selectionDeclaration = declarator.selectionDeclaration();
  		if(selectedClass().isInstance(selectionDeclaration)) {
  			if(selectedRegardlessOfName((D)selectionDeclaration)) {
  				result = actualDeclaration(declarator);
  			}
  		}
  	}
  	return result;
  }

  /**
   * Return the list of declarations in the given set that are selected.
   * 
   * @param declarators
   *        The list containing the declarations that are checked for a match with {@link #selects(Signature)}}.
   * @return
   * @throws LookupException
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post (\forall D d;; \result.contains(d) == 
   @           (set.contains(d) && 
   @            selects(d) && 
   @            ! (\exists D other; set.contains(other); order().strictOrder().contains(other,d))));
   @*/
  public List<D> selection(List<? extends Declaration> declarators) throws LookupException {
    List<D> tmp = new ArrayList<D>();
      for(Declaration decl: declarators) {
        D e = selection(decl);
        if(e != null) {
          tmp.add(e);
        }
      }
      order().removeBiggerElements(tmp);
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
  	Map<D,Declaration> tmp = new HashMap<D,Declaration>();
  	List<D> Ds = new ArrayList<D>();
  	Class<D> selectedClass = selectedClass();
  	for(Declaration decl: selectionCandidates) {
  		if(selectedBasedOnName(decl.signature())) {
  			Declaration selectionDeclaration = decl.selectionDeclaration();
			if(selectedClass.isInstance(selectionDeclaration)) {
  				if(selectedRegardlessOfName((D)selectionDeclaration)) {
  					tmp.put((D) selectionDeclaration,decl.declarator());
  					Ds.add((D) selectionDeclaration);
  				}
  			}
  		} 
  	}
  	order().removeBiggerElements(Ds);
  	List<Declaration> result = new ArrayList<Declaration>();
    for(D d: Ds) {
    	result.add(tmp.get(d));
    }
  	return result;
  }

  /**
   * Check if this selector selects the given declaration 
   * @param signature
   * @return
   */
 /*@
   @ public behavior
   @
   @ post \result == (selection(declaration) != null);
   @*/
  public boolean selects(Declaration declaration) throws LookupException {
    return selection(declaration) != null;
  }

  /**
   * This method decides if the given signature is selected by this declaration selector.
   */
 /*@
   @ public behavior
   @
   @ pre signature != null;
   @*/
  public abstract boolean selectedBasedOnName(Signature signature) throws LookupException;
  

}

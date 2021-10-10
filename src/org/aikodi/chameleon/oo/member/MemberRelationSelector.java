package org.aikodi.chameleon.oo.member;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.util.Lists;

import java.util.Iterator;
import java.util.List;

public class MemberRelationSelector<D extends Declaration> implements DeclarationSelector<D> {

	public MemberRelationSelector(Class<D> kind, D declaration,DeclarationComparator<D> comparator) {
		_selectedClass = kind;
		_declaration = declaration;
		_comparator = comparator;
	}
	
	protected DeclarationComparator<D> comparator() {
		return _comparator;
	}
	
	private DeclarationComparator<D> _comparator;
	
	private Class<D> _selectedClass;
	
	@Override
	public String selectionName(DeclarationContainer container) throws LookupException {
		return declaration().signature().name();
	}
	
	public D declaration() {
		return _declaration;
	}
	
	private D _declaration;

	public Class<D> selectedClass() {
		return _selectedClass;
	}

  @Override
  public boolean canSelect(Class<? extends Declaration> type) {
  	return selectedClass().isAssignableFrom(type);
  }

	@Override
	public List<? extends SelectionResult<D>> selection(List<? extends Declaration> declarators) throws LookupException {
  	List<SelectionResult<D>> tmp = Lists.create();
  	for(Declaration decl: declarators) {
  		SelectionResult<D> e = selection(decl);
  		if(e != null) {
  			tmp.add(e);
  		}
  	}
    return tmp;
	}
	
  protected SelectionResult<D> selection(Declaration declarator) throws LookupException {
  	// We first perform the checks on the selectionDeclaration, since a signature check may be
  	// very expensive.
  	SelectionResult<D> result = null;
  	Declaration selectionDeclaration = declarator.selectionDeclaration();
  		if(selects(selectionDeclaration)) {
  				result = (SelectionResult<D>)selectionDeclaration.actualDeclaration();
  		}
  	return result;
  }

	public boolean selects(Declaration declaration) throws LookupException {
		return selects(declaration.signature(), declaration);
	}
	
	public boolean selects(Signature signature, Declaration declaration) throws LookupException {
		return (declaration == null || selectedClass().isInstance(declaration)) && selectedBasedOnName(signature) && (declaration == null || selectedRegardlessOfName((D) declaration));
	}
	

	private boolean selectedBasedOnName(Signature signature) throws LookupException {
		return declaration().name().equals(signature.name());
	}

	private boolean selectedRegardlessOfName(D declaration) throws LookupException {
		return comparator().containsBasedOnRest(declaration(), declaration);
	}

	/**
	 * The filter method must be implemented to do something because some inheritance relations,
	 * such as subobjects, transform the members after they have been selected in the super class.
	 * After they are inherited, the filter method is invoked. Here the selection (and thus the
	 * overrides check) must be repeated. The new member may not be overridden by the declaration
	 * of this selector, even if the original member was. For methods for example, the containing
	 * type of the declaration must be a subtype of the containing type of the member. If the 
   * member is incorporated in a subobject, that relation no longer holds. 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void filter(List<? extends SelectionResult<D>> selected) throws LookupException {
		Iterator<? extends SelectionResult> iterator = selected.iterator();
		while(iterator.hasNext()) {
			SelectionResult result = iterator.next();
			Declaration declaration = result.finalDeclaration();
			if(selection(declaration) == null) {
				iterator.remove();
			}
		}
	}
}

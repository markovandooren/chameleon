package be.kuleuven.cs.distrinet.chameleon.oo.member;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;

public class MemberRelationSelector<D extends Declaration> extends DeclarationSelector<D> {

	public MemberRelationSelector(Class<D> kind, D declaration,DeclarationComparator<D> comparator) {
		_selectedClass = kind;
		_declaration = declaration;
		_comparator = comparator;
	}
	
	public DeclarationComparator<D> comparator() {
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
	public List<? extends SelectionResult> selection(List<? extends Declaration> declarators) throws LookupException {
  	List<SelectionResult> tmp = Lists.create();
  	for(Declaration decl: declarators) {
  		D e = selection(decl);
  		if(e != null) {
  			tmp.add(e);
  		}
  	}
  	//This is no order, so the following line can be removed.
  	//order().removeBiggerElements(tmp);
    return tmp;
	}
	
  protected D selection(Declaration declarator) throws LookupException {
  	// We first perform the checks on the selectionDeclaration, since a signature check may be
  	// very expensive.
  	D result = null;
  	Declaration selectionDeclaration = declarator.selectionDeclaration();
  		if(selects(selectionDeclaration)) {
  				result = (D) selectionDeclaration.actualDeclaration();
  		}
  	return result;
  }

	public boolean selects(Declaration declaration) throws LookupException {
		return selects(declaration.signature(), declaration);
	}
	
	public boolean selects(Signature signature, Declaration declaration) throws LookupException {
		return (declaration == null || selectedClass().isInstance(declaration)) && selectedBasedOnName(signature) && (declaration == null || selectedRegardlessOfName((D) declaration));
	}
	

	public boolean selectedBasedOnName(Signature signature) throws LookupException {
		return declaration().name().equals(signature.name());
	}

	public boolean selectedRegardlessOfName(D declaration) throws LookupException {
		return comparator().containsBasedOnRest(declaration(), declaration);
	}

//	public abstract boolean selectedBasedOnName(Signature signature) throws LookupException;
//	
//	public abstract boolean selectedRegardlessOfName(D declaration) throws LookupException;

	@Override
	public List<? extends SelectionResult> declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
  	List<SelectionResult> result = Lists.create();
  	for(Declaration selectionCandidate: selectionCandidates) {
  		if(selectedBasedOnName(selectionCandidate.signature())) {
  			Declaration selectionDeclaration = selectionCandidate.selectionDeclaration();
  			if(_selectedClass.isInstance(selectionDeclaration)) {
  				if(selectedRegardlessOfName((D)selectionDeclaration)) {
  					result.add(selectionCandidate.declarator());
  				}
  			}
  		} 
  	}
  	return result;

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
	@Override
	public void filter(List<? extends SelectionResult> selected) throws LookupException {
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

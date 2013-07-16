package be.kuleuven.cs.distrinet.chameleon.oo.member;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;

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
  	List<SelectionResult> tmp = new ArrayList<SelectionResult>();
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
		return comparator().containsBasedOnName(declaration().signature(), signature);
	}

	public boolean selectedRegardlessOfName(D declaration) throws LookupException {
		return comparator().containsBasedOnRest(declaration(), declaration);
	}

//	public abstract boolean selectedBasedOnName(Signature signature) throws LookupException;
//	
//	public abstract boolean selectedRegardlessOfName(D declaration) throws LookupException;

	@Override
	public List<? extends SelectionResult> declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
  	List<SelectionResult> result = new ArrayList<SelectionResult>();
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

}

package be.kuleuven.cs.distrinet.chameleon.oo.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectorWithoutOrder.EqualityOrder;
import be.kuleuven.cs.distrinet.chameleon.core.relation.WeakPartialOrder;

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

	@Override
	public Class<D> selectedClass() {
		return _selectedClass;
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
  	order().removeBiggerElements(tmp);
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

///**
//* Select the actual declaration selected by this declaration selector.
//* By default, this method returns declarator.selectionDeclaration()).actualDeclaration().
//* 
//* This method was introduced to allow the declarator to be returned instead (in DeclaratorSelector)
//* without having to add lookup method everywhere that would do the same as the original
//* lookup methods except for invoking a (currently non-existant) declarator() method instead
//* of selection().
//* 
//* @param declarator
//* @return
//* @throws LookupException
//*/
//protected D actualDeclaration(Declaration declarator) throws LookupException {
//	Declaration declaration = declarator.selectionDeclaration();
//	Declaration actualDeclaration = declaration.actualDeclaration();
//	if(selectedClass().isInstance(actualDeclaration)) {
//		return (D) actualDeclaration;
//	} else {
//		throw new LookupException("The actual declaration is of type "+actualDeclaration.getClass().getName()+" but a declaration of type "+selectedClass().getName()+" was expected.");
//	}
//}

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
	public List<? extends Declaration> declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
  	Map<D,Declaration> tmp = new HashMap<D,Declaration>();
  	List<D> Ds = new ArrayList<D>();
  	Class<D> selectedClass = selectedClass();
  	for(Declaration selectionCandidate: selectionCandidates) {
  		if(selectedBasedOnName(selectionCandidate.signature())) {
  			Declaration selectionDeclaration = selectionCandidate.selectionDeclaration();
  			if(selectedClass.isInstance(selectionDeclaration)) {
  				if(selectedRegardlessOfName((D)selectionDeclaration)) {
  					tmp.put((D) selectionDeclaration,selectionCandidate.declarator());
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
//  	Map<D,Declaration> tmp = new HashMap<D,Declaration>();
//  	List<D> Ds = new ArrayList<D>();
//  	Class<D> selectedClass = selectedClass();
//  	for(Declaration selectionCandidate: selectionCandidates) {
//  		Declaration selectionDeclaration = selectionCandidate.selectionDeclaration();
//  		if(selectedClass.isInstance(selectionDeclaration)) {
//  			if(selects(selectionCandidate.signature())) {
//  				tmp.put((D) selectionDeclaration,selectionCandidate.declarator());
//  				Ds.add((D) selectionDeclaration);
//  			}
//  		} 
//  	}
//  	order().removeBiggerElements(Ds);
//  	List<Declaration> result = new ArrayList<Declaration>();
//  	for(D d: Ds) {
//  		result.add(tmp.get(d));
//  	}
//  	return result;
	}

	@Override
	public WeakPartialOrder<D> order() {
		return new EqualityOrder<D>();
	}

}

package org.aikodi.chameleon.oo.type.inheritance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.member.Member;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Single;

import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;

public abstract class AbstractInheritanceRelation extends ElementWithModifiersImpl implements InheritanceRelation {
	
	public AbstractInheritanceRelation(TypeReference ref) {
		setSuperClassReference(ref);
	}
	
	@Override
   public Type target() throws LookupException {
		return superClass();
	}
	
	/**
	 * @return the super class.
	 * @throws LookupException The super class could not be resolved.
	 */
 /*@
   @ public behavior
   @
   @ post \result == superClassReference().getType();
   @*/
	public Type superClass() throws LookupException {
//			System.out.println("Inheritance relation of class "+fqn+" is going to look up super class ");
			Type result = null;
			try {
		    result = superClassReference().getElement();
			} 
			catch(NullPointerException exc) {
				String fullyQualifiedName = "CLASS WITHOUT PARENT!!!";
				if(parent() != null) {
				  fullyQualifiedName = fullyQualifiedName;
				}
				if(superClassReference() == null) {
				  throw new ChameleonProgrammerException("trying to get the super class of an inheritance relation that points to null in class" + fullyQualifiedName,exc);
				} else {
					throw exc;
				}
			}
//		  if(result != null) {
		  	return result;
//		  } else {
//		  	throw new LookupException("Superclass is null",superClassReference());
//		  }
	}
	
	/**
	 * Return a reference to the super class of this inheritance relation.
	 * @return
	 */
	@Override
   public TypeReference superClassReference() {
		return _superClass.getOtherEnd();
	}
	
	/**
	 * Set the type reference that points to the super class of this inheritance relation.
	 */
 /*@
   @ public behavior
   @
   @ post superClassReference() == ref;
   @*/
	public void setSuperClassReference(TypeReference ref) {
		set(_superClass,ref);
	}
	
	/**
	 * Return the set of members inherited through this inheritance relation.
	 * @param <M>
	 * @param <S>
	 * @param <F>
	 * @param kind
	 * @return
	 * @throws LookupException
	 */
	@Override
   public <M extends Member> 
  List<M> accumulateInheritedMembers(final Class<M> kind, List<M> current) throws LookupException {
		final List<M> potential = potentiallyInheritedMembers(kind);
		return (List<M>) removeNonMostSpecificMembers((List)current, (List)potential);
	}

	@Override
   public <X extends Member> 
	List<SelectionResult<X>> accumulateInheritedMembers(DeclarationSelector<X> selector, List<SelectionResult<X>> current) throws LookupException {
		final List<SelectionResult<X>> potential = potentiallyInheritedMembers(selector);
		return removeNonMostSpecificMembers(current, potential);
	}

	protected 
	<X extends Member> List<SelectionResult<X>> removeNonMostSpecificMembers(List<SelectionResult<X>> current, final List<SelectionResult<X>> potential) throws LookupException {
		if(current == Collections.EMPTY_LIST || current.isEmpty()) {
			return (List)potential; 
		}
		final List<SelectionResult<X>> toAdd = Lists.create(potential.size());
//		final List<SelectionResult<X>> toAdd = new LinkedList<>();
		int potentialSize = potential.size();
		int currentSize = current.size();
		int[] removedIndices = new int[currentSize];
		int removedIndex = -1;
		for(int potentialIndex =0; potentialIndex < potentialSize; potentialIndex++) {
			SelectionResult mm = potential.get(potentialIndex);
			Member m = (Member)mm.finalDeclaration();
			boolean add = true;
			for(int currentIndex = 0; add && currentIndex < currentSize; currentIndex++) {
				SelectionResult selectionResult = current.get(currentIndex);
				if(selectionResult != null) {
				Member alreadyInherited = (Member)selectionResult.finalDeclaration();
					// Remove the already inherited member if potentially inherited member m overrides or hides it.
					if(alreadyInherited.sameAs(m) || alreadyInherited.compatibleSignature(m)) {
						add = false;
					} else if (alreadyInherited.hides(m)) {
						add = false;
						alreadyInherited.hides(m);
					}
					else if((!alreadyInherited.sameAs(m)) && (m.compatibleSignature(alreadyInherited) || m.hides(alreadyInherited))) {
						removedIndex++;
						removedIndices[removedIndex] = currentIndex;
						current.set(currentIndex,null);
					}
				}
			}
			if(add == true) {
				toAdd.add(mm);
			}
		}
		int idx = toAdd.size() - 1;
		int min = Math.min(removedIndex, idx);
		for(int i=0; i <= min ;i++) {
			SelectionResult a = toAdd.get(idx);
			current.set(removedIndices[removedIndex],a);
			toAdd.remove(idx--);
			removedIndex--;
		}
		List result;
		if(removedIndex >= 0) {
			// there are still gaps. removedIndex points to the last gap.
			result = Lists.create(currentSize-(removedIndex+1));
			for(int i=0; i < currentSize; i++) {
				SelectionResult sel = current.get(i);
				if(sel != null) {
					result.add(sel);
				}
			}
		} else {
			current.addAll(toAdd);
			result = current;
		}
		return result;
	}

	public <M extends Member> List<M> potentiallyInheritedMembers(final Class<M> kind) throws LookupException {
		List<M> superMembers = superClass().members(kind);
		removeNonInheritableMembers((List)superMembers);
    return superMembers;
	}

	public List<Member> potentiallyInheritedMembers() throws LookupException {
		List<Member> superMembers = superClass().members();
		removeNonInheritableMembers((List)superMembers);
    return superMembers;
	}
	
	public <X extends Member> List<SelectionResult<X>> potentiallyInheritedMembers(
			final DeclarationSelector<X> selector) throws LookupException {
		List<SelectionResult<X>> superMembers = superClass().members(selector);
		return removeNonInheritableMembers((List)superMembers);
	}
	
  /**
   * Remove members that are not inheritable.
   */
 /*@
   @ public behavior
   @
   @ (\forall M m; members.contains(m); \old(members()).contains(m) && m.is(language(ObjectOrientedLanguage.class).INHERITABLE == Ternary.TRUE);
   @*/
	protected <X extends Member> List<SelectionResult<X>> removeNonInheritableMembers(List<SelectionResult<X>> members) throws LookupException {
		StaticChameleonProperty inheritable = language(ObjectOrientedLanguage.class).INHERITABLE;
		int size = members.size();
		for(int i =0; i< size;) {
			SelectionResult r = members.get(i);
			Declaration member = r.finalDeclaration();
			Ternary temp = member.is(inheritable);
			if (temp == Ternary.UNKNOWN) {
				temp = member.is(language(ObjectOrientedLanguage.class).INHERITABLE);
				throw new LookupException("For one of the members of super type " + superClass().getFullyQualifiedName()
						+ " it is unknown whether it is inheritable or not. Member type: " + member.getClass().getName());
			} else {
				if (temp == Ternary.FALSE) {
					SelectionResult last = members.remove(size-1);
					if(i < size -1) {
						members.set(i,last);
					}
					size--;
				} else {
					i++;
				}
			}
		}
		return members;
	}
	
	private Single<TypeReference> _superClass = new Single<TypeReference>(this);

	@Override
	public String toString() {
		return "<: "+superClassReference().toString();
	}

}

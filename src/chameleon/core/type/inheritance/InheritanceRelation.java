package chameleon.core.type.inheritance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;
import org.rejuse.logic.ternary.Ternary;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.member.Member;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public abstract class InheritanceRelation<E extends InheritanceRelation> extends ElementImpl<E,Type> {
	
	public abstract E clone();

	public InheritanceRelation(TypeReference ref) {
		setSuperClassReference(ref);
	}
	
	public List<? extends Element> getChildren() {
		List<Element> result = new ArrayList<Element>();
		result.add(superClassReference());
		return result;
	}
	
	/**
	 * Return the inherited class.
	 * @return
	 * @throws MetamodelException
	 */
 /*@
   @ public behavior
   @
   @ post \result == superClassReference().getType();
   @*/
	public Type superClass() throws MetamodelException {
		try {
		  Type result = superClassReference().getType();
		  if(result != null) {
		  	return result;
		  } else {
		  	throw new MetamodelException("Superclass ");
		  }
		} catch(NullPointerException exc) {
			throw new ChameleonProgrammerException("trying to get the super class of an inheritance relation that points to null");
		}
	}
	
	/**
	 * Return the inherited type, if this relation also introduces a subtype relation.
	 * @return
	 * @throws MetamodelException
	 */
	public abstract Type superType() throws MetamodelException;
	
	/**
	 * Return a reference to the inherited class.
	 * @return
	 */
	public TypeReference superClassReference() {
    return _superClass.getOtherEnd();
	}
	
 /*@
   @ public behavior
   @
   @ post superClassReference() == ref;
   @*/
	public void setSuperClassReference(TypeReference ref) {
		if(ref != null) {
			_superClass.connectTo(ref.getParentLink());
		} else {
			_superClass.connectTo(null);
		}
	}
	
	public <M extends Member<M,? super Type,S,F>, S extends Signature<S,M>, F extends Member<? extends Member,? super Type,S,F>> Set<M> potentiallyInheritedMembers(final Class<M> kind) throws MetamodelException {
		Set<M> superMembers = superClass().members(kind);
		Set<M> result = new HashSet<M>();
		for(M member:superMembers) {
	    Ternary temp = member.is(language().INHERITABLE);
	    if (temp == Ternary.TRUE) {
	      result.add(transform(member));
	    } else if (temp == Ternary.FALSE) {
	    } else {
	      //assert (temp == Ternary.UNKNOWN);
	      throw new MetamodelException(
	          "For one of the members of a super type of "
	              + superClass().getFullyQualifiedName()
	              + " it is unknown whether it is inheritable or not.");
	    }
		}
    return result;
	}
	
	public <M extends Member<M,? super Type,S,F>, S extends Signature<S,M>, F extends Member<? extends Member,? super Type,S,F>> M transform(M member) throws MetamodelException {
		M result = member.clone();
		//SUBSTITUTE GENERIC PARAMETERS, OR USE TRICK CONTEXT?
		result.setUniParent(getParent());
		return result;
	}
	
	private Reference<InheritanceRelation,TypeReference> _superClass = new Reference<InheritanceRelation, TypeReference>(this);

}

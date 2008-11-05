package chameleon.core.type.inheritance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;
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
	
	public <M extends Member> Set<M> potentiallyInheritedMembers(final Class<M> kind) throws MetamodelException {
		Set<M> superMembers = superClass().members(kind);
    try {
			new PrimitivePredicate<M>() {
			  public boolean eval(M method) throws MetamodelException {
			    Ternary temp = method.is(language().INHERITABLE);
			    boolean result;
			    if (temp == Ternary.TRUE) {
			      result = true;
			    } else if (temp == Ternary.FALSE) {
			      result = false;
			    } else {
			      //assert (temp == Ternary.UNKNOWN);
			      throw new MetamodelException(
			          "For one of the members of a super type of "
			              + superClass().getFullyQualifiedName()
			              + " it is unknown whether it is inheritable or not.");
			    }
			    return result;
			  }
			}.filter(superMembers);
		} catch(RuntimeException e) {
    	throw e;
    } catch (MetamodelException e) {
      throw e;
    } catch (Exception e) {
      throw new Error();
    }
    return superMembers;
	}
	
	private Reference<InheritanceRelation,TypeReference> _superClass = new Reference<InheritanceRelation, TypeReference>(this);

}

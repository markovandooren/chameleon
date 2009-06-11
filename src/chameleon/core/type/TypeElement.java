package chameleon.core.type;

import java.util.Set;

import org.rejuse.logic.ternary.Ternary;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.member.Member;
import chameleon.core.modifier.ModifierContainer;
import chameleon.core.statement.CheckedExceptionList;

/**
 * A class of elements that can be direct children of a type.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the element itself
 * @param <P> The type of the parent of the element
 */
public interface TypeElement<E extends TypeElement<E,P>, P extends Element> extends TypeDescendant<E, P>, ModifierContainer<E, P> {

  public abstract E clone();
  
  /**
   * Return the set of members introduced into the parent type (if any) of this type element.
   * @throws MetamodelException 
   */
 /*@
   @ public behavior
   @
   @ post \result != null; 
   @*/
  public Set<Member> getIntroducedMembers();

	public abstract CheckedExceptionList getCEL() throws MetamodelException;
	
	public abstract CheckedExceptionList getAbsCEL() throws MetamodelException;
    
}

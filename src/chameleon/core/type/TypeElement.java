package chameleon.core.type;

import java.util.Set;

import org.rejuse.logic.ternary.Ternary;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.member.Member;
import chameleon.core.modifier.ModifierContainer;

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
  
  /**
   * Check if this type element has the given property. 
   * 
   * If the given property does not apply directly to this type element, we check if it
   * is implied by the declared properties of this element. If the given property does apply
   * to this type element, we still need to check for conflicts with the declared properties.
   * 
   * @param property
   *        The property to be verified.
   */
 /*@
   @ behavior
   @
   @ pre property != null;
   @
   @ post ! property.appliesTo(this) ==> \result == declaredProperties().implies(property);
   @ post property.appliesTo(this) ==> \result == declaredProperties().with(property).implies(property);
   @*/
  public Ternary is(Property<Element> property);
  
  /**
   * Return a property set representing the properties of this type element
   * as declared by its modifiers.
   */
 /*@
   @ behavior
   @
   @ post \result != null;
   @ post (\forall Modifier mod; modifiers().contains(mod);
   @        \result.properties.containsAll(mod.impliedProperties()));
   @*/
  public PropertySet<Element> declaredProperties();


}

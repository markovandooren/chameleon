package org.aikodi.chameleon.oo.type;

import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.ElementWithModifiers;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.member.Member;

/**
 * A class of elements that can be direct children of a type.
 * 
 * @author Marko van Dooren
 */
public interface TypeElement extends ElementWithModifiers {

  /**
   * Return the set of members introduced into the parent type (if any) of this type element.
   * @throws LookupException 
   */
 /*@
   @ public behavior
   @
   @ post \result != null; 
   @*/
  public List<? extends Member> getIntroducedMembers() throws LookupException;
  
  public default List<? extends Member> declaredMembers() {
    try {
       return getIntroducedMembers();
    } catch (LookupException e) {
       throw new ChameleonProgrammerException(
             "This should not happen. Element of class "
                   + this.getClass().getName()
                   + " threw a lookup exception in getIntroducedMembers. This exception ended up in declaredMembers. But if that is the case, then declaredMembers must be overridden to provide a proper definition.",
             e);
    }
 }

}

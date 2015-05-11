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
  public List<? extends Member> getIntroducedMembers();
  
  public default List<? extends Member> declaredMembers() {
  	return getIntroducedMembers();
  }

}

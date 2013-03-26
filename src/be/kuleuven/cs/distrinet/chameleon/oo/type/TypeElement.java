package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ElementWithModifiers;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;

/**
 * A class of elements that can be direct children of a type.
 * 
 * @author Marko van Dooren
 */
public interface TypeElement extends ElementWithModifiers {

	public abstract TypeElement clone();

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
  
  public List<? extends Member> declaredMembers();

}

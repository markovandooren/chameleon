package chameleon.oo.type;

import java.util.List;

import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.ElementWithModifiers;
import chameleon.core.namespace.NamespaceElement;
import chameleon.oo.member.Member;
import chameleon.oo.statement.CheckedExceptionList;

/**
 * A class of elements that can be direct children of a type.
 * 
 * @author Marko van Dooren
 */
public interface TypeElement extends NamespaceElement, ElementWithModifiers {

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

  public abstract CheckedExceptionList getCEL() throws LookupException;
	
	public abstract CheckedExceptionList getAbsCEL() throws LookupException;
    
}

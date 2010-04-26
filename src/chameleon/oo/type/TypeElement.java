package chameleon.oo.type;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.core.modifier.ElementWithModifiers;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.statement.CheckedExceptionList;

/**
 * A class of elements that can be direct children of a type.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the element itself
 * @param <P> The type of the parent of the element
 */
public interface TypeElement<E extends TypeElement<E,P>, P extends Element> extends NamespaceElement<E, P>, ElementWithModifiers<E, P> {

	public abstract E clone();

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

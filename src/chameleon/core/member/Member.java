package chameleon.core.member;

import java.util.Set;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.TypeElement;

/**
 * A class of type elements (@see {@link #TypeElement}) that have a signature and can thus be referenced. Examples
 * are methods, variable, types, properties,...
 * 
 * @author Marko van Dooren
 * 
 * <E> The type of the element
 * <P> The type of the parent
 * <S> The type of the signature
 * <F> The type of the family to which this member belongs. E should always be a subtype of F but
 * we cannot enforce this because Java is so primitive.
 */
public interface Member<E extends Member<E,P,S,F>, P extends Element, S extends Signature, F extends Member> extends TypeElement<E,P>, Declaration<E,P,S,F> {
  
  
//  /**
//   * Set the signature of this member.
//   * @param signature
//   */
//  public void setSignature(S signature);

  public abstract E clone();
	
  /**
   * Check whether this member overrides the given member.
   */
 /*@
   @ public behavior
   @
   @ post other == null ==> \result == false;
   @*/
  public boolean overrides(Member other) throws LookupException;
  
  /**
   * Return a selector that selects members that could override this
   * member based on the signature and other properties.
   */
  public OverridesRelation<? extends Member> overridesSelector();
  
  /**
   * Check whether this member overrides the given member.
   */
 /*@
   @ public behavior
   @
   @ post other == null ==> \result == false;
   @*/
  public boolean hides(Member other) throws LookupException;
  
//  /**
//   * Check whether this is equivalent to given member.
//   */
// /*@
//   @ public behavior
//   @
//   @ post other == null ==> \result == false;
//   @*/
//  public boolean equivalentTo(Member other) throws LookupException;
  
  /**
   * Check whether this member can implement the given member.
   */
 /*@
   @ public behavior
   @
   @ post other == null ==> \result == false;
   @ post other != null && other.is(language().DEFINED)==> \result == false;
   @*/
  public boolean canImplement(Member other) throws LookupException;
  
  
  /**
   * Return the set of members that are overridden by this member.
   * @return
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ (\forall Member m; \result.contains(m); overrides(m));
   @*/
  public Set<Member> directlyOverriddenMembers() throws LookupException;

}

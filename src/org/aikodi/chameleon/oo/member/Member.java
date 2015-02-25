package org.aikodi.chameleon.oo.member;

import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.TypeElement;

/**
 * A class of type elements (@see {@link #TypeElement}) that have a signature and can thus be referenced. Examples
 * are methods, variable, types, properties,...
 * 
 * @author Marko van Dooren
 */
public interface Member extends TypeElement, Declaration {
  
  
  /**
   * Check whether this member overrides the given member.
   */
 /*@
   @ public behavior
   @
   @ post other == null ==> \result == false;
   @*/
  public boolean overrides(Member other) throws LookupException;
  
//  public boolean canOverride(Member other) throws LookupException;
//
//  public OverridesRelation<? extends Member> overridesRelation();
  
  /**
   * Return a selector that selects members that could override this
   * member based on the signature and other properties.
   */
  public MemberRelationSelector<? extends Member> overridesSelector();
  
  /**
   * Return a selector that selects members that could override this
   * member based on the signature and other properties.
   */
  public MemberRelationSelector<? extends Member> aliasSelector();
  
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
  public List<? extends Member> directlyOverriddenMembers() throws LookupException;

  public List<? extends Member> directlyAliasedMembers() throws LookupException;
  
  public List<? extends Member> directlyAliasingMembers() throws LookupException;

  public Set<? extends Member> overriddenMembers() throws LookupException;

  public Set<? extends Member> aliasedMembers() throws LookupException;

  public Set<? extends Member> aliasingMembers() throws LookupException;
}

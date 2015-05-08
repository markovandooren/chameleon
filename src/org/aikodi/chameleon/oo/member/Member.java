package org.aikodi.chameleon.oo.member;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeElement;
import org.aikodi.chameleon.util.Lists;

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
  public default boolean overrides(Member other) throws LookupException {
    //    // overriddenMembers().contains(other) does not work
    //    // because a member can also override non-lexical members
    //    // which are created on demand.
    //    if(overridden != null && overridden.contains(other)) {
    ////      System.out.println("Hit overridden: "+ ++HIT_OVERRIDDEN);
    //      return true;
    //    }
    //    if(notOverridden != null && notOverridden.contains(other)) {
    ////      System.out.println("Hit not overridden: "+ ++HIT_NOT_OVERRIDDEN);
    //      return false;
    //    }
    boolean overrides = overridesRelation().contains(this,other);
    //    if(overrides) {
    //      synchronized (this) {
    //        if(overridden == null) {
    //          overridden = new HashSet<>();
    //        }
    //        overridden.add(other);
    //      }
    //    } else {
    //      synchronized (this) {
    //        if(notOverridden == null) {
    //          notOverridden = new HashSet<>();
    //        }
    //        notOverridden.add(other);
    //      }
    //    }
    return overrides;
  }

  public default OverridesRelation<? extends Member> overridesRelation() {
    return new OverridesRelation<Member>(Member.class);
  }


//  public boolean canOverride(Member other) throws LookupException;
//
//  public OverridesRelation<? extends Member> overridesRelation();
  
  /**
   * Return a selector that selects members that could override this
   * member based on the signature and other properties.
   */
  public default MemberRelationSelector<? extends Member> overridesSelector() {
    return new MemberRelationSelector<Member>(Member.class,this,(DeclarationComparator<Member>) overridesRelation());
  }
  
  /**
   * Return a selector that selects members that could override this
   * member based on the signature and other properties.
   */
  public default MemberRelationSelector<? extends Member> aliasSelector() {
    DeclarationComparator<Member> aliasRelation = new DeclarationComparator<>(Member.class);
    return new MemberRelationSelector<Member>(Member.class,this,aliasRelation);
  }
  
  /**
   * Check whether this member overrides the given member.
   */
 /*@
   @ public behavior
   @
   @ post other == null ==> \result == false;
   @*/
  public default boolean hides(Member other) throws LookupException {
    return hidesRelation().contains(this,other);
  }
  
  public default HidesRelation<? extends Member> hidesRelation() {
    return new HidesRelation<Member>(Member.class);
  }

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
  public default boolean canImplement(Member other) throws LookupException {
    return language(ObjectOrientedLanguage.class).implementsRelation().contains(this,other);
  }
  
  
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
  public default List<? extends Member> directlyOverriddenMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyOverriddenBy(overridesSelector());
  }

  public default List<? extends Member> directlyAliasedMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyAliasedBy(aliasSelector());
  }
  
  public default List<? extends Member> directlyAliasingMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyAliasing(aliasSelector());
  }

  public default Set<? extends Member> overriddenMembers() throws LookupException {
    Set<Member> result = null;
    //    boolean cacheDeclarations = Config.cacheDeclarations();
    //    if(cacheDeclarations && _overriddenMembersCache != null) {
    //      result = new HashSet<Member>(_overriddenMembersCache);
    //    }
    if(result == null) {
      List<Member> todo = (List<Member>) directlyOverriddenMembers();
      Map<Type,List<Member>> visitedTypes = new HashMap<Type,List<Member>>();
      while(! todo.isEmpty()) {
        Member m = todo.get(0);
        todo.remove(0);
        Type containingType = m.nearestAncestor(Type.class);
        if(! visitedTypes.containsKey(containingType)) {
          visitedTypes.put(containingType, Lists.<Member>create());
        }
        List<Member> done = visitedTypes.get(containingType);
        boolean contains = false;
        for(Member member:done) {
          if(member.sameSignatureAs(m)) {
            contains = true;
            break;
          }
        }
        if(! contains) {
          done.add(m);
          todo.addAll(m.directlyOverriddenMembers());
          todo.addAll(m.aliasedMembers());
          todo.addAll(m.aliasingMembers());
        }
      }
      result = new HashSet<Member>();
      for(List<Member> members: visitedTypes.values()) {
        result.addAll(members);
      }
    }
    return result;
  }

  /**
   * @return the member that this member overrides. If there is more than
   * one candidate, an exception is thrown. If there is no candidate, null
   * is returned.
   */
  /*@
   @ public behavior
   @
   @ post directlyOverriddenMembers().size() == 0 ==> \result == null;
   @ post directlyOverriddenMembers().size() == 1 ==> directlyOverriddenMembers().contains(\result);
   @ post directlyOverriddenMembers().size() > 1 ==> false;
   @*/
  public default Member overriddenMember() throws LookupException {
    List<? extends Member> overridden = directlyOverriddenMembers();
    int size = overridden.size();
    if(size == 1) {
      return overridden.get(0);
    } else if (size > 1) {
      throw new LookupException("There is than one overridden member. Use directlyOverriddenMembers() instead.");
    } else {
      //FIXME Why doesn't this throw an exception?
      return null;
    }
  }

  /**
   * @return The <em>reachable</em> members that are aliased by this member.
   *         Note that for type constructor instantiations (List&lt;A&gt;) there
   *         can be multiple equal instances. Members of the other instances are
   *         considered unreachable. This method cannot find every theoretical
   *         match because the object does not know all of its 'twins'.
   * @throws LookupException
   */
  public default Set<? extends Member> aliasedMembers() throws LookupException {
    List<Member> todo = (List<Member>) directlyAliasedMembers();
    Set<Member> result = new HashSet<Member>();
    while(! todo.isEmpty()) {
      Member m = todo.get(0);
      todo.remove(0);
      if(result.add(m)) {
        todo.addAll(m.directlyAliasedMembers());
      }
    }
    return result;
  }

  /**
   * @return The <em>reachable</em> members that are an alias of this member.
   *         Note that for type constructor instantiations (List&lt;A&gt;) there
   *         can be multiple equal instances. Members of the other instances are
   *         considered unreachable. This method cannot find every theoretical
   *         match because the object does not know all of its 'twins'.
   * @throws LookupException
   */
  public default Set<? extends Member> aliasingMembers() throws LookupException {
    List<Member> todo = (List<Member>) directlyAliasingMembers();
    Set<Member> result = new HashSet<Member>();
    while(! todo.isEmpty()) {
      Member m = todo.get(0);
      todo.remove(0);
      if(result.add(m)) {
        todo.addAll(m.directlyAliasingMembers());
      }
    }
    return result;
  }
}

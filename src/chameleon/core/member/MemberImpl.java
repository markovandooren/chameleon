package chameleon.core.member;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.predicate.UnsafePredicate;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.SelectorWithoutOrder;
import chameleon.core.lookup.TwoPhaseDeclarationSelector;
import chameleon.core.method.Method;
import chameleon.core.method.MethodSignature;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeElementImpl;

public abstract class MemberImpl<E extends Member<E,P,S,F>,P extends Element, S extends Signature, F extends Member> extends TypeElementImpl<E, P> implements Member<E,P,S,F>{

  /**
   * Return the signature of this member.
   */
  public abstract S signature();
  
//  public final boolean overrides(Member other) throws LookupException {
//    ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
//    if(language != null) {
//		  StrictPartialOrder<Member> overridesRelation = language.overridesRelation();
//      return overridesRelation.contains(this, other);
//    } else {
//    	throw new LookupException("Language is null");
//    }
//  }
  
  public final boolean overrides(Member other) throws LookupException {
  	return overridesSelector().selects(other);
  }
  
//  public final boolean hides(Member other) throws LookupException {
//    ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
//    if(language != null) {
//    	StrictPartialOrder<Member> hidesRelation = language.hidesRelation();
//    	return hidesRelation.contains(this, other);
//    } else {
//    	throw new LookupException("Language is null");
//    }
//  }

  public final boolean hides(Member other) throws LookupException {
	  return ((HidesRelation)hidesSelector()).contains(this,other);
  }

  public final boolean canImplement(Member other) throws LookupException {
  	return language(ObjectOrientedLanguage.class).implementsRelation().contains(this,other);
  }

  /**
   * Return the members that are overridden by this member.
   * 
   * DOES NOT WORK WITH RENAMING YET!
   */
  public Set<Member> directlyOverriddenMembers() throws LookupException {
    List<Type> superTypes = nearestAncestor(Type.class).getDirectSuperTypes();
    // Collect the overridden members in the following set.
    final Set<Member> result = new HashSet<Member>();
    // Iterate over all super types.
    for(Type type: superTypes) {
      // Fetch all members from the current super type.
      Collection<Member> superMembers = type.members(Member.class);
      // Retain only those members that are overridden by this member. 
      new UnsafePredicate<Member,LookupException>() {
        public boolean eval(Member o) throws LookupException {
          return overrides(o);
        }
      }.filter(superMembers);
      result.addAll(superMembers);
    }
    return result;
  }
  
  /**
   * Return the member that this member overrides. If there is more than
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
  public Member overriddenMember() throws LookupException {
  	Set<Member> overridden = directlyOverriddenMembers();
  	int size = overridden.size();
  	if(size == 1) {
  		return overridden.iterator().next();
  	} else if (size > 1) {
  		throw new LookupException("There is than one overridden member. Use directlyOverriddenMembers() instead.");
  	} else {
  		return null;
  	}
  }
  
  public Declaration selectionDeclaration() throws LookupException {
  	return this;
  }
  
  public F actualDeclaration() throws LookupException {
  	return (F) this;
  }
  
  public Scope scope() throws ModelException {
  	Scope result = null;
  	ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX);
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
  }
  
  public MemberRelationSelector<? extends Member> overridesSelector() {
		return new OverridesRelationSelector<Member>(Member.class,this,_overridesSelector);
  }
  
  private static OverridesRelation<Member> _overridesSelector = new OverridesRelation<Member>(Member.class) {
		
		public boolean containsBasedOnRest(Member first, Member second) throws LookupException {
			return true;
		}

		@Override
		public boolean containsBasedOnName(Signature first, Signature second) {
			return first.name().equals(second.name());
		}
	};

  public HidesRelation<? extends Member> hidesSelector() {
		return _hidesSelector;
  }
  
  private static HidesRelation<Member> _hidesSelector = new HidesRelation<Member>(Member.class) {
		
		public boolean containsBasedOnRest(Member first, Member second) throws LookupException {
			return true;
		}

		@Override
		public boolean containsBasedOnName(Signature first, Signature second) {
			return first.name().equals(second.name());
		}
	};
}

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
import chameleon.core.property.ChameleonProperty;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.type.Type;
import chameleon.core.type.TypeElementImpl;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.oo.language.ObjectOrientedLanguage;

public abstract class MemberImpl<E extends MemberImpl<E,P,S,F>,P extends Element, S extends Signature, F extends Member> extends TypeElementImpl<E, Element> implements Member<E,P,S,F>{

  /**
   * Return the signature of this member.
   */
  public abstract S signature();
  
  public final boolean overrides(Member other) throws LookupException {
    StrictPartialOrder<Member> overridesRelation = language(ObjectOrientedLanguage.class).overridesRelation();
    return overridesRelation.contains(this, other);
  }
  
  public final boolean hides(Member other) throws LookupException {
    StrictPartialOrder<Member> hidesRelation = language(ObjectOrientedLanguage.class).hidesRelation();
    return hidesRelation.contains(this, other);
  }
  
  public final boolean equivalentTo(Member other) throws LookupException {
  	return language(ObjectOrientedLanguage.class).equivalenceRelation().contains(this,other);
  }

  public final boolean canImplement(Member other) throws LookupException {
  	return language(ObjectOrientedLanguage.class).implementsRelation().contains(this,other);
  }

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

}

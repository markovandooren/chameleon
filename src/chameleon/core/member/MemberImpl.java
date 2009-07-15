package chameleon.core.member;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.predicate.PrimitivePredicate;
import org.rejuse.property.Property;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.type.Type;
import chameleon.core.type.TypeElementImpl;

public abstract class MemberImpl<E extends MemberImpl<E,P,S,F>,P extends DeclarationContainer, S extends Signature, F extends Member> extends TypeElementImpl<E, P> implements Member<E,P,S,F>{

  /**
   * Return the signature of this member.
   */
  public abstract S signature();
  
  public final boolean overrides(Member other) throws LookupException {
    StrictPartialOrder<Member> overridesRelation = language().overridesRelation();
    return overridesRelation.contains(this, other);
  }
  
  public final boolean hides(Member other) throws LookupException {
    StrictPartialOrder<Member> hidesRelation = language().hidesRelation();
    return hidesRelation.contains(this, other);
  }
  
  public final boolean equivalentTo(Member other) throws LookupException {
  	return language().equivalenceRelation().contains(this,other);
  }

  public final boolean canImplement(Member other) throws LookupException {
  	return language().implementsRelation().contains(this,other);
  }

  public Set<Member> directlyOverriddenMembers() throws LookupException {
    List<Type> superTypes = nearestAncestor(Type.class).getDirectSuperTypes();
    // Collect the overridden members in the following set.
    final Set<Member> result = new HashSet<Member>();
    // Iterate over all super types.
    for(Type type: superTypes) {
      // Fetch all members from the current super type.
      Collection superMembers = type.members(Member.class);
      // Retain only those members that are overridden by this member. 
      try {
        new PrimitivePredicate<Member>() {
          public boolean eval(Member o) throws LookupException {
            return overrides(o);
          }
        }.filter(superMembers);
      } catch(LookupException e) {
        throw e; 
      } catch (Exception e) {
        e.printStackTrace();
        throw new Error();
      }
      result.addAll(superMembers);
    }
    return result;
  }
  
  public Declaration resolve() throws LookupException {
  	return this;
  }
  
  public Scope scope() throws MetamodelException {
  	Scope result = null;
  	Property<Element> scopeProperty = property(language().SCOPE_MUTEX);
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
  }

}

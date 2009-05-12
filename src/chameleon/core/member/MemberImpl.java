package chameleon.core.member;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;
import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.type.Type;
import chameleon.core.type.TypeElementImpl;

public abstract class MemberImpl<E extends MemberImpl<E,P,S,F>,P extends DeclarationContainer, S extends Signature, F extends Member> extends TypeElementImpl<E, P> implements Member<E,P,S,F>{

  /**
   * Return the signature of this member.
   */
  public abstract S signature();
  
  public final boolean overrides(Member other) throws MetamodelException {
    StrictPartialOrder<Member> overridesRelation = language().overridesRelation();
    return overridesRelation.contains(this, other);
  }
  
  public final boolean hides(Member other) throws MetamodelException {
    StrictPartialOrder<Member> hidesRelation = language().hidesRelation();
    return hidesRelation.contains(this, other);
  }
  
  public final boolean equivalentTo(Member other) throws MetamodelException {
  	return language().equivalenceRelation().contains(this,other);
  }

  public Set<Member> directlyOverriddenMembers() throws MetamodelException {
    List<Type> superTypes = getNearestType().getDirectSuperTypes();
    // Collect the overridden members in the following set.
    final Set<Member> result = new HashSet<Member>();
    // Iterate over all super types.
    for(Type type: superTypes) {
      // Fetch all members from the current super type.
      Collection superMembers = type.members(Member.class);
      // Retain only those members that are overridden by this member. 
      try {
        new PrimitivePredicate<Member>() {
          public boolean eval(Member o) throws MetamodelException {
            return overrides(o);
          }
        }.filter(superMembers);
      } catch(MetamodelException e) {
        throw e; 
      } catch (Exception e) {
        e.printStackTrace();
        throw new Error();
      }
      result.addAll(superMembers);
    }
    return result;
  }
  
  public Declaration resolve() throws MetamodelException {
  	return this;
  }

}

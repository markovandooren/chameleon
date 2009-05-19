package chameleon.core.variable;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.predicate.PrimitivePredicate;
import org.rejuse.property.Property;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.member.Member;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.type.Type;
import chameleon.core.type.TypeContainer;
import chameleon.core.type.TypeDescendant;
import chameleon.core.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class RegularMemberVariable extends RegularVariable<RegularMemberVariable,Type> implements MemberVariable<RegularMemberVariable>{
  
  
  /**
   * @param name
   */
  public RegularMemberVariable(SimpleNameSignature sig, TypeReference type) {
    super(sig, type, null);
  }
  
  /**
   * @param name
   */
  public RegularMemberVariable(SimpleNameSignature sig, TypeReference type, Expression initCode) {
    super(sig, type, initCode);
  }

  
  public final boolean equivalentTo(Member other) throws MetamodelException {
  	return language().equivalenceRelation().contains(this,other);
  }

  
  /**********
   * ACCESS *
   **********/

  //@FIXME: bad design
  protected boolean surroundingTypeAncestorOfOneOfTheSurroundingTypesOf(TypeDescendant other) throws MetamodelException {
    TypeContainer type = other.getNearestType();
    boolean found = true;
    while(type instanceof Type && (!found)) {
      if(((Type)type).assignableTo(parent())) {
        found = true; 
      }
      type = ((Type) type).parent(); //stupid generics 
    }
    return found;
  }

  protected RegularMemberVariable cloneThis() {
    Expression expr = null;
    if(getInitialization() != null) {
      expr = getInitialization().clone();
    }
    return new RegularMemberVariable(signature().clone(), (TypeReference)getTypeReference().clone(), expr);
  }
	  
  public Set<Member> getIntroducedMembers() {
    return Util.createSingletonSet(this);
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

  public boolean overrides(Member other) throws MetamodelException {
    StrictPartialOrder<Member> overridesRelation = language().overridesRelation();
    return overridesRelation.contains(this, other);
  }

  public boolean hides(Member other) throws MetamodelException {
    StrictPartialOrder<Member> hidesRelation = language().hidesRelation();
    return hidesRelation.contains(this, other);
  }

	public MemberVariable alias(SimpleNameSignature signature) {
		return new VariableAlias(signature,this);
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

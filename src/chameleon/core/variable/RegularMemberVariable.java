package chameleon.core.variable;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.predicate.AbstractPredicate;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.expression.Expression;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.core.modifier.Modifier;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class RegularMemberVariable extends RegularVariable<RegularMemberVariable,DeclarationContainer,MemberVariable> implements MemberVariable<RegularMemberVariable>{
  
  
  /**
   * @param name
   */
  public RegularMemberVariable(String name, TypeReference type) {
    super(new SimpleNameSignature(name), type, null);
  }
  
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

  
  public final boolean equivalentTo(Member other) throws LookupException {
  	return language(ObjectOrientedLanguage.class).equivalenceRelation().contains(this,other);
  }

  @Override
  public MemberVariable origin() {
  	return this;
  }
  
  /**********
   * ACCESS *
   **********/

//  //@FIXME: bad design
//  protected boolean surroundingTypeAncestorOfOneOfTheSurroundingTypesOf(TypeDescendant other) throws MetamodelException {
//    TypeContainer type = other.getNearestType();
//    boolean found = true;
//    while(type instanceof Type && (!found)) {
//      if(((Type)type).assignableTo(parent())) {
//        found = true; 
//      }
//      type = ((Type) type).parent(); //stupid generics 
//    }
//    return found;
//  }

  protected RegularMemberVariable cloneThis() {
    Expression expr = null;
    if(getInitialization() != null) {
      expr = getInitialization().clone();
    }
    return new RegularMemberVariable(signature().clone(), (TypeReference)getTypeReference().clone(), expr);
  }
	  
  public List<Member> getIntroducedMembers() {
    return Util.<Member>createSingletonList(this);
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
        new AbstractPredicate<Member>() {
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

  public boolean overrides(Member other) throws LookupException {
    StrictPartialOrder<Member> overridesRelation = language(ObjectOrientedLanguage.class).overridesRelation();
    return overridesRelation.contains(this, other);
  }

  public boolean canImplement(Member other) throws LookupException {
    StrictPartialOrder<Member> implementsRelation = language(ObjectOrientedLanguage.class).implementsRelation();
    return implementsRelation.contains(this, other);
  }

  public boolean hides(Member other) throws LookupException {
    StrictPartialOrder<Member> hidesRelation = language(ObjectOrientedLanguage.class).hidesRelation();
    return hidesRelation.contains(this, other);
  }

	public MemberVariable alias(SimpleNameSignature signature) {
		return new VariableAlias(signature,this);
	}

  public Scope scope() throws MetamodelException {
  	Scope result = null;
  	ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX);
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
  }

	public MemberVariable actualDeclaration() throws LookupException {
		return this;
	}

}

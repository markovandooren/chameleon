package be.kuleuven.cs.distrinet.chameleon.oo.variable;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.relation.StrictPartialOrder;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.member.DeclarationComparator;
import be.kuleuven.cs.distrinet.chameleon.oo.member.HidesRelation;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.member.MemberRelationSelector;
import be.kuleuven.cs.distrinet.chameleon.oo.member.OverridesRelation;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class RegularMemberVariable extends RegularVariable implements MemberVariable {
  
  
  /**
   * @param name
   */
  public RegularMemberVariable(String name, TypeReference type) {
    super(name, type, null);
  }
  
  /**
   * @param name
   */
  public RegularMemberVariable(String name, TypeReference type, Expression initCode) {
    super(name, type, initCode);
  }

  
  public final boolean equivalentTo(Member other) throws LookupException {
  	return language(ObjectOrientedLanguage.class).equivalenceRelation().contains(this,other);
  }

  @Override
  public boolean uniSameAs(Element other) {
  	if(other instanceof RegularMemberVariable) {
  		RegularMemberVariable var = (RegularMemberVariable) other;
  		Element parent = parent();
			Element otherParent = other.parent();
			return (parent != null && otherParent != null && otherParent.equals(parent) && sameSignatureAs(var));
  	} else {
  		return false;
  	}
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


  @Override
protected RegularMemberVariable cloneSelf() {
    return new RegularMemberVariable(name(),null,null);
  }
	  
  @Override
public List<Member> getIntroducedMembers() {
    return Util.<Member>createSingletonList(this);
  }

  @Override
public List<Member> declaredMembers() {
    return Util.<Member>createSingletonList(this);
  }

//  public Set<Member> directlyOverriddenMembers() throws LookupException {
//    List<Type> superTypes = nearestAncestor(Type.class).getDirectSuperTypes();
//    // Collect the overridden members in the following set.
//    final Set<Member> result = new HashSet<Member>();
//    // Iterate over all super types.
//    for(Type type: superTypes) {
//      // Fetch all members from the current super type.
//      Collection superMembers = type.members(Member.class);
//      // Retain only those members that are overridden by this member. 
//      try {
//        new AbstractPredicate<Member>() {
//          public boolean eval(Member o) throws LookupException {
//            return overrides(o);
//          }
//        }.filter(superMembers);
//      } catch(LookupException e) {
//        throw e; 
//      } catch (Exception e) {
//        e.printStackTrace();
//        throw new Error();
//      }
//      result.addAll(superMembers);
//    }
//    return result;
//  }

  @Override
public List<? extends Member> directlyOverriddenMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyOverriddenBy(overridesSelector());
  }

  @Override
public List<? extends Member> directlyAliasedMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyAliasedBy(aliasSelector());
  }

  @Override
public List<? extends Member> directlyAliasingMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyAliasing(aliasSelector());
  }
  
  @Override
public boolean overrides(Member other) throws LookupException {
  	return overridesSelector().selects(other);
  }

  public final boolean canOverride(Member other) throws LookupException {
  	return overridesRelation().contains(this, other);
  }

  @Override
public boolean canImplement(Member other) throws LookupException {
    StrictPartialOrder<Member> implementsRelation = language(ObjectOrientedLanguage.class).implementsRelation();
    return implementsRelation.contains(this, other);
  }

  @Override
public boolean hides(Member other) throws LookupException {
//    StrictPartialOrder<Member> hidesRelation = language(ObjectOrientedLanguage.class).hidesRelation();
//    return hidesRelation.contains(this, other);
  	return ((HidesRelation)hidesSelector()).contains(this,other);
  }

//	public MemberVariable alias(String name) {
//		return new VariableAlias(name,this);
//	}

  @Override
public Scope scope() throws ModelException {
  	Scope result = null;
  	ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX());
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
  }

	@Override
   public MemberVariable actualDeclaration() throws LookupException {
		return this;
	}

  @Override
public MemberRelationSelector<MemberVariable> overridesSelector() {
		return new MemberRelationSelector<MemberVariable>(MemberVariable.class,this,_overridesSelector);
  }
  
  public OverridesRelation<MemberVariable> overridesRelation() {
  	return _overridesSelector;
  }
  
  private static OverridesRelation<MemberVariable> _overridesSelector = new OverridesRelation<MemberVariable>(MemberVariable.class);
	
  public HidesRelation<? extends Member> hidesSelector() {
		return _hidesSelector;
  }
  
  private static HidesRelation<RegularMemberVariable> _hidesSelector = new HidesRelation<RegularMemberVariable>(RegularMemberVariable.class);
	
  @Override
public Set<? extends Member> overriddenMembers() throws LookupException {
  	List<Member> todo = (List<Member>) directlyOverriddenMembers();
  	Set<Member> result = new HashSet<Member>();
  	while(! todo.isEmpty()) {
  		Member m = todo.get(0);
  		todo.remove(0);
  		if(result.add(m)) {
  			todo.addAll(m.overriddenMembers());
  		}
  	}
  	return result;
  }

  @Override
public MemberRelationSelector<Member> aliasSelector() {
		return new MemberRelationSelector<Member>(Member.class,this,_aliasSelector);
  }
	
  private static DeclarationComparator<Member> _aliasSelector = new DeclarationComparator<Member>(Member.class);

	  @Override
   public Set<? extends Member> aliasedMembers() throws LookupException {
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

	  @Override
   public Set<? extends Member> aliasingMembers() throws LookupException {
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

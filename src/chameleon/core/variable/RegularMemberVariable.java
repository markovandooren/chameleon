package chameleon.core.variable;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.DeclarationComparator;
import chameleon.core.member.HidesRelation;
import chameleon.core.member.Member;
import chameleon.core.member.MemberRelationSelector;
import chameleon.core.member.OverridesRelation;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class RegularMemberVariable extends RegularVariable<RegularMemberVariable,MemberVariable> implements MemberVariable<RegularMemberVariable>{
  
  
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
  public boolean uniSameAs(Element other) {
  	if(other instanceof RegularMemberVariable) {
  		RegularMemberVariable var = (RegularMemberVariable) other;
  		Element parent = parent();
			Element otherParent = other.parent();
			return (parent != null && otherParent != null && otherParent.equals(parent) && signature().equals(var.signature()));
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

  public List<? extends Member> directlyOverriddenMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyOverriddenBy(overridesSelector());
  }

  public List<? extends Member> directlyAliasedMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyAliasedBy(aliasSelector());
  }

  public List<? extends Member> directlyAliasingMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyAliasing(aliasSelector());
  }
  
  public boolean overrides(Member other) throws LookupException {
  	return overridesSelector().selects(other);
  }

  public final boolean canOverride(Member other) throws LookupException {
  	return overridesRelation().contains(this, other);
  }

  public boolean canImplement(Member other) throws LookupException {
    StrictPartialOrder<Member> implementsRelation = language(ObjectOrientedLanguage.class).implementsRelation();
    return implementsRelation.contains(this, other);
  }

  public boolean hides(Member other) throws LookupException {
//    StrictPartialOrder<Member> hidesRelation = language(ObjectOrientedLanguage.class).hidesRelation();
//    return hidesRelation.contains(this, other);
  	return ((HidesRelation)hidesSelector()).contains(this,other);
  }

	public MemberVariable alias(SimpleNameSignature signature) {
		return new VariableAlias(signature,this);
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

	public MemberVariable actualDeclaration() throws LookupException {
		return this;
	}

  public MemberRelationSelector<? extends Member> overridesSelector() {
		return new MemberRelationSelector<MemberVariable>(MemberVariable.class,this,_overridesSelector);
  }
  
  public OverridesRelation<MemberVariable> overridesRelation() {
  	return _overridesSelector;
  }
  
  private static OverridesRelation<MemberVariable> _overridesSelector = new OverridesRelation<MemberVariable>(MemberVariable.class) {
		
		public boolean containsBasedOnRest(MemberVariable first, MemberVariable second) throws LookupException {
			return true;
		}

		/**
		 * Returns true by default. The "rest" method will check for equality of the signatures
		 * by default. 
		 */
		@Override
		public boolean containsBasedOnName(Signature first, Signature second) {
			return first.name().equals(second.name());
		}
	};
	
  public HidesRelation<? extends Member> hidesSelector() {
		return _hidesSelector;
  }
  
  private static HidesRelation<RegularMemberVariable> _hidesSelector = new HidesRelation<RegularMemberVariable>(RegularMemberVariable.class) {
		
  	/**
  	 * Returns true because only the name matters.
  	 */
		public boolean containsBasedOnRest(RegularMemberVariable first, RegularMemberVariable second) throws LookupException {
			return true;
		}
	};
	
  public Set<? extends Member> overriddenMembers() throws LookupException {
  	List<? extends Member> todo = directlyOverriddenMembers();
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

  public MemberRelationSelector<? extends Member> aliasSelector() {
		return new MemberRelationSelector<Member>(Member.class,this,_aliasSelector);
  }
	
  private static DeclarationComparator<Member> _aliasSelector = new DeclarationComparator<Member>(Member.class) {
		
		public boolean containsBasedOnRest(Member first, Member second) throws LookupException {
			return first.signature().sameAs(second.signature());
		}

		@Override
		public boolean containsBasedOnName(Signature first, Signature second) {
			return true;
		}
	};

	  public Set<? extends Member> aliasedMembers() throws LookupException {
		  List<Member> todo = (List<Member>) directlyAliasedMembers();
		  Set<Member> result = new HashSet<Member>();
		  while(! todo.isEmpty()) {
			  Member<?,?,?> m = todo.get(0);
			  todo.remove(0);
			  if(result.add(m)) {
				  todo.addAll(m.directlyAliasedMembers());
			  }
		  }
		  return result;
	  }

	  public Set<? extends Member> aliasingMembers() throws LookupException {
		  List<Member> todo = (List<Member>) directlyAliasingMembers();
		  Set<Member> result = new HashSet<Member>();
		  while(! todo.isEmpty()) {
			  Member<?,?,?> m = todo.get(0);
			  todo.remove(0);
			  if(result.add(m)) {
				  todo.addAll(m.directlyAliasingMembers());
			  }
		  }
		  return result;
	  }

  
}

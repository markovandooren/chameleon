package be.kuleuven.cs.distrinet.chameleon.oo.member;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeElementImpl;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;

public abstract class MemberImpl extends TypeElementImpl implements Member {

  /**
   * Return the signature of this member.
   */
  public abstract Signature signature();
  
  @Override
  public boolean sameSignatureAs(Declaration declaration)
  		throws LookupException {
  	return signature().sameAs(declaration.signature());
  }
  
  public String name() {
  	return signature().name();
  }
  
//  private static int HIT_OVERRIDDEN;
//  private static int HIT_NOT_OVERRIDDEN;
  
  public final boolean overrides(Member other) throws LookupException {
//  	// overriddenMembers().contains(other) does not work
//  	// because a member can also override non-lexical members
//  	// which are created on demand.
//  	if(overridden != null && overridden.contains(other)) {
////  		System.out.println("Hit overridden: "+ ++HIT_OVERRIDDEN);
//  		return true;
//  	}
//  	if(notOverridden != null && notOverridden.contains(other)) {
////  		System.out.println("Hit not overridden: "+ ++HIT_NOT_OVERRIDDEN);
//  		return false;
//  	}
  	boolean overrides = overridesRelation().contains(this,other);
//  	if(overrides) {
//  		synchronized (this) {
//  			if(overridden == null) {
//  				overridden = new HashSet<>();
//  			}
//  			overridden.add(other);
//  		}
//  	} else {
//  		synchronized (this) {
//  			if(notOverridden == null) {
//  				notOverridden = new HashSet<>();
//  			}
//  			notOverridden.add(other);
//  		}
//  	}
		return overrides;
  }
  
  private Set<Member> overridden;
  private Set<Member> notOverridden;
  
  public final boolean hides(Member other) throws LookupException {
	  return ((HidesRelation)hidesRelation()).contains(this,other);
  }

  public final boolean canImplement(Member other) throws LookupException {
  	return language(ObjectOrientedLanguage.class).implementsRelation().contains(this,other);
  }

  public List<? extends Member> directlyOverriddenMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyOverriddenBy(overridesSelector());
  }
  
  public List<? extends Member> directlyAliasedMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyAliasedBy(aliasSelector());
  }
  
  public List<? extends Member> directlyAliasingMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyAliasing(aliasSelector());
  }
  
  public Set<? extends Member> overriddenMembers() throws LookupException {
  	Set<Member> result = null;
//  	boolean cacheDeclarations = Config.cacheDeclarations();
//		if(cacheDeclarations && _overriddenMembersCache != null) {
//  		result = new HashSet<Member>(_overriddenMembersCache);
//  	}
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
//  		if(cacheDeclarations) {
//  			_overriddenMembersCache = new HashSet<Member>(result);
//  		}
  	}
  	return result;
  }
  
  @Override
  public synchronized void flushLocalCache() {
  	super.flushLocalCache();
//  	_overriddenMembersCache = null;
  }
  
//  private Set<Member> _overriddenMembersCache;
  
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
  	List<? extends Member> overridden = directlyOverriddenMembers();
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
  
  public Declaration actualDeclaration() throws LookupException {
  	return this;
  }
  
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
  
  public MemberRelationSelector<? extends Member> overridesSelector() {
		return new MemberRelationSelector<Member>(Member.class,this,_overridesRelation);
  }
  
  public OverridesRelation<? extends Member> overridesRelation() {
  	return _overridesRelation;
  }
  
  private static OverridesRelation<Member> _overridesRelation = new OverridesRelation<Member>(Member.class);

  public HidesRelation<? extends Member> hidesRelation() {
		return _hidesSelector;
  }
  
  private static HidesRelation<Member> _hidesSelector = new HidesRelation<Member>(Member.class);

  public MemberRelationSelector<? extends Member> aliasSelector() {
		return new MemberRelationSelector<Member>(Member.class,this,_aliasSelector);
  }
	
  private static DeclarationComparator<Member> _aliasSelector = new DeclarationComparator<Member>(Member.class);
	
	@Override
	public Declaration finalDeclaration() {
		return this;
	}
	
	@Override
	public Declaration template() {
		return finalDeclaration();
	}

	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}

}

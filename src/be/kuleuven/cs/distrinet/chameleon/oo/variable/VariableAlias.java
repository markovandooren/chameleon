package be.kuleuven.cs.distrinet.chameleon.oo.variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.Modifier;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.member.DeclarationComparator;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.member.MemberRelationSelector;
import be.kuleuven.cs.distrinet.chameleon.oo.member.OverridesRelation;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

public class VariableAlias extends VariableImpl implements MemberVariable {
	
	public VariableAlias(SimpleNameSignature sig, MemberVariable aliasedVariable) {
		super(sig);
		_aliasedVariable = aliasedVariable;
		setOrigin(aliasedVariable);
	}
	
	public MemberVariable aliasedVariable() {
		return _aliasedVariable;
	}
	
	private final MemberVariable _aliasedVariable;

	@Override
	protected VariableAlias cloneSelf() {
		return new VariableAlias(null,aliasedVariable());
	}

	public TypeReference getTypeReference() {
		return aliasedVariable().getTypeReference();
	}

	@SuppressWarnings("unchecked")
  public final boolean equivalentTo(Member other) throws LookupException {
  	return language(ObjectOrientedLanguage.class).equivalenceRelation().contains(this,other);
  }

	public String getName() {
		return signature().name();
	}

	public Type getType() throws LookupException {
		return aliasedVariable().getType();
	}

	public Ternary is(ChameleonProperty property) {
		return aliasedVariable().is(property);
	}

	private Multi<Modifier> _modifiers = new Multi<Modifier>(this);

	public void addModifier(Modifier modifier) {
		add(_modifiers,modifier);
	}

	public void removeModifier(Modifier modifier) {
		remove(_modifiers,modifier);
	}

	public boolean hasModifier(Modifier modifier) {
		return _modifiers.getOtherEnds().contains(modifier);
	}
	public List<Modifier> modifiers() {
		final List<Modifier> mine = _modifiers.getOtherEnds();
		List<Modifier> result = aliasedVariable().modifiers();
		new SafePredicate<Modifier>() {
			public boolean eval(final Modifier aliasedModifier) {
				return new SafePredicate<Modifier>() {
					public boolean eval(Modifier object) {
						PropertySet aliasedProperties = aliasedModifier.impliedProperties();
						aliasedProperties.addAll(object.impliedProperties());
						return aliasedProperties.consistent();
					}
				}.forAll(mine);
			}
		}.filter(result);
		result.addAll(mine);
		return result;
	}

	private PropertySet<Element,ChameleonProperty> myDefaultProperties() {
		return language().defaultProperties(this,explicitProperties());
	}

  public PropertySet<Element,ChameleonProperty> defaultProperties() {
    return filterProperties(myDefaultProperties(), aliasedVariable().defaultProperties());
  }
	
  public PropertySet<Element,ChameleonProperty> inherentProperties() {
    return filterProperties(myInherentProperties(), aliasedVariable().inherentProperties());
  }

	protected PropertySet<Element,ChameleonProperty> myInherentProperties() {
		return new PropertySet<Element,ChameleonProperty>();
	}
  public PropertySet<Element,ChameleonProperty> declaredProperties() {
    return filterProperties(myDeclaredProperties(), aliasedVariable().declaredProperties());
  }

	private PropertySet<Element,ChameleonProperty> myDeclaredProperties() {
		PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties().properties());
    }
    return result;
	}


  public Variable selectionDeclaration() {
		return this;
	}

	public LocalLookupContext targetContext() throws LookupException {
		return aliasedVariable().targetContext();
	}

	public MemberVariable alias(SimpleNameSignature signature) {
		return new VariableAlias(signature,this);
	}

	public List<? extends Member> directlyOverriddenMembers() throws LookupException {
		return aliasedVariable().directlyOverriddenMembers();
	}

	public boolean hides(Member other) throws LookupException {
		return aliasedVariable().hides(other);
	}

	public boolean overrides(Member other) throws LookupException {
		return aliasedVariable().overrides(other);
	}

//  public final boolean canOverride(Member other) throws LookupException {
//  	return aliasedVariable().canOverride(other);
//  }

  public boolean canImplement(Member other) throws LookupException {
		return aliasedVariable().canImplement(other);
  }

	public List<Member> getIntroducedMembers() {
		List<Member> result = new ArrayList<Member>();
		result.add(this);
		return result;
	}
  public List<Member> declaredMembers() {
    return Util.<Member>createSingletonList(this);
  }


	public Expression getInitialization() {
		return aliasedVariable().getInitialization();
	}

	public void setInitialization(Expression expr) {
		throw new ChameleonProgrammerException("Trying to set the initialization of a variable alias.");
	}
	
	public void setTypeReference(TypeReference ref) {
		throw new ChameleonProgrammerException("Trying to set the type reference of a variable alias.");
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

	public CheckedExceptionList getCEL() throws LookupException {
	  return new CheckedExceptionList();	
	}
	
	public CheckedExceptionList getAbsCEL() throws LookupException {
		return new CheckedExceptionList();
	}

	// copied from TypeElementImpl
  public void addModifiers(List<Modifier> modifiers) {
  	if(modifiers == null) {
  		throw new ChameleonProgrammerException("List passed to addModifiers is null");
  	} else {
  		for(Modifier modifier: modifiers) {
  			addModifier(modifier);
  		}
  	}
  }

	public MemberVariable actualDeclaration() throws LookupException {
		return this;
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public Declaration declarator() {
		return aliasedVariable().declarator();
	}

  public MemberRelationSelector<MemberVariable> overridesSelector() {
		return new MemberRelationSelector<MemberVariable>(MemberVariable.class,this,_overridesSelector);
  }

  public OverridesRelation<? extends Member> overridesRelation() {
  	return _overridesSelector;
  }
  
  private static OverridesSelector _overridesSelector = new OverridesSelector();
  
	private static class OverridesSelector extends OverridesRelation<MemberVariable> {
		private OverridesSelector() {
			super(MemberVariable.class);
		}

		@Override
		public boolean containsBasedOnRest(MemberVariable first, MemberVariable second) throws LookupException {
			return ((MemberRelationSelector)((VariableAlias)first).aliasedVariable().overridesSelector()).selectedRegardlessOfName(second);
		}

		@Override
		public boolean containsBasedOnName(Signature first, Signature second) throws LookupException {
			return first.name().equals(second.name());
		}
	}

	@Override
	public Set<? extends Member> overriddenMembers() throws LookupException {
		return aliasedVariable().overriddenMembers();
	}

  public MemberRelationSelector<Member> aliasSelector() {
		return new MemberRelationSelector<Member>(Member.class,this,_aliasSelector);
  }
	
  private static DeclarationComparator<Member> _aliasSelector = new DeclarationComparator<Member>(Member.class) {
		
		public boolean containsBasedOnRest(Member first, Member second) throws LookupException {
			return ((VariableAlias)first).aliasedVariable().aliasSelector().selectedRegardlessOfName(second);
		}

		@Override
		public boolean containsBasedOnName(Signature first, Signature second) {
			return true;
		}
	};

	@Override
	public List<? extends Member> directlyAliasedMembers() throws LookupException {
		return Util.createNonNullList(aliasedVariable());
	}

  public List<? extends Member> directlyAliasingMembers() throws LookupException {
    return nearestAncestor(Type.class).membersDirectlyAliasing(aliasSelector());
  }
  
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

}

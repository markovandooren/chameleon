package chameleon.core.variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.SafePredicate;
import org.rejuse.property.PropertySet;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.member.DeclarationComparator;
import chameleon.core.member.Member;
import chameleon.core.member.MemberRelationSelector;
import chameleon.core.member.OverridesRelation;
import chameleon.core.modifier.Modifier;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public class VariableAlias extends VariableImpl<VariableAlias,MemberVariable> implements MemberVariable<VariableAlias> {
	
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
	public VariableAlias clone() {
		return new VariableAlias(signature().clone(),aliasedVariable());
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

	private OrderedMultiAssociation<Variable, Modifier> _modifiers = new OrderedMultiAssociation<Variable, Modifier>(this);

	public void addModifier(Modifier modifier) {
		if ((modifier != null) && (!_modifiers.contains(modifier.parentLink()))) {
			_modifiers.add(modifier.parentLink());
		}
	}

	public void removeModifier(Modifier modifier) {
		_modifiers.remove(modifier.parentLink());
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
				}.forall(mine);
			}
		}.filter(result);
		result.addAll(mine);
		return result;
	}

	private PropertySet<Element,ChameleonProperty> myDeclaredProperties() {
		PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties().properties());
    }
    return result;
	}

	private PropertySet<Element,ChameleonProperty> myDefaultProperties() {
		return language().defaultProperties(this);
	}

  public PropertySet<Element,ChameleonProperty> defaultProperties() {
    return filterProperties(myDefaultProperties(), aliasedVariable().defaultProperties());
  }
	
  public PropertySet<Element,ChameleonProperty> declaredProperties() {
    return filterProperties(myDeclaredProperties(), aliasedVariable().declaredProperties());
  }

  public Variable selectionDeclaration() {
		return this;
	}

	public LookupStrategy targetContext() throws LookupException {
		return aliasedVariable().targetContext();
	}

	public List<? extends Element> children() {
    return aliasedVariable().children();
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
  	ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX);
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
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public Declaration declarator() {
		return aliasedVariable().declarator();
	}

  public MemberRelationSelector<? extends Member> overridesSelector() {
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
			return ((VariableAlias)first).aliasedVariable().overridesSelector().selectedRegardlessOfName(second);
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

  public MemberRelationSelector<? extends Member> aliasSelector() {
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

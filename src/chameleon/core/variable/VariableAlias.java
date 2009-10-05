package chameleon.core.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.SafePredicate;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.language.ObjectOrientedLanguage;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.member.Member;
import chameleon.core.modifier.Modifier;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class VariableAlias extends VariableImpl<VariableAlias,DeclarationContainer,MemberVariable> implements MemberVariable<VariableAlias> {
	
	public VariableAlias(SimpleNameSignature sig, MemberVariable aliasedVariable) {
		super(sig);
		_aliasedVariable = aliasedVariable;
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

	public Set<Member> directlyOverriddenMembers() throws LookupException {
		return aliasedVariable().directlyOverriddenMembers();
	}

	public boolean hides(Member other) throws LookupException {
		return aliasedVariable().hides(other);
	}

	public boolean overrides(Member other) throws LookupException {
		return aliasedVariable().overrides(other);
	}

  public boolean canImplement(Member other) throws LookupException {
		return aliasedVariable().canImplement(other);
  }

	public List<Member> getIntroducedMembers() {
		List<Member> result = new ArrayList<Member>();
		result.add(this);
		return result;
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

}

package chameleon.core.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.PrimitiveTotalPredicate;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.member.Member;
import chameleon.core.modifier.Modifier;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

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
  	return language().equivalenceRelation().contains(this,other);
  }

	public String getName() {
		return signature().getName();
	}

	public Type getType() throws LookupException {
		return aliasedVariable().getType();
	}

	public Ternary is(Property<Element> property) {
		return aliasedVariable().is(property);
	}

	private OrderedReferenceSet<Variable, Modifier> _modifiers = new OrderedReferenceSet<Variable, Modifier>(this);

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
		new PrimitiveTotalPredicate<Modifier>() {
			public boolean eval(final Modifier aliasedModifier) {
				return new PrimitiveTotalPredicate<Modifier>() {
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

	private PropertySet<Element> myDeclaredProperties() {
		PropertySet<Element> result = new PropertySet<Element>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties().properties());
    }
    return result;
	}

	private PropertySet<Element> myDefaultProperties() {
		return language().defaultProperties(this);
	}

  public PropertySet<Element> defaultProperties() {
    return filterProperties(myDefaultProperties(), aliasedVariable().defaultProperties());
  }
	
  public PropertySet<Element> declaredProperties() {
    return filterProperties(myDeclaredProperties(), aliasedVariable().declaredProperties());
  }

  public Variable resolveForMatch() {
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
  	Property<Element> scopeProperty = property(language().SCOPE_MUTEX);
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
  }

	public CheckedExceptionList getCEL() throws LookupException {
	  return new CheckedExceptionList(getNamespace().language());	
	}
	
	public CheckedExceptionList getAbsCEL() throws LookupException {
		return new CheckedExceptionList(getNamespace().language());
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

	public MemberVariable resolveForResult() throws LookupException {
		return this;
	}

}

package chameleon.core.variable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.PrimitiveTotalPredicate;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.MetamodelException;
import chameleon.core.context.TargetContext;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.member.Member;
import chameleon.core.modifier.Modifier;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public class VariableAlias extends VariableImpl<VariableAlias,Type> implements MemberVariable<VariableAlias> {
	
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

  public final boolean equivalentTo(Member other) throws MetamodelException {
  	return language().equivalenceRelation().contains(this,other);
  }

	public String getName() {
		return signature().getName();
	}

	public Type getNearestType() {
		return parent().getNearestType();
	}

	public Type getType() throws MetamodelException {
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

	private Set<Property<Element>> myDeclaredProperties() {
		Set<Property<Element>> result = new HashSet<Property<Element>>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties().properties());
    }
    return result;
	}
	
  public PropertySet<Element> declaredProperties() {
    Set<Property<Element>> result = aliasedVariable().properties().properties();
    final Set<Property<Element>> mine = myDeclaredProperties();
    new PrimitiveTotalPredicate<Property<Element>>() {
			@Override
			public boolean eval(final Property<Element> aliasedProperty) {
				return new PrimitiveTotalPredicate<Property<Element>>() {
					@Override
					public boolean eval(Property<Element> myProperty) {
						return !aliasedProperty.contradicts(myProperty);
					}
				}.forAll(mine);
			}
    	
    }.filter(result);
    result.addAll(mine);
    return new PropertySet<Element>(result);
  }
	
//	public void addModifier(Modifier modifier) {
//		throw new ChameleonProgrammerException("Trying to add a modifier to a variable alias.");
//	}
//
//	public void removeModifier(Modifier modifier) {
//		throw new ChameleonProgrammerException("Trying to remove a modifier from a variable alias.");
//	}

	public Variable resolve() {
		return this;
	}

	public TargetContext targetContext() throws MetamodelException {
		return aliasedVariable().targetContext();
	}

	public List<? extends Element> children() {
    return aliasedVariable().children();
	}

	public MemberVariable alias(SimpleNameSignature signature) {
		return new VariableAlias(signature,this);
	}

	public Set<Member> directlyOverriddenMembers() throws MetamodelException {
		return aliasedVariable().directlyOverriddenMembers();
	}

	public boolean hides(Member other) throws MetamodelException {
		return aliasedVariable().hides(other);
	}

	public boolean overrides(Member other) throws MetamodelException {
		return aliasedVariable().overrides(other);
	}

	public Set<Member> getIntroducedMembers() {
		Set<Member> result = new HashSet<Member>();
		result.add(this);
		return result;
	}

	public Expression getInitialization() {
		return aliasedVariable().getInitialization();
	}

	public void setInitialization(Expression expr) {
		throw new ChameleonProgrammerException("Trying to set the initialization of a variable alias.");
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

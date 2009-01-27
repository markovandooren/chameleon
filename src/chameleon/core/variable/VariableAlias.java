package chameleon.core.variable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.logic.ternary.Ternary;
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

	public void addModifier(Modifier modifier) {
		throw new ChameleonProgrammerException("Trying to add a modifier to a variable alias.");
	}

	public PropertySet<Element> declaredProperties() {
		return aliasedVariable().declaredProperties();
	}
	
  public final boolean equivalentTo(Member other) throws MetamodelException {
  	return language().equivalenceRelation().contains(this,other);
  }

	public String getName() {
		return signature().getName();
	}

	public Type getNearestType() {
		return getParent().getNearestType();
	}

	public Type getType() throws MetamodelException {
		return aliasedVariable().getType();
	}

	public Ternary is(Property<Element> property) {
		return aliasedVariable().is(property);
	}

	public List<Modifier> modifiers() {
		return aliasedVariable().modifiers();
	}

	public void removeModifier(Modifier modifier) {
		throw new ChameleonProgrammerException("Trying to remove a modifier from a variable alias.");
	}

	public Variable resolve() {
		return this;
	}

	public TargetContext targetContext() throws MetamodelException {
		return aliasedVariable().targetContext();
	}

	public List<? extends Element> getChildren() {
    return aliasedVariable().getChildren();
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
}

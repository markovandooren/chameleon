package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.DeclaratorSelector;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.SelectorWithoutOrder;
import chameleon.core.reference.CrossReference;
import chameleon.core.reference.SpecificReference;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.expression.Expression;
import chameleon.oo.variable.Variable;

public class EnumLabel extends SwitchLabel<EnumLabel> implements CrossReference<EnumLabel, Variable>{

	public EnumLabel(String name) {
		_signature = new SimpleNameSignature(name);
	}
	
	@Override
	public EnumLabel clone() {
		return new EnumLabel(name());
	}

	public List<? extends Element> children() {
		return new ArrayList<Element>();
	}
	
	public String name() {
		return _signature.name();
	}
	
	public void setName(String name) {
		_signature.setName(name);
	}
	
	private SimpleNameSignature _signature;
	
	public Variable getElement() throws LookupException {
		return getElement(selector());
	}
	
	public <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
		// class must move to Jnome because of enum dependency?
		Expression<?> switchExpr = nearestAncestor(SwitchStatement.class).getExpression();
		return switchExpr.getType().targetContext().lookUp(selector);
	}

	public Declaration getDeclarator() throws LookupException {
		return getElement(new DeclaratorSelector(selector()));
	}

	public DeclarationSelector<Variable> selector() {
		return new SelectorWithoutOrder<Variable>(Variable.class) {
			public Signature signature() {
				return _signature;
			}
		};
	}

	@Override
	public VerificationResult verifySelf() {
		return checkNull(name(), "The enum has no name", Valid.create());
	}

	@Override
	public LookupStrategy targetContext() throws LookupException {
		throw new LookupException("Requesting the target context of an enum label.");
	}
	
}

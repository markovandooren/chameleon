package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationCollector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclaratorSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectorWithoutOrder;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.core.reference.SpecificReference;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.Variable;

public class EnumLabel extends SwitchLabel implements CrossReference<Variable>{

	public EnumLabel(String name) {
		_signature = new SimpleNameSignature(name);
	}
	
	@Override
	public EnumLabel clone() {
		return new EnumLabel(name());
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
		Expression switchExpr = nearestAncestor(SwitchStatement.class).getExpression();
		DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
	  switchExpr.getType().targetContext().lookUp(collector);
	  return collector.result();
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
	public LookupContext targetContext() throws LookupException {
		throw new LookupException("Requesting the target context of an enum label.");
	}
	
}

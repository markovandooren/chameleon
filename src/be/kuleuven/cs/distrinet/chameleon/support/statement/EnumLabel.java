package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationCollector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclaratorSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.NameSelector;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.Variable;

public class EnumLabel extends SwitchLabel implements CrossReference<Variable>{

	public EnumLabel(String name) {
		setName(name);
	}
	
	@Override
	protected EnumLabel cloneSelf() {
		return new EnumLabel(name());
	}

	public String name() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	private String _name;
	
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
		return new NameSelector<Variable>(Variable.class) {
			public String name() {
				return _name;
			}
		};
	}

	@Override
	public Verification verifySelf() {
		return checkNull(name(), "The enum has no name", Valid.create());
	}

	@Override
	public LookupContext targetContext() throws LookupException {
		throw new LookupException("Requesting the target context of an enum label.");
	}
	
}

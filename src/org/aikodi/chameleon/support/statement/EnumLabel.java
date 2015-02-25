package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.DeclaratorSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.NameSelector;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.oo.expression.Expression;

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
	
	@Override
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

	@Override
   public Declaration getDeclarator() throws LookupException {
		return getElement(new DeclaratorSelector(selector()));
	}

	public DeclarationSelector<Variable> selector() {
		return new NameSelector<Variable>(Variable.class) {
			@Override
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

package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.modifier;

import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.property.ThrowingProperty;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

/**
 * @author Jens De Temmerman
 * @author Marko van Dooren
 */
public class Throwing extends ModifierWithParameters {

	
	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(ThrowingProperty.ID));
	}

	@Override
   protected Throwing cloneSelf() {
		return new Throwing();
	}

	@Override
	public Verification verifySelf() {
		Verification result = super.verifySelf();
		try {
			FormalParameter parameter = parameter();
			if (hasParameter() && !language(ObjectOrientedLanguage.class).isException(parameter.getType()))
				result = result.and(new BasicProblem(this, "Parameter must be a throwable"));
		} catch (LookupException e) {
			result = result.and(new BasicProblem(this, "Could not determine the parameter type"));
		}
		return result;
	}
}

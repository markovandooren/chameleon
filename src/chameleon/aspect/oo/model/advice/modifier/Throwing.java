package chameleon.aspect.oo.model.advice.modifier;

import org.rejuse.property.PropertySet;

import chameleon.aspect.oo.model.advice.property.ThrowingProperty;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.variable.FormalParameter;

/**
 * @author Jens De Temmerman
 * @author Marko van Dooren
 */
public class Throwing extends ModifierWithParameters {

	
	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(ThrowingProperty.ID));
	}

	protected Throwing cloneThis() {
		return new Throwing();
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = super.verifySelf();
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
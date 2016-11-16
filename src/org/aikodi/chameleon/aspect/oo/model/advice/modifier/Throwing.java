package org.aikodi.chameleon.aspect.oo.model.advice.modifier;

import org.aikodi.chameleon.aspect.oo.model.advice.property.ThrowingProperty;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.variable.FormalParameter;
import org.aikodi.rejuse.property.PropertySet;

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

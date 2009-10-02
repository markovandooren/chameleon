package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.language.ObjectOrientedLanguage;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class PureWildCard extends ActualTypeArgument<PureWildCard> {

	public TypeParameter capture(FormalTypeParameter formal) {
		CapturedTypeParameter newParameter = new CapturedTypeParameter(formal.signature().clone());
		for(TypeConstraint constraint: formal.constraints()) {
			newParameter.addConstraint(constraint.clone());
		}
   return newParameter;
	}

	@Override
	public PureWildCard clone() {
		return new PureWildCard();
	}

	// TypeVariable concept invoeren, en lowerbound,... verplaatsen naar daar? Deze is context sensitive. Hoewel, dat
	// wordt toch nooit direct vergeleken. Er moet volgens mij altijd eerst gecaptured worden, dus dan moet dit inderdaad
	// verplaatst worden.
	@Override
	public Type lowerBound() throws LookupException {
		return language(ObjectOrientedLanguage.class).getNullType();
	}

	@Override
	public Type type() throws LookupException {
		return new PureWildCardType(language(ObjectOrientedLanguage.class));
	}

	@Override
	public Type upperBound() throws LookupException {
		return language(ObjectOrientedLanguage.class).getDefaultSuperClass();
	}

	public List<Element> children() {
		return new ArrayList<Element>();
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}

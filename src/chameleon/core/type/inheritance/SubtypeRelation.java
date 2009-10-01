package chameleon.core.type.inheritance;

import org.rejuse.logic.ternary.Ternary;

import chameleon.core.element.Element;
import chameleon.core.language.ObjectOrientedLanguage;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class SubtypeRelation extends InheritanceRelation<SubtypeRelation> {

	public SubtypeRelation(TypeReference ref) {
		super(ref);
	}

	@Override
	public SubtypeRelation clone() {
		return new SubtypeRelation(superClassReference().clone());
	}

	@Override
	public Type superType() throws LookupException {
		return superClass();
	}
	@Override
	public VerificationResult verifyThis() {
		try {
			Type superClass = superClass();
			if(superClass.is(language(ObjectOrientedLanguage.class).EXTENSIBLE) == Ternary.TRUE) {
				return Valid.create();
			} else {
				return new SuperClassNotExtensible(this);
			}
		} catch (LookupException e) {
			return new SuperClassNotExtensible(this);
		}
	}
	
	public static class SuperClassNotExtensible extends BasicProblem {

		public SuperClassNotExtensible(Element element) {
			super(element, "The super class is not extensible");
		}
		
	}


}

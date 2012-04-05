package chameleon.support.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.member.Member;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeElementImpl;

public class EmptyTypeElement extends TypeElementImpl {

	@Override
	public EmptyTypeElement clone() {
		return new EmptyTypeElement();
	}

	public List<Member> getIntroducedMembers() {
		return new ArrayList<Member>();
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}

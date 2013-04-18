package be.kuleuven.cs.distrinet.chameleon.support.type;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeElementImpl;

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

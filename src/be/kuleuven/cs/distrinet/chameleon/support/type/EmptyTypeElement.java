package be.kuleuven.cs.distrinet.chameleon.support.type;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeElementImpl;

import com.google.common.collect.ImmutableList;

public class EmptyTypeElement extends TypeElementImpl {

	@Override
	protected EmptyTypeElement cloneSelf() {
		return new EmptyTypeElement();
	}

	@Override
   public List<Member> getIntroducedMembers() {
		return ImmutableList.of();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

}

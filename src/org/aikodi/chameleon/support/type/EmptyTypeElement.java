package org.aikodi.chameleon.support.type;

import java.util.List;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.member.Member;
import org.aikodi.chameleon.oo.type.TypeElementImpl;

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

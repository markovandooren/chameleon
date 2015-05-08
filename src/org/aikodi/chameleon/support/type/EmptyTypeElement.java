package org.aikodi.chameleon.support.type;

import java.util.List;

import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.oo.member.Member;
import org.aikodi.chameleon.oo.type.TypeElement;

import com.google.common.collect.ImmutableList;

public class EmptyTypeElement extends ElementWithModifiersImpl implements TypeElement {

	@Override
	protected EmptyTypeElement cloneSelf() {
		return new EmptyTypeElement();
	}

	@Override
   public List<Member> getIntroducedMembers() {
		return ImmutableList.of();
	}

}

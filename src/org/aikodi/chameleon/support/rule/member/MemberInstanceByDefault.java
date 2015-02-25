package org.aikodi.chameleon.support.rule.member;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.PropertyRule;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.member.Member;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

public class MemberInstanceByDefault extends PropertyRule<Member> {

	public MemberInstanceByDefault() {
		super(Member.class);
	}

	@Override
	public PropertySet<Element, ChameleonProperty> suggestedProperties(Member element) {
		return createSet(language(ObjectOrientedLanguage.class).INSTANCE);
	}

}

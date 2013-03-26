package be.kuleuven.cs.distrinet.chameleon.support.rule.member;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.property.PropertyRule;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;

public class MemberInstanceByDefault extends PropertyRule<Member> {

	public MemberInstanceByDefault() {
		super(Member.class);
	}

	@Override
	public PropertySet<Element, ChameleonProperty> suggestedProperties(Member element) {
		return createSet(language(ObjectOrientedLanguage.class).INSTANCE);
	}

}

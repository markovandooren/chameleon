package chameleon.support.rule.member;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import chameleon.core.element.Element;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.property.PropertyRule;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.member.Member;

public class MemberInheritableByDefault extends PropertyRule<Member> {

	public MemberInheritableByDefault() {
		super(Member.class);
	}

	@Override
	public PropertySet<Element,ChameleonProperty> suggestedProperties(Member element) {
		return createSet(language(ObjectOrientedLanguage.class).INHERITABLE);
	}

}

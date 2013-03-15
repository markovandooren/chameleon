package chameleon.support.rule.member;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.property.PropertyRule;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.member.Member;

public class MemberOverridableByDefault extends PropertyRule<Member> {

	public MemberOverridableByDefault() {
		super(Member.class);
	}

	public PropertySet<Element,ChameleonProperty> suggestedProperties(Member element) {
		return createSet(language(ObjectOrientedLanguage.class).OVERRIDABLE);
	}
	
}

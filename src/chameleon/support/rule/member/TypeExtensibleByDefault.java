package chameleon.support.rule.member;

import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.property.PropertyRule;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.member.Member;

public class TypeExtensibleByDefault extends PropertyRule<Member> {

	public TypeExtensibleByDefault() {
		super(Member.class);
	}

	@Override
	public PropertySet<Element, ChameleonProperty> suggestedProperties(Member element) {
		return createSet(language(ObjectOrientedLanguage.class).EXTENSIBLE);
	}

}

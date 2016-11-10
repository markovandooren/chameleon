package org.aikodi.chameleon.support.rule.member;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.PropertyRule;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

public class TypeExtensibleByDefault extends PropertyRule<Declaration> {

	public TypeExtensibleByDefault() {
		super(Declaration.class);
	}

	@Override
	public PropertySet<Element, ChameleonProperty> suggestedProperties(Declaration element) {
		return createSet(language(ObjectOrientedLanguage.class).EXTENSIBLE);
	}

}

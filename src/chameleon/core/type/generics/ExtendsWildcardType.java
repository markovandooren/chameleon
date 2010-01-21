package chameleon.core.type.generics;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.language.Language;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.core.type.Type;
import chameleon.core.type.TypeElement;
import chameleon.core.type.TypeReference;
import chameleon.core.type.inheritance.InheritanceRelation;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;

public class ExtendsWildcardType extends WildCardType {

	public ExtendsWildcardType(Type upperBound) {
		super(new SimpleNameSignature("? extends "+upperBound.getName()), upperBound,upperBound.language(ObjectOrientedLanguage.class).getNullType());
	}

	

}

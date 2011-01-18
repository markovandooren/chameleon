package chameleon.oo.type.generics;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.language.Language;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Parameter;
import chameleon.oo.type.ParameterBlock;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeElement;
import chameleon.oo.type.TypeReference;
import chameleon.oo.type.inheritance.AbstractInheritanceRelation;
import chameleon.util.CreationStackTrace;

public class ExtendsWildcardType extends WildCardType {

	public ExtendsWildcardType(Type upperBound) {
		super(new SimpleNameSignature("? extends "+upperBound.getName()), upperBound,upperBound.language(ObjectOrientedLanguage.class).getNullType());
	}

	public Type bound() {
		return upperBound();
	}

	@Override
	public String getFullyQualifiedName() {
		return "? extends "+upperBound().getFullyQualifiedName();
	}


}

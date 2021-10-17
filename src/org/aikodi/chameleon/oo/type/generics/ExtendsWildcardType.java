package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeReference;

public class ExtendsWildcardType extends IntervalType {

	public ExtendsWildcardType(Type upperBound) {
		super("? extends "+upperBound.name(),upperBound.language(ObjectOrientedLanguage.class).getNullType(upperBound.view().namespace()),upperBound);
	}

	public Type bound() {
		return upperBound();
	}
	
	@Override
	protected Element cloneSelf() {
	  return new ExtendsWildcardType(upperBound());
	}

	@Override
	public String getFullyQualifiedName() {
		return "? extends "+upperBound().getFullyQualifiedName();
	}

	/**
	* @{inheritDoc}
	*/
	@Override
	public boolean contains(Type other, TypeFixer trace) throws LookupException {
	  return other.subtypeOf(upperBound(),trace);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean uniSupertypeOf(Type other, TypeFixer trace) throws LookupException {
	  return other.upperBound().subtypeOf(upperBound(),trace);
	}

    public TypeReference reference() {
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		TypeReference reference = language.reference(bound());
		Element parent = reference.lexical().parent();
		reference.setUniParent(null);
		TypeReference result = language.createExtendsReference(reference);
		result.setUniParent(parent);
		return result;
    }
}

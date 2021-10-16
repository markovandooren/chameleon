package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.util.Util;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.rejuse.association.SingleAssociation;
import org.aikodi.rejuse.predicate.Predicate;

import java.util.List;
import java.util.Set;

public class NonLocalTypeReference extends ElementImpl implements TypeReference {

	private NonLocalTypeReference() {
	}

	public NonLocalTypeReference(TypeReference tref) {
	   this(tref,tref.lexical().parent());
		}

		public NonLocalTypeReference(TypeReference tref, Element lookupParent) {
		  setActualReference(tref);
			setLookupParent(lookupParent);
		}
	
	public TypeReference actualReference() {
		return _actual.getOtherEnd();
	}

	public void setActualReference(TypeReference actual) {
		set(_actual, actual);
	}

	private Single<TypeReference> _actual = new Single<TypeReference>(this, "actual");

	@Override
	protected Element cloneSelf() {
		return new NonLocalTypeReference();
	}

	@Override
	public LookupContext lexicalContext() throws LookupException {
		return lookupParent().lookupContext(this);
	}

  @Override
public LookupContext lookupContext(Element child) throws LookupException {
		return lookupParent().lookupContext(this);
  }
  
	public void setLookupParent(Element newParent) {
		_lookupParent = newParent;
	}
	
	public Element lookupParent() {
		return _lookupParent;
	}

	private Element _lookupParent;

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	@Override
   public Type getElement() throws LookupException {
		return actualReference().getElement();
	}
	
	@Override
	public String toString(Set<Element> visited) {
		return actualReference().toString(visited);
	}


	/**
	 * Replace all references to 'declarator' in 'in' with copies of 'replacement'.
	 * @param replacement
	 * @param declarator
	 * @param in
	 * @param kind
	 * @return
	 * @throws LookupException
	 */
	public static <E extends Element> E replace(TypeReference replacement, final Declaration declarator, E in, Class<E> kind) throws LookupException {
		ObjectOrientedLanguage lang = in.language(ObjectOrientedLanguage.class);
		E result = in;
		Predicate<BasicTypeReference, LookupException> predicate = object -> object.getDeclarator().sameAs(declarator);
		List<BasicTypeReference> crefs = in.lexical().descendants(BasicTypeReference.class,
				predicate);
		if(in instanceof BasicTypeReference) {
			BasicTypeReference in2 = (BasicTypeReference) in;
			if(predicate.eval(in2)) {
				crefs.add(in2);
			}
		}
		for(BasicTypeReference cref: crefs) {
			TypeReference substitute;
			Element oldParent = replacement.lexical().parent();
			if(replacement.isDerived()) {
				substitute = lang.createNonLocalTypeReference(Util.clone(replacement),oldParent);
				substitute.setOrigin(replacement);
			} else {
				substitute = lang.createNonLocalTypeReference(Util.clone(replacement),oldParent);
			}
			if(! cref.isDerived()) {
				SingleAssociation crefParentLink = cref.parentLink();
				crefParentLink.getOtherRelation().replace(crefParentLink, substitute.parentLink());
			} else {
				substitute.setUniParent(in.lexical().parent());
			}
			if(cref == in) {
				if(kind.isInstance(substitute)) {
					result = (E) substitute;
				} else {
					throw new ChameleonProgrammerException("The type reference passed to replace must be replaced as a whole, but the kind that was given is more specific than the newly created type reference.");
				}
			}
		}
		return result;
	}


	public static TypeReference replace(TypeReference replacement, final Declaration declarator, TypeReference in) throws LookupException {
		return replace(replacement, declarator,in,TypeReference.class);
	}
}

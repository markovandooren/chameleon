package chameleon.oo.type;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.reference.CrossReferenceWithName;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.util.Util;

public abstract class NonLocalTypeReference<E extends NonLocalTypeReference> extends NamespaceElementImpl<E> implements TypeReference<E> {

	public NonLocalTypeReference(TypeReference tref) {
	   this(tref,tref.parent());
		}
		
		public NonLocalTypeReference(TypeReference tref, Element lookupParent) {
		  setActualReference(tref);
			setLookupParent(lookupParent);
		}
	
	public abstract E clone();
	
	public TypeReference actualReference() {
		return _actual.getOtherEnd();
	}

	public void setActualReference(TypeReference actual) {
		setAsParent(_actual, actual);
	}

	private SingleAssociation<NonLocalTypeReference, TypeReference> _actual = new SingleAssociation<NonLocalTypeReference, TypeReference>(this);

	@Override
	public LookupStrategy lexicalLookupStrategy() throws LookupException {
		return lookupParent().lexicalLookupStrategy(this);
	}

  public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		return lookupParent().lexicalLookupStrategy(this);
  }
  
	public void setLookupParent(Element newParent) {
		_lookupParent = newParent;
	}
	
	public Element lookupParent() {
		return _lookupParent;
	}

	private Element _lookupParent;

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public Type getElement() throws LookupException {
		return actualReference().getElement();
	}

	public Type getType() throws LookupException {
		return getElement();
	}

	public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	public TypeReference intersectionDoubleDispatch(TypeReference other) {
		return language(ObjectOrientedLanguage.class).createIntersectionReference(clone(), other.clone());
	}

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference<?> other) {
		IntersectionTypeReference<?> result = other.clone();
		result.add(clone());
		return result;
	}

	public Declaration getDeclarator() throws LookupException {
		return actualReference().getDeclarator();
	}

	public List<? extends Element> children() {
		return Util.createNonNullList(actualReference());
	}
	
	@Override
	public LookupStrategy targetContext() throws LookupException {
		return getElement().targetContext();
	}


}

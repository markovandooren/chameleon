package chameleon.oo.type;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.util.association.Single;

public abstract class NonLocalTypeReference extends ElementImpl implements TypeReference {

	public NonLocalTypeReference(TypeReference tref) {
	   this(tref,tref.parent());
		}
		
		public NonLocalTypeReference(TypeReference tref, Element lookupParent) {
		  setActualReference(tref);
			setLookupParent(lookupParent);
		}
	
	public abstract NonLocalTypeReference clone();
	
	public TypeReference actualReference() {
		return _actual.getOtherEnd();
	}

	public void setActualReference(TypeReference actual) {
		set(_actual, actual);
	}

	private Single<TypeReference> _actual = new Single<TypeReference>(this);

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

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		IntersectionTypeReference result = other.clone();
		result.add(clone());
		return result;
	}

	public Declaration getDeclarator() throws LookupException {
		return actualReference().getDeclarator();
	}

	@Override
	public LookupStrategy targetContext() throws LookupException {
		return getElement().targetContext();
	}


}

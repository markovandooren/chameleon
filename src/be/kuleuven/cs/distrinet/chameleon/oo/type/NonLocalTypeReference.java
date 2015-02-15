package be.kuleuven.cs.distrinet.chameleon.oo.type;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public abstract class NonLocalTypeReference extends ElementImpl implements TypeReference {

	public NonLocalTypeReference(TypeReference tref) {
	   this(tref,tref.parent());
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

	private Single<TypeReference> _actual = new Single<TypeReference>(this);

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
   public Type getType() throws LookupException {
		return getElement();
	}

	@Override
   public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	@Override
   public TypeReference intersectionDoubleDispatch(TypeReference other) {
		return language(ObjectOrientedLanguage.class).createIntersectionReference(clone(this), clone(other));
	}

	@Override
   public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		IntersectionTypeReference result = clone(other);
		result.add(clone(this));
		return result;
	}

	@Override
   public Declaration getDeclarator() throws LookupException {
		return actualReference().getDeclarator();
	}

	@Override
	public LookupContext targetContext() throws LookupException {
		return getElement().targetContext();
	}


}

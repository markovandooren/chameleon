package org.aikodi.chameleon.oo.type;

import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.util.association.Single;

public abstract class NonLocalTypeReference extends ElementImpl implements TypeReference {

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


}

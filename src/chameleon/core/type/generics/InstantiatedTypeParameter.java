package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.MissingSignature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeIndirection;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class InstantiatedTypeParameter extends TypeParameter<InstantiatedTypeParameter> {
	
	public InstantiatedTypeParameter(SimpleNameSignature signature, ActualTypeArgument argument) {
		super(signature);
		setArgument(argument);
	}

	@Override
	public InstantiatedTypeParameter clone() {
		throw new Error();
	}
	
//	/**
//	 * A generic parameter introduces itself. During lookup, the resolve() method will
//	 * introduce an alias.
//	 */
//	public List<Member> getIntroducedMembers() {
//		List<Member> result = new ArrayList<Member>();
//		result.add(this);
//		return result;
//	}


	public List<Element> children() {
		return new ArrayList<Element>();
	}
	
	private void setArgument(ActualTypeArgument type) {
		_argument = type;
	}
	
	public ActualTypeArgument argument() {
		return _argument;
	}
	
	private ActualTypeArgument _argument;

	public Type selectionDeclaration() throws LookupException {
		return new ActualType(signature().clone(), argument().type());
	}

	public static class ActualType extends TypeIndirection {

		public ActualType(SimpleNameSignature sig, Type aliasedType) {
			super(sig,aliasedType);
		}

		@Override
		public Type clone() {
			return new ActualType(signature().clone(), aliasedType());
		}
		
		@Override
		public Type actualDeclaration() {
			return aliasedType();
		}
		
	}
//  // upper en lower naar type param verhuizen? Wat met formal parameter? Moet dat daar niet hetzelfde blijven?
//	public boolean compatibleWith(TypeParameter other) throws LookupException {
//		return  (other instanceof InstantiatedTypeParameter) && ((InstantiatedTypeParameter)other).argument().contains(argument());
//	}

	@Override
	public InstantiatedTypeParameter resolveForRoundTrip() throws LookupException {
		return this;
	}

	public TypeParameter capture(FormalTypeParameter formal) {
//		return argument().capture(formal);
		throw new Error();
	}

	@Override
	public Type lowerBound() throws LookupException {
		return argument().lowerBound();
	}

	@Override
	public Type upperBound() throws LookupException {
		return argument().upperBound();
	}
	
	@Override
	public VerificationResult verifySelf() {
		VerificationResult tmp = super.verifySelf();
		if(argument() != null) {
		  return tmp;
		} else {
			return tmp.and(new MissingSignature(this)); 
		}
	}

}

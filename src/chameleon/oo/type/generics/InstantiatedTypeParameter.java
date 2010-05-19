package chameleon.oo.type.generics;

import java.util.ArrayList;
import java.util.List;


import chameleon.core.declaration.MissingSignature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.ConstructedType;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeIndirection;
import chameleon.oo.type.TypeReference;
import chameleon.util.CreationStackTrace;
import chameleon.util.Pair;

public class InstantiatedTypeParameter<E extends InstantiatedTypeParameter<E>> extends AbstractInstantiatedTypeParameter<E> {
	
	public InstantiatedTypeParameter(SimpleNameSignature signature, ActualTypeArgument argument) {
		super(signature,argument);
	}
	
	@Override
	public E clone() {
		return (E) new InstantiatedTypeParameter(signature().clone(),argument());
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



//  // upper en lower naar type param verhuizen? Wat met formal parameter? Moet dat daar niet hetzelfde blijven?
//	public boolean compatibleWith(TypeParameter other) throws LookupException {
//		return  (other instanceof InstantiatedTypeParameter) && ((InstantiatedTypeParameter)other).argument().contains(argument());
//	}



}

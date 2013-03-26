package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;

public class InstantiatedTypeParameter extends AbstractInstantiatedTypeParameter {
	
	public InstantiatedTypeParameter(SimpleNameSignature signature, ActualTypeArgument argument) {
		super(signature,argument);
	}
	
	@Override
	public InstantiatedTypeParameter clone() {
		return new InstantiatedTypeParameter(signature().clone(),argument());
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

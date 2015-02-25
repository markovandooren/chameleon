package org.aikodi.chameleon.oo.type.generics;



public class InstantiatedTypeParameter extends AbstractInstantiatedTypeParameter {
	
	public InstantiatedTypeParameter(String name, ActualTypeArgument argument) {
		super(name,argument);
	}
	
	@Override
	protected InstantiatedTypeParameter cloneSelf() {
		return new InstantiatedTypeParameter(name(),argument());
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

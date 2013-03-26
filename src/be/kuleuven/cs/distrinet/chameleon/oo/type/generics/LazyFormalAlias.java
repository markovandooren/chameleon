/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;

public class LazyFormalAlias extends FormalParameterType {

	public LazyFormalAlias(SimpleNameSignature sig, FormalTypeParameter param) {
		super(sig,null,param);
	}
	
	public Type aliasedType() {
		try {
			return parameter().upperBound();
		} catch (LookupException e) {
			throw new Error("LookupException while looking for aliasedType of a lazy alias",e);
		}
	}
	

}

/**
 * 
 */
package chameleon.oo.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.Type;

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
/**
 * 
 */
package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;

public class LazyFormalAlias extends FormalParameterType {

	public LazyFormalAlias(String name, FormalTypeParameter param) {
		super(name,null,param);
	}
	
	@Override
   public Type aliasedType() {
		try {
			return parameter().upperBound();
		} catch (LookupException e) {
			throw new Error("LookupException while looking for aliasedType of a lazy alias",e);
		}
	}
	
	@Override
	public FormalParameterType cloneSelf() {
	  return new LazyFormalAlias(name(), parameter());
	}

}

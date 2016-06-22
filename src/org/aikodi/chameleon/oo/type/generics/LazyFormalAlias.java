/**
 * 
 */
package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;

public class LazyFormalAlias extends TypeVariable {

	public LazyFormalAlias(String name, FormalTypeParameter param) {
		super(name,null,param);
	}

	@Override
   protected Type indirectionTarget() {
		try {
			return parameter().upperBound();
		} catch (LookupException e) {
			throw new Error("LookupException while looking for aliasedType of a lazy alias",e);
		}
	}
	
	@Override
	public TypeVariable cloneSelf() {
	  return new LazyFormalAlias(name(), parameter());
	}

}

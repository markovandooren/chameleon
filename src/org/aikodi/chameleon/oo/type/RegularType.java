package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;

public class RegularType extends ClassWithBody {

	public RegularType(String name) {
		super(name);
	}
	
//	protected RegularType() {
//	}
	
	@Override
   protected RegularType cloneSelf() {
		RegularType result = new RegularType(name());
		result.parameterBlock(TypeParameter.class).disconnect();
		return result;
	}

	@Override
	public Type baseType() {
		return this;
	}

	@Override
   public boolean uniSameAs(Type other, TypeFixer trace) throws LookupException {
		return other == this;
	}

}

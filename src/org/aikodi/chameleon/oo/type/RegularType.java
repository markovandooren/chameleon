package org.aikodi.chameleon.oo.type;

import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.util.Pair;

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
   public boolean uniSameAs(Type other, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		return other == this;
	}

}

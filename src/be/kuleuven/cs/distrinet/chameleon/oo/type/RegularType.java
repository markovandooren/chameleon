package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.generics.TypeParameter;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;

public class RegularType extends ClassWithBody {

	public RegularType(SimpleNameSignature sig) {
		super(sig);
	}
	
	public RegularType(String name) {
		this(new SimpleNameSignature(name));
	}
	
	protected RegularType() {
		
	}
	
	protected RegularType cloneSelf() {
		return new RegularType((SimpleNameSignature)null);
	}

	@Override
	public Type baseType() {
		return this;
	}

	public boolean uniSameAs(Type other, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		return other == this;
	}

}

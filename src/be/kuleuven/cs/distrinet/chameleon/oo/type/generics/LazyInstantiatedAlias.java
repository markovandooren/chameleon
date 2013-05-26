package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeIndirection;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;

public class LazyInstantiatedAlias extends TypeIndirection {

	public LazyInstantiatedAlias(SimpleNameSignature sig, TypeParameter param) {
		super(sig,null);
		_param = param;
	}
	
	public Type aliasedType() {
		try {
			return parameter().upperBound();
		} catch (LookupException e) {
			throw new Error("LookupException while looking for aliasedType of a lazy alias",e);
		}
	}
	
	public TypeParameter parameter() {
		return _param;
	}
	
	private final TypeParameter _param;

	@Override
	public LazyInstantiatedAlias cloneSelf() {
		return new LazyInstantiatedAlias(null, _param);
	}

	public boolean uniSameAs(Type other, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		return other == this;
	}

	public Declaration declarator() {
		return parameter();
	}
	
}

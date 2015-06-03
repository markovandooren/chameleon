package org.aikodi.chameleon.oo.type.generics;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeIndirection;
import org.aikodi.chameleon.util.Pair;

public class LazyInstantiatedAlias extends TypeIndirection {

	public LazyInstantiatedAlias(String name, TypeParameter param) {
		super(name,null);
		_param = param;
	}
	
	@Override
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
		return new LazyInstantiatedAlias(name(), _param);
	}

	@Override
   public boolean uniSameAs(Type other, TypeFixer trace) throws LookupException {
		return other == this;
	}
	
	@Override
	public boolean uniSupertypeOf(Type other, TypeFixer trace) throws LookupException {
	  TypeParameter secondParam = ((LazyInstantiatedAlias)other).parameter();
	  if(trace.contains(this, secondParam)) {
	  	return true;
	  }
	  trace.add(this, secondParam);
	  return false;
	}
	
	@Override
	public boolean uniSubtypeOf(Type other, TypeFixer trace) throws LookupException {
      TypeParameter firstParam = parameter();
      if(trace.contains(other, firstParam)) {
      	return true;
      }
      trace.add(other, firstParam);
      // FIXME or return super?
      return false;
	}

	@Override
   public Declaration declarator() {
		return parameter();
	}
	
}

package org.aikodi.chameleon.oo.type;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.util.Pair;

public class TypeAlias extends TypeIndirection {

	
	public TypeAlias(String name, Type aliasedType) {
		super(name, aliasedType);
		setOrigin(aliasedType);
	}

	@Override
	public boolean uniSameAs(Element type) throws LookupException {
		return super.uniSameAs(type) || 
		       type.sameAs(indirectionTarget()) || 
		       ((type instanceof TypeIndirection) && (((TypeIndirection)type).indirectionTarget().sameAs(indirectionTarget())));
	}
	
	@Override
	public int hashCode() {
		return indirectionTarget().hashCode();
	}
	
	/**
	 * OVERRIDE IN SUBCLASSES !!!!
	 */
	@Override
   public TypeAlias cloneSelf() {
		return new TypeAlias(name(), indirectionTarget());
	}

	@Override
   public boolean uniSameAs(Type other, TypeFixer trace) throws LookupException {
		return super.uniSameAs(other) || 
    other.sameAs(indirectionTarget(),trace) || 
    ((other instanceof TypeIndirection) && (((TypeIndirection)other).indirectionTarget().sameAs(indirectionTarget(),trace)));
	}

	@Override
   public Declaration declarator() {
		return indirectionTarget().declarator();
	}


}

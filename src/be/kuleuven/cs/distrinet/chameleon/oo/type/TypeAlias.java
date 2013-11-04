package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.generics.TypeParameter;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;

public class TypeAlias extends TypeIndirection {

	
	public TypeAlias(String name, Type aliasedType) {
		super(name, aliasedType);
		setOrigin(aliasedType);
	}

	@Override
	public boolean uniSameAs(Element type) throws LookupException {
		return super.uniSameAs(type) || 
		       type.sameAs(aliasedType()) || 
		       ((type instanceof TypeIndirection) && (((TypeIndirection)type).aliasedType().sameAs(aliasedType())));
	}
	
	@Override
	public int hashCode() {
		return aliasedType().hashCode();
	}
	
	/**
	 * OVERRIDE IN SUBCLASSES !!!!
	 */
	public TypeAlias cloneSelf() {
		return new TypeAlias(name(), aliasedType());
	}

	public boolean uniSameAs(Type other, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		return super.uniSameAs(other) || 
    other.sameAs(aliasedType(),trace) || 
    ((other instanceof TypeIndirection) && (((TypeIndirection)other).aliasedType().sameAs(aliasedType(),trace)));
	}

	public Declaration declarator() {
		return aliasedType().declarator();
	}


}

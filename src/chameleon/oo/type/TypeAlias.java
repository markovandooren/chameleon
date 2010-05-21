package chameleon.oo.type;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.generics.TypeParameter;
import chameleon.util.Pair;

public class TypeAlias extends TypeIndirection {

	
	public TypeAlias(SimpleNameSignature sig, Type aliasedType) {
		super(sig, aliasedType);
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
	public TypeAlias clone() {
		return new TypeAlias(signature().clone(), aliasedType());
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

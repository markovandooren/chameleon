package org.aikodi.chameleon.oo.type.generics;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeIndirection;
import org.aikodi.chameleon.util.Pair;

import com.google.common.collect.ImmutableList;

/**
 * This class represents types created as a result of looking up (resolving) a generic parameter, which itself is
 * not a type.
 * 
 * @author Marko van Dooren
 */
public class FormalParameterType extends TypeIndirection {

	public FormalParameterType(String name, Type aliasedType, FormalTypeParameter param) {
		super(name, aliasedType);
		if(param == null) {
			throw new ChameleonProgrammerException("The formal type parameter corresponding to a constructed type cannot be null.");
		}
		_param = param;
	}
	
	@Override
	public boolean uniSameAs(Element type) throws LookupException {
		return type == this || 
		       ((type instanceof FormalParameterType) && (((FormalParameterType)type).parameter().sameAs(parameter())));
	}
	
	@Override
	public int hashCode() {
		return parameter().hashCode();
	}
	
	
	@Override
	public List<Type> getDirectSuperTypes() throws LookupException {
		return ImmutableList.of(aliasedType());
	}


	@Override
	public String getFullyQualifiedName() {
		return name();
	}

	public FormalTypeParameter parameter() {
		return _param;
	}
	
	private final FormalTypeParameter _param;

	@Override
	public FormalParameterType cloneSelf() {
		return new FormalParameterType(name(), aliasedType(), parameter());
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	@Override
	public Type baseType() {
		return this;
	}


	@Override
   public boolean uniSameAs(Type type, TypeFixer trace) throws LookupException {
		return uniSameAs(type);
	}


	@Override
   public Declaration declarator() {
		return parameter();
	}


}

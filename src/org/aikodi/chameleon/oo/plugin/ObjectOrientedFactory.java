package org.aikodi.chameleon.oo.plugin;

import java.util.Arrays;
import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.factory.Factory;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.method.MethodHeader;
import org.aikodi.chameleon.oo.method.SimpleNameMethodHeader;
import org.aikodi.chameleon.oo.type.IntersectionType;
import org.aikodi.chameleon.oo.type.RegularType;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.type.UnionType;
import org.aikodi.chameleon.oo.type.generics.ConstrainedType;
import org.aikodi.chameleon.oo.type.generics.TypeVariable;
import org.aikodi.chameleon.oo.type.generics.FormalTypeParameter;
import org.aikodi.chameleon.oo.type.generics.InstantiatedParameterType;
import org.aikodi.chameleon.oo.type.generics.LazyFormalAlias;
import org.aikodi.chameleon.oo.type.generics.LazyInstantiatedAlias;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.support.member.simplename.method.NormalMethod;
import org.aikodi.chameleon.support.modifier.Constructor;

/**
 * A factory class for object oriented languages.
 * 
 * @author Marko van Dooren
 */
public abstract class ObjectOrientedFactory extends Factory {

	/**
	 * Create a new basic lexical class with the given signature.
	 */
	/*@
   @ public behavior
   @
   @ pre signature != null;
   @
   @ post \result != null;
   @ post \result.signature() == signature;
   @*/
	public Type createRegularType(String name) {
		return new RegularType(name);
	}

	/**
	 * @param lowerBound The lower bound of the constrained type. The lower bound cannot be null.
	 * @param upperBound The upper bound of the constrained type. The lower bound cannot be null.
	 * @param parentIfNew The parent of the result in case a new type is constructed.
	 * 
	 * @return A constrained type with the given lower and upper bounds. If the lower bound and 
	 * the upper bound are the same, that type is returned. Otherwise, a new constrained type is
	 * constructed with the given bounds, and its unidirectional parent is set to the given parent.
	 */
	public Type createConstrainedType(Type lowerBound, Type upperBound, Element parentIfNew) throws LookupException {
		if(lowerBound.sameAs(upperBound)) {
			return lowerBound;
		} else {
			ConstrainedType result = reallyCreateConstrainedType(lowerBound, upperBound);
			result.setUniParent(parentIfNew);
			return result;
		}

	}

	/**
	 * @param lowerBound The lower bound of the constrained type. The lower bound cannot be null.
	 * @param upperBound The upper bound of the constrained type. The lower bound cannot be null.
	 * 
	 * @return A constrained type with the given lower and upper bounds.
	 */
	public ConstrainedType reallyCreateConstrainedType(Type lowerBound, Type upperBound) {
		return new ConstrainedType(lowerBound, upperBound);
	}

	public Method createNormalMethod(String name, TypeReference returnType) {
		return createNormalMethod(new SimpleNameMethodHeader(name, returnType));
	}

	public NormalMethod createNormalMethod(MethodHeader header) {
		return new NormalMethod(header);
	}

	public abstract InheritanceRelation createDefaultInheritanceRelation(Type type);

	public Method transformToConstructor(Method m) {
		m.addModifier(new Constructor());
		return m;
	}

	//TODO Check if lower bound is needed
	public Type createTypeVariable(String name, Type upperBound, FormalTypeParameter formalTypeParameter) {
		return new TypeVariable(name,upperBound,formalTypeParameter);
	}

	//TODO Check if lower bound is needed
	public Type createLazyTypeVariable(String name, FormalTypeParameter formalTypeParameter) {
		return new LazyFormalAlias(name, formalTypeParameter);
	}

	public Type createInstantiatedTypeVariable(String name, Type aliasedType, TypeParameter capturedTypeParameter) {
		return new InstantiatedParameterType(name, aliasedType,capturedTypeParameter);
	}

	public Type createLazyInstantiatedTypeVariable(String name, TypeParameter capturedTypeParameter) {
		return new LazyInstantiatedAlias(name, capturedTypeParameter);
	}

	public Type createIntersectionType(Type first, Type second) throws LookupException {
		if(first.subtypeOf(second)) {
			return first;
		} else if(second.subtypeOf(first)) {
			return second;
		}
		Type[] list = new Type[]{first,second};
		return createIntersectionType(Arrays.asList(list));
	}  

	public Type createIntersectionType(List<Type> types) throws LookupException {
		int size = types.size();
		if(size == 0) {
			throw new ChameleonProgrammerException();
		} 
		for(int i=0; i< size; i++) {
			int j = 0;
			while(j < size) {
				if((i != j) && (types.get(i).subtypeOf(types.get(j)))) {
					types.remove(j);
					size--;
					if(j<i) {
						i--;
					}
				} else {
					j++;
				}
			}
		}
		if(types.size() == 1) {
			return types.get(0);
		} else {
			Namespace def = types.get(0).view().namespace();
			IntersectionType result = doCreateIntersectionType(types);
			result.setUniParent(def);
			return result;
		}
	}

	public Type createUnionType(Type first, Type second) {
		return createUnionType(Arrays.asList(new Type[]{first,second}));
	}  

	public Type createUnionType(List<Type> types) {
		if(types.size() == 1) {
			return types.get(0);
		} else {
			Namespace def = types.get(0).view().namespace();
			UnionType result = doCreateUnionType(types);
			result.setUniParent(def);
			return result;
		}
	}

	protected UnionType doCreateUnionType(List<Type> types) {
		return new UnionType(types);
	}

	protected IntersectionType doCreateIntersectionType(List<Type> types) {
		return new IntersectionType(types);
	}
}

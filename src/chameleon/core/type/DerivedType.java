package chameleon.core.type;

import java.util.Iterator;
import java.util.List;

import chameleon.core.type.generics.ActualTypeArgument;
import chameleon.core.type.generics.InstantiatedTypeParameter;
import chameleon.core.type.generics.TypeParameter;


/**
 * A derived type is created by filling in the type parameters of a parameterized
 * type.
 * 
 * @author Marko van Dooren
 */
public class DerivedType extends RegularType {

	/**
	 * Create a derived type by filling in the type parameters with the given list of
	 * actual type arguments.
	 * 
	 * @param baseType
	 * @param typeParameters
	 */
	public DerivedType(Type baseType, List<ActualTypeArgument> typeParameters) {
		this(baseType);
		// substitute parameters
		List<TypeParameter> myParameters = parameters();
		Iterator<TypeParameter> parametersIterator = myParameters.iterator();
		Iterator<ActualTypeArgument> argumentsIterator = typeParameters.iterator();
		while (parametersIterator.hasNext()) {
			TypeParameter parameter = parametersIterator.next();
			ActualTypeArgument argument = argumentsIterator.next();
			// The next call does not change the parent of 'argument'. It is stored in InstantiatedTypeParameter
			// using a regular reference.
			InstantiatedTypeParameter instantiated = new InstantiatedTypeParameter(parameter.signature().clone(), argument);
			replaceParameter(parameter, instantiated);
		}

	}
	
	public DerivedType(List<TypeParameter> typeParameters, Type baseType) {
		this(baseType);
		substituteParameters(typeParameters);
	}

	public void substituteParameters(List<TypeParameter> typeParameters) {
		Iterator<TypeParameter> parametersIterator = parameters().iterator();
		Iterator<TypeParameter> argumentsIterator = typeParameters.iterator();
		while (parametersIterator.hasNext()) {
			TypeParameter parameter = parametersIterator.next();
			TypeParameter argument = argumentsIterator.next();
			// The next call does not change the parent of 'argument'. It is stored in InstantiatedTypeParameter
			// using a regular reference.
			replaceParameter(parameter, argument);
		}
	}
	
	/**
	 * Create a new derived type for the given base type.
	 * The contents of the type is copied into this type.
	 * @param baseType
	 */
	private DerivedType(Type baseType) {
		super(baseType.signature().clone());
		// Does not take nested members of any kind into account.
		copyContents(baseType, true);
		_baseType = baseType;
		setOrigin(baseType);
	}

	private Type _baseType;
	
	@Override
	public Type baseType() {
		return _baseType;
	}

	@Override
	public DerivedType clone() {
		return new DerivedType(baseType());
	}
}

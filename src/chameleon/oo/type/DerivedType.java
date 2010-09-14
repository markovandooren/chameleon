package chameleon.oo.type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.Modifier;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.type.generics.ActualTypeArgument;
import chameleon.oo.type.generics.InstantiatedTypeParameter;
import chameleon.oo.type.generics.TypeParameter;
import chameleon.util.Pair;


/**
 * A derived type is created by filling in the type parameters of a parameterized
 * type.
 * 
 * @author Marko van Dooren
 */
public class DerivedType extends TypeWithBody {
	
	public DerivedType(List<ParameterSubstitution> parameters, Type baseType) {
		this(baseType);
		substituteParameters(parameters);
	}

	public <P extends Parameter> DerivedType(Class<P> kind, List<P> parameters, Type baseType) {
		this(baseType);
		substituteParameters(kind, parameters);
	}
	
	public DerivedType(ParameterSubstitution substitution, Type baseType) {
		this(baseType);
		substituteParameters(substitution);
	}

	/**
	 * Create a new derived type for the given base type.
	 * The contents of the type is copied into this type.
	 * @param baseType
	 */
	private DerivedType(Type baseType) {
		super(baseType.signature().clone());
		_baseType = baseType;
		setOrigin(baseType);
		copyInheritanceRelations(baseType, true);
		copyParameterBlocks(baseType, true);
//		copyModifiers(baseType, true); // Let's try to do this lazily.
		setBody(new LazyClassBody(((TypeWithBody)baseType).body()));

//		copyContents(baseType, true);
//		_baseType = baseType;
//		setOrigin(baseType);
	}
	
//	private static DerivedType create(Type baseType, List<ActualTypeArgument> typeArguments) throws LookupException {
//		Map<List<String>, DerivedType> map = _cache.get(baseType);
//		if(map == null) {
//			map = new HashMap<List<String>, DerivedType>();
//			_cache.put(baseType, map);
//		} 
//		List<String> typeList = new ArrayList<String>();
//		for(ActualTypeArgument arg: typeArguments) {
//			String fullyQualifiedName = arg.type().getFullyQualifiedName();
//			if(fullyQualifiedName.indexOf(".") < 0) {
//				System.out.println("######### "+fullyQualifiedName);
//			}
//			typeList.add(fullyQualifiedName);
//		}
//		DerivedType result = map.get(typeList);
//		if(result == null) {
//			result = new DerivedType(baseType, typeArguments);
//			map.put(typeList, result);
//		} else {
//		}
//		return result;
//	}
	
//	private static int COUNT;
	
//	private static Map<Type, Map<List<String>, DerivedType>> _cache = new HashMap<Type, Map<List<String>, DerivedType>>();
	
	/**
	 * Create a derived type by filling in the type parameters with the given list of
	 * actual type arguments.
	 * 
	 * Note that the arguments are reversed compared to the constructor which takes a list
	 * of type parameters because otherwise, both constructors would have the same erasure.
	 * 
	 * @param baseType
	 * @param typeArguments
	 */
	public DerivedType(Type baseType, List<ActualTypeArgument> typeArguments) {
		this(baseType);
		// substitute parameters
		List<TypeParameter> myParameters = parameters(TypeParameter.class);
		if(myParameters.size() != typeArguments.size()) {
			throw new ChameleonProgrammerException();
		}
		Iterator<TypeParameter> parametersIterator = myParameters.iterator();
		Iterator<ActualTypeArgument> argumentsIterator = typeArguments.iterator();
		while (parametersIterator.hasNext()) {
			TypeParameter parameter = parametersIterator.next();
			ActualTypeArgument argument = argumentsIterator.next();
			// The next call does not change the parent of 'argument'. It is stored in InstantiatedTypeParameter
			// using a regular reference.
			InstantiatedTypeParameter instantiated = new InstantiatedTypeParameter(parameter.signature().clone(), argument);
			replaceParameter(TypeParameter.class,parameter, instantiated);
		}
	}
	
	
	/**
	 * The modifiers are loaded lazily from the base type.
	 */
	@Override
	public List<Modifier> modifiers() {
		List<Modifier> result = super.modifiers();
		if(result.isEmpty()) {
			copyModifiers(baseType(), true);
			result = super.modifiers();
		}
		return result;
	}

	@Override
	public boolean uniSameAs(Element otherType) throws LookupException {
		boolean result = false;
		if(otherType instanceof DerivedType) {
			DerivedType type = (DerivedType) otherType;
			result = type.baseType().sameAs(baseType());
			Iterator<TypeParameter> myParams = parameters(TypeParameter.class).iterator();
			Iterator<TypeParameter> otherParams = type.parameters(TypeParameter.class).iterator();
			while(myParams.hasNext() && result) {
				TypeParameter mine = myParams.next();
				TypeParameter otherParam = otherParams.next();
				result = mine.sameValueAs(otherParam, new ArrayList());
			}
		}
		return result;
	}
	
	public boolean uniSameAs(Type otherType, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		boolean result = false;
		if(otherType instanceof DerivedType) {
			DerivedType type = (DerivedType) otherType;
			result = type.baseType().sameAs(baseType());
			Iterator<TypeParameter> myParams = parameters(TypeParameter.class).iterator();
			Iterator<TypeParameter> otherParams = type.parameters(TypeParameter.class).iterator();
			while(myParams.hasNext() && result) {
				TypeParameter mine = myParams.next();
				TypeParameter otherParam = otherParams.next();
				result = mine.sameValueAs(otherParam,trace);
			}
		}
		return result;
	}

	
	@Override
	public int hashCode() {
		int result = baseType().hashCode();
//		for(TypeParameter parameter: parameters()) {
//			result += parameter.hashCode();
//		}
		return result;
	}
	
	private Type _baseType;
	
	@Override
	public Type baseType() {
		return _baseType.baseType();
	}

	@Override
	public DerivedType clone() {
		List<ParameterSubstitution> args = new ArrayList<ParameterSubstitution>();
		for(ParameterBlock<?,?> block: parameterBlocks()) {
			List<Parameter> list = new ArrayList<Parameter>();
			for(Parameter par: block.parameters()) {
				list.add(par.clone());
			}
			args.add(new ParameterSubstitution(block.parameterType(), list)); 
		}
		return new DerivedType(args,baseType());
	}

}

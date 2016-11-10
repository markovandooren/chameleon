package org.aikodi.chameleon.oo.type;

import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.forAll;

import java.util.Iterator;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.type.generics.InstantiatedTypeParameter;
import org.aikodi.chameleon.oo.type.generics.TypeArgument;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.util.Lists;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;


/**
 * A derived type is created by filling in the type parameters of a parameterized
 * type.
 * 
 * @author Marko van Dooren
 */
public class TypeInstantiation extends ClassWithBody {
	
	public TypeInstantiation(List<ParameterSubstitution<?>> parameters, Type baseType) {
		this(baseType);
		substituteParameters(parameters);
	}

	public <P extends Parameter> TypeInstantiation(Class<P> kind, List<P> parameters, Type baseType) {
		this(baseType);
		substituteParameters(kind, parameters);
	}
	
	public TypeInstantiation(ParameterSubstitution substitution, Type baseType) {
		this(baseType);
		substituteParameters(substitution);
	}

	/**
	 * Create a new derived type for the given base type.
	 * The contents of the type is copied into this type.
	 * @param baseType
	 */
	private TypeInstantiation(Type baseType) {
		super(baseType.name());
		_baseType = baseType;
		setOrigin(baseType);
		copyInheritanceRelations(baseType, true);
		copyParameterBlocks(baseType, true);
		setBody(new LazyClassBody(((ClassWithBody)baseType).body()));
		copyImplicitMembers(baseType);
	}

  @Override
  public boolean hasInheritanceRelation(InheritanceRelation relation) throws LookupException {
  	return super.hasInheritanceRelation(relation) || relation.hasMetadata(IMPLICIT_CHILD);
  }
  
  public final static String IMPLICIT_CHILD = "IMPLICIT CHILD";

	/**
	 * Create a derived type by filling in the type parameters with the given list of
	 * actual type arguments.
	 * 
	 * Note that the arguments are reversed compared to the constructor which takes a list
	 * of type parameters because otherwise, both constructors would have the same erasure.
	 * 
	 * @param baseType
	 * @param typeArguments
	 * @throws LookupException 
	 */
	public TypeInstantiation(Type baseType, List<TypeArgument> typeArguments) throws LookupException {
		this(baseType);
		// substitute parameters
		List<TypeParameter> myParameters = parameters(TypeParameter.class);
		if(myParameters.size() != typeArguments.size()) {
			throw new LookupException("The number of actual type arguments ("+typeArguments.size()+") does not match the number of formal type parameters ("+myParameters.size()+").");
		}
		Iterator<TypeParameter> parametersIterator = myParameters.iterator();
		Iterator<TypeArgument> argumentsIterator = typeArguments.iterator();
		while (parametersIterator.hasNext()) {
			TypeParameter parameter = parametersIterator.next();
			TypeArgument argument = argumentsIterator.next();
			// The next call does not change the parent of 'argument'. It is stored in InstantiatedTypeParameter
			// using a regular reference.
			InstantiatedTypeParameter instantiated = new InstantiatedTypeParameter(parameter.name(), argument);
			replaceParameter(TypeParameter.class,parameter, instantiated);
		}
	}
	
	private void copyImplicitMembers(Type original) {
		Builder<Declaration> builder = ImmutableList.<Declaration>builder();
		List<Declaration> implicits = original.implicitMembers();
		for(Declaration m: implicits) {
			Declaration clone = clone(m);
			clone.setUniParent(body());
			builder.add(clone);
		}
		_implicitMembers = builder.build();
	}

	private ImmutableList<Declaration> _implicitMembers;
	
	@Override
	public List<Declaration> implicitMembers() {
		return _implicitMembers;
	}
	
	@Override
	public <D extends Declaration> List<? extends SelectionResult> implicitMembers(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(_implicitMembers);
	}

	/**
	 * The modifiers are loaded lazily from the base type.
	 */
	@Override
	public List<Modifier> modifiers() {
		if(!hasModifiers()) {
			copyModifiers(baseType(), true);
		}
		return super.modifiers();
	}

	@Override
	public boolean uniSameAs(Element otherType) throws LookupException {
		boolean result = false;
		if(otherType instanceof TypeInstantiation) {
			TypeInstantiation type = (TypeInstantiation) otherType;
			result = type.baseType().sameAs(baseType()) && 
					     forAll(parameters(TypeParameter.class), type.parameters(TypeParameter.class),
					       (mine,otherParam) -> mine.contains(otherParam, new TypeFixer()) && otherParam.contains(mine, new TypeFixer()));
		}
		return result;
	}
	
	@Override
   public boolean uniSameAs(Type otherType, TypeFixer trace) throws LookupException {
		boolean result = false;
		if(otherType instanceof TypeInstantiation) {
			TypeInstantiation type = (TypeInstantiation) otherType;
			result = type.baseType().sameAs(baseType()) &&
					forAll(parameters(TypeParameter.class),type.parameters(TypeParameter.class), 
							(mine,otherParam) -> mine.contains(otherParam,trace.clone()) && otherParam.contains(mine, trace.clone()));
		}
		return result;
	}

	
	@Override
	public int hashCode() {
		return baseType().hashCode();
	}
	
	private Type _baseType;
	
	@Override
	public Type baseType() {
		return _baseType.baseType();
	}

	/**
	 * Here we override clone because we need to perform parameter substitution.
	 */
	@Override
	public TypeInstantiation clone() {
		// I might use the standard mechanism here later, but I first want to make
		// the switch for the regular elements before I touch generics code. 
		return new TypeInstantiation(clonedParameters(),baseType());
	}

	@Override
   protected Element cloneSelf() {
		throw new ChameleonProgrammerException();
	}
	
	protected List<ParameterSubstitution<?>> clonedParameters() {
		List<ParameterSubstitution<?>> args = Lists.create();
		for(ParameterBlock<?> block: parameterBlocks()) {
			List<Parameter> list = Lists.create();
			for(Parameter par: block.parameters()) {
				list.add(clone(par));
			}
			args.add(new ParameterSubstitution(block.parameterType(), list)); 
		}
		return args;
	}

}

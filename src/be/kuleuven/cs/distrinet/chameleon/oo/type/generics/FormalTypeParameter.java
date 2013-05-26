package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.IntersectionTypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;

/**
 * This class represents generic parameters as used in Java and C#.
 * 
 * @author Marko van Dooren
 */
public class FormalTypeParameter extends TypeParameter {

	public FormalTypeParameter(SimpleNameSignature signature) {
		super(signature);
	}
	
	public FormalTypeParameter(String name) {
		this(new SimpleNameSignature(name));
	}
	
  
	@Override
	protected FormalTypeParameter cloneSelf() {
		return new FormalTypeParameter((SimpleNameSignature)null);
	}
	
	/**
	 * Resolving a generic parameter results in a constructed type whose bound
	 * is the upper bound of this generic parameter as defined by the upperBound method.
	 */
	public Type selectionDeclaration() throws LookupException {
//		String x = nearestAncestor(Type.class).getFullyQualifiedName()+"."+signature();
//		if(x.equals("org.rejuse.property.Property.E")) {
//			System.out.println("Selection declaration of " + x);
//		}
		Type constructedType = createSelectionType();
		constructedType.setUniParent(parent());
		return constructedType;
	}


	protected Type createSelectionType() throws LookupException {
		return new FormalParameterType(clone(signature()),upperBound(),this);
	}
	
	public Type resolveForRoundTrip() throws LookupException {
  	Type result = createLazyAlias();
  	result.setUniParent(parent());
  	return result;
	}


	protected Type createLazyAlias() {
		return new LazyFormalAlias(clone(signature()), this);
	}
	
	private Multi<TypeConstraint> _typeConstraints = new Multi<TypeConstraint>(this);
	
	public List<TypeConstraint> constraints() {
		return _typeConstraints.getOtherEnds();
	}
	
	public void addConstraint(TypeConstraint constraint) {
		add(_typeConstraints,constraint);
	}
	
	public TypeReference upperBoundReference() {
		List<TypeConstraint> constraints = constraints();
		int size = constraints.size();
		TypeReference result;
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		if(size == 0) {
		  result = language.createTypeReferenceInNamespace(language.getDefaultSuperClassFQN(), view().namespace());
		} else if(size == 1) {
			result = constraints.get(0).upperBoundReference();
		} else {
			result = language.createIntersectionReference(constraints.get(0).upperBoundReference(), constraints.get(1).upperBoundReference());
			for(int i=2; i<size;i++) {
				((IntersectionTypeReference)result).add(constraints.get(i).upperBoundReference());
			}
			result.setUniParent(this);
		}
		return result;
	}

	public Type upperBound() throws LookupException {
		List<TypeConstraint> constraints = constraints();
		Type result;
		int size = constraints.size();
		if(size == 0) {
			result = language(ObjectOrientedLanguage.class).getDefaultSuperClass(view().namespace());
		} else {
			result = constraints.get(0).upperBound();
			for(int i = 1; i < size; i++) {
				result = result.intersection(constraints.get(i).upperBound());
			}
		}
		return result;
	}

	public FormalTypeParameter alias(SimpleNameSignature signature) {
		throw new ChameleonProgrammerException();
	}


//	@Override
//	public boolean compatibleWith(TypeParameter other) throws LookupException {
//		return equals(other);
//	}


	@Override
	public Type lowerBound() throws LookupException {
		List<TypeConstraint> constraints = constraints();
		Type result;
		int size = constraints.size();
		if(size == 0) {
			result = language(ObjectOrientedLanguage.class).getNullType(view().namespace());
		} else {
			result = constraints.get(0).lowerBound();
			for(int i = 1; i < size; i++) {
				result = result.union(constraints.get(i).lowerBound());
			}
		}
		return result;
	}


	@Override
	public boolean uniSameAs(Element other) throws LookupException {
		if(origin() == this) {
			if(other == other.origin()) {
			  return this == other;
			} else {
				return uniSameAs(other.origin());
			}
		} else {
			return origin().sameAs(other);
		}
//		boolean result = false;
//		if(other instanceof FormalTypeParameter) {
//			result = signature().sameAs(((FormalTypeParameter) other).signature());
//			if(result) {
//				Element parent = nearestAncestor(TypeParameterBlock.class).parent();
//				Element otherParent = other.nearestAncestor(TypeParameterBlock.class).parent();
//				result = parent.sameAs(otherParent);
//			}
//		}
//		return result;
	}

  @Override
  public int hashCode() {
		if(origin() == this) {
			return super.hashCode();
		} else {
			return origin().hashCode();
		}

  }


//	@Override
//	public boolean sameValueAs(TypeParameter otherParam) throws LookupException {
//		return sameAs(otherParam);
//	}


	@Override
	public boolean sameValueAs(TypeParameter otherParam, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		return sameAs(otherParam);
	}
}

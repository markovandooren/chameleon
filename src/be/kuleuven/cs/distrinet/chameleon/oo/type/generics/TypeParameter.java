package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.MissingSignature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.scope.LexicalScope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Parameter;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;

/**
 * A class representing type parameters. These can be formal type parameters or instantiated type parameters.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 */
public abstract class TypeParameter extends Parameter {
	
	public TypeParameter(SimpleNameSignature signature) {
		super(signature);
	}
	
	public abstract TypeParameter clone();

	/**
	 * Check whether this type parameter is equal to the other type parameter. 
	 * 
	 * Equality for type parameters means that for each assignment of actual types, both parameters
	 * must refer to the same type. 
	 */
  @Override
	public abstract boolean uniSameAs(Element other) throws LookupException;

  public Type actualDeclaration() throws LookupException {
  	throw new ChameleonProgrammerException();
  }

	public abstract Declaration resolveForRoundTrip() throws LookupException;

	public TypeParameter cloneForStub() throws LookupException {
		return (TypeParameter) clone();
	}
	
	public boolean compatibleWith(TypeParameter other,List<Pair<Type, TypeParameter>> trace) throws LookupException {
		List<Pair<Type, TypeParameter>> slowTrace = new ArrayList<Pair<Type, TypeParameter>>(trace);
		boolean result = sameAs(other);
		if(! result) {
		 result = upperBound().upperBoundNotHigherThan(other.upperBound(),slowTrace);
		 if(result) {
			 result = other.lowerBound().upperBoundNotHigherThan(lowerBound(),slowTrace);
		 }
		}
		return result;
	}

//	public boolean canBeAssigned(ActualTypeArgument typeArgument) throws LookupException {
//		return lowerBound().subTypeOf(typeArgument.lowerBound()) && typeArgument.upperBound().subTypeOf(upperBound());
//	}
	
	public abstract Type upperBound() throws LookupException;
	
	public abstract TypeReference upperBoundReference() throws LookupException;
	
	public abstract Type lowerBound() throws LookupException;

	public Scope scope() throws ModelException {
		return new LexicalScope(nearestAncestor(Type.class));
	}

	@Override
	public VerificationResult verifySelf() {
		if(signature() != null) {
		  return Valid.create();
		} else {
			return new MissingSignature(this); 
		}
	}

	public abstract boolean sameValueAs(TypeParameter otherParam, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException;

  public Declaration declarator() {
  	return this;
  }

	@Override
	public boolean complete() {
		return true;
	}
	
	public String toString() {
		return signature().name();
	}

  public LocalLookupContext<?> targetContext() throws LookupException {
  	return upperBound().targetContext();
  }

}

package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.MissingSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.scope.LexicalScope;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Parameter;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeReference;

import java.util.Set;

/**
 * A class representing type parameters. These can be formal type parameters or instantiated type parameters.
 * 
 * @author Marko van Dooren
 */
public abstract class TypeParameter extends Parameter implements ElementWithTypeBounds {

  public TypeParameter(String name) {
    super(name);
  }

  /**
   * Check whether this type parameter is equal to the other type parameter. 
   * 
   * Equality for type parameters means that for each assignment of actual types, both parameters
   * must refer to the same type. 
   */
  @Override
  public abstract boolean uniSameAs(Element other) throws LookupException;

  @Override
  public Type actualDeclaration() throws LookupException {
    throw new ChameleonProgrammerException();
  }

  public abstract Declaration resolveForRoundTrip() throws LookupException;

  /**
   * Check whether the set of valid types for this type parameter contains
   * the set of valid types for the given type parameter.
   * 
   * @param other The type parameter for which must be checked if its valid types
   *              are a subset of the valid types of this type parameter. The given
   *              type parameter cannot be null.
   * @param trace A trace that tracks which types have already been processed. This trace
   *              is used to compute the fixed point.
   * @return True if the set of valid types for the given type parameter is a subset of
   *              the set of valid types for this type parameter. False otherwise.
   * @throws LookupException
   */
  public final boolean contains(TypeParameter other,TypeFixer trace) throws LookupException {
    return selectionDeclaration().contains(other.selectionDeclaration(), trace);
  }

  /**
   * @return a type reference that represents the upper bound for this type parameter.
   * @throws LookupException
   */
  public abstract TypeReference upperBoundReference() throws LookupException;

  @Override
  public Scope scope() throws ModelException {
    return new LexicalScope(lexical().nearestAncestor(Type.class));
  }

  @Override
  public Verification verifySelf() {
    if(name() != null) {
      return Valid.create();
    } else {
      return new MissingSignature(this); 
    }
  }

  /**
   * @return For debugging purposes, the default implementation of this method returns 
   *  the name of the type parameter. That is not a contract, however.
   */
  @Override
  public String toString() {
    return name();
  }

  /**
   * @return The target context of the {@link #upperBound()}. 
   */
  @Override
  public LocalLookupContext<?> targetContext() throws LookupException {
    return upperBound().targetContext();
  }

  @Override
  public abstract Type selectionDeclaration() throws LookupException;

  public abstract String toString(Set<Element> visited);

}


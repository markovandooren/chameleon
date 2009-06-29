/*
 * Copyright 2000-2004 the Jnome development team.
 *
 * @author Marko van Dooren
 * @author Nele Smeets
 * @author Kristof Mertens
 * @author Jan Dockx
 *
 * This file is part of Jnome.
 *
 * Jnome is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Jnome is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Jnome; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */
package chameleon.core.method.exception;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;
import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.context.LookupException;
import chameleon.core.expression.Invocation;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.util.Util;

/**
 * @author marko
 */
public class TypeExceptionDeclaration extends ExceptionDeclaration<TypeExceptionDeclaration> {

  public TypeExceptionDeclaration(TypeReference type) {
    setTypeReference(type);
  }

  public TypeExceptionDeclaration(String type) {
    setTypeReference(new TypeReference(type));
  }

  public Set getExceptionTypes(Invocation invocation) throws LookupException {
    return getExceptionTypeSet();
  }
  
  public Set getWorstCaseExceptionTypes() throws LookupException {
    return getExceptionTypeSet();
  }
  
  private Set getExceptionTypeSet() throws LookupException {
    Set result = new HashSet();
    result.add(getType());
    return result;
  }
  
  /******************
   * TYPE REFERENCE *
   ******************/

	private Reference<TypeExceptionDeclaration,TypeReference> _typeReference = new Reference<TypeExceptionDeclaration,TypeReference>(this);

	public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }
  
  public void setTypeReference(TypeReference ref) {
    _typeReference.connectTo(ref.parentLink());
  }

  /**
   * Return the type of the exception that can be thrown according to this exception declaration.
   */
 /*@
   @ public behavior
   @
   @ post \result == getTypeReference().getType();
   @*/
  public Type getType() throws LookupException {
    return getTypeReference().getType();
  }



  public boolean compatibleWith(final ExceptionClause other) throws LookupException {
    if(! getNamespace().language().isCheckedException(getType())) {
      return true;
    }
    try {
      return new PrimitivePredicate() {
        public boolean eval(Object o2) throws LookupException {
          return (o2 instanceof TypeExceptionDeclaration) && (getType().assignableTo(((TypeExceptionDeclaration)o2).getType()));
        }
      }.exists(other.getDeclarations());
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  public TypeExceptionDeclaration clone() {
    return new TypeExceptionDeclaration((TypeReference)getTypeReference().clone());
  }

  

  public boolean hasValidAccessibility() throws LookupException {
    return true; 
  }

  public List children() {
    return Util.createNonNullList(getTypeReference());
  }

  public boolean isAcyclic(Set done) {
    return true;
  }
}

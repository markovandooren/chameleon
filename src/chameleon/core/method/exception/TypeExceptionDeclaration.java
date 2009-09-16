package chameleon.core.method.exception;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.SingleAssociation;
import org.rejuse.predicate.AbstractPredicate;

import chameleon.core.expression.Invocation;
import chameleon.core.language.ObjectOrientedLanguage;
import chameleon.core.lookup.LookupException;
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

	private SingleAssociation<TypeExceptionDeclaration,TypeReference> _typeReference = new SingleAssociation<TypeExceptionDeclaration,TypeReference>(this);

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
    if(! language(ObjectOrientedLanguage.class).isCheckedException(getType())) {
      return true;
    }
    try {
      return new AbstractPredicate() {
        public boolean eval(Object o2) throws LookupException {
          return (o2 instanceof TypeExceptionDeclaration) && (getType().assignableTo(((TypeExceptionDeclaration)o2).getType()));
        }
      }.exists(other.exceptionDeclarations());
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

}

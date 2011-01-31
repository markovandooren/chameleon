package chameleon.core.method.exception;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.SingleAssociation;
import org.rejuse.predicate.AbstractPredicate;

import chameleon.core.element.Element;
import chameleon.core.expression.MethodInvocation;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

/**
 * A class for absolute exception declarations. An absolute exception declaration declares that a certain type of exception
 * can be thrown at run-time.
 * 
 * @author Marko van Dooren
 */
public class TypeExceptionDeclaration extends ExceptionDeclaration<TypeExceptionDeclaration> {

  public TypeExceptionDeclaration(TypeReference type) {
    setTypeReference(type);
  }

  public Set<Type> getExceptionTypes(MethodInvocation invocation) throws LookupException {
    return getExceptionTypeSet();
  }
  
  public Set<Type> getWorstCaseExceptionTypes() throws LookupException {
    return getExceptionTypeSet();
  }
  
  private Set<Type> getExceptionTypeSet() throws LookupException {
  	Set<Type> result = new HashSet<Type>();
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
    return new TypeExceptionDeclaration(getTypeReference().clone());
  }

  

  public boolean hasValidAccessibility() throws LookupException {
    return true; 
  }

  public List children() {
    return Util.createNonNullList(getTypeReference());
  }

	@Override
	public VerificationResult verifySelf() {
		try {
			boolean isException = language(ObjectOrientedLanguage.class).isException(getType());
			if(isException) {
				return Valid.create();
			} else {
				return new NonExceptionType(this);
			}
		} catch (LookupException e) {
			return new NonExceptionType(this);
		}
	}
	
	public static class NonExceptionType extends BasicProblem {

		public NonExceptionType(Element element) {
			super(element, "The type is not an exception type.");
		}
		
	}

}

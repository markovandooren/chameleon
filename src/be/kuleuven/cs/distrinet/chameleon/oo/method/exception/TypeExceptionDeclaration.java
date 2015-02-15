package be.kuleuven.cs.distrinet.chameleon.oo.method.exception;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.MethodInvocation;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;

/**
 * A class for absolute exception declarations. An absolute exception declaration declares that a certain type of exception
 * can be thrown at run-time.
 * 
 * @author Marko van Dooren
 */
public class TypeExceptionDeclaration extends ExceptionDeclaration {

  public TypeExceptionDeclaration(TypeReference type) {
    setTypeReference(type);
  }

  @Override
public Set<Type> getExceptionTypes(MethodInvocation invocation) throws LookupException {
    return getExceptionTypeSet();
  }
  
  @Override
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

	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);

	public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }
  
  public void setTypeReference(TypeReference ref) {
    set(_typeReference,ref);
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



  @Override
public boolean compatibleWith(final ExceptionClause other) throws LookupException {
    if(! language(ObjectOrientedLanguage.class).isCheckedException(getType())) {
      return true;
    }
    try {
      return new AbstractPredicate() {
        @Override
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

  @Override
public TypeExceptionDeclaration cloneSelf() {
    return new TypeExceptionDeclaration(null);
  }

  @Override
public boolean hasValidAccessibility() throws LookupException {
    return true; 
  }

	@Override
	public Verification verifySelf() {
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

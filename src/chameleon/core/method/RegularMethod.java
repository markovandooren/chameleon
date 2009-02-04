package chameleon.core.method;

import org.rejuse.association.Reference;

import chameleon.core.method.exception.ExceptionClause;
import chameleon.core.type.TypeReference;

public abstract class RegularMethod<E extends RegularMethod<E,S>, S extends MethodSignature> extends Method<E,S> {

	public RegularMethod(S signature, TypeReference returnType) {
		super(signature);
		setReturnTypeReference(returnType);
		setExceptionClause(new ExceptionClause());
	}
	
	private Reference<Method,TypeReference> _typeReference = new Reference<Method,TypeReference>(this);

	public TypeReference getReturnTypeReference() {
		return _typeReference.getOtherEnd();
	}

	public void setReturnTypeReference(TypeReference type) {
		if(type != null) {
			_typeReference.connectTo(type.parentLink());
		}
		else {
			_typeReference.connectTo(null);
		}
	}

	private Reference<Method,Implementation> _implementationLink = new Reference<Method,Implementation>(this);

	public Implementation getImplementation() {
		return _implementationLink.getOtherEnd();
	}

	public void setImplementation(Implementation implementation) {
		if (implementation != null) {
			_implementationLink.connectTo(implementation.parentLink());
		}
		else {
			_implementationLink.connectTo(null);
		}
	}

  /**
   * EXCEPTION CLAUSE
   */
  private Reference<Method<? extends Method,? extends MethodSignature>,ExceptionClause> _exceptionClause = new Reference<Method<? extends Method,? extends MethodSignature>,ExceptionClause>(this);


  public ExceptionClause getExceptionClause() {
    return _exceptionClause.getOtherEnd();
  }

  public void setExceptionClause(ExceptionClause clause) {
    if(clause != null) {
      _exceptionClause.connectTo(clause.parentLink());
    }
    else {
      _exceptionClause.connectTo(null);
    }
  }

}

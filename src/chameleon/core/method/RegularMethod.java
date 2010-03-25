package chameleon.core.method;

import org.rejuse.association.SingleAssociation;

import chameleon.core.method.exception.ExceptionClause;
import chameleon.core.type.TypeReference;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;

public abstract class RegularMethod<E extends RegularMethod<E,H,S,M>, H extends MethodHeader<H, E, S>, S extends MethodSignature,M extends Method> extends Method<E,H,S,M> {

	public RegularMethod(H header, TypeReference returnType) {
		super(header);
		setReturnTypeReference(returnType);
		setExceptionClause(new ExceptionClause());
	}
	
	private SingleAssociation<Method,TypeReference> _typeReference = new SingleAssociation<Method,TypeReference>(this);

	public TypeReference returnTypeReference() {
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

	private SingleAssociation<Method,Implementation> _implementationLink = new SingleAssociation<Method,Implementation>(this);

	public Implementation implementation() {
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
  private SingleAssociation<RegularMethod<E,H,S,M>,ExceptionClause> _exceptionClause = new SingleAssociation<RegularMethod<E,H,S,M>,ExceptionClause>(this);


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
  
	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = super.verifySelf();
		if(returnTypeReference() == null) {
			result = result.and(new BasicProblem(this, "Method "+name()+" has no return type."));
		}
		return result;
	}

}

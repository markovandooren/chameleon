package chameleon.oo.method;

import org.rejuse.association.SingleAssociation;

import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.member.DeclarationWithParametersSignature;
import chameleon.oo.method.exception.ExceptionClause;
import chameleon.oo.type.TypeReference;

public abstract class RegularMethod<E extends RegularMethod<E,H,S>, H extends MethodHeader<H, S>, S extends DeclarationWithParametersSignature> extends Method<E,H,S> {

	public RegularMethod(H header) {
		super(header);
		setExceptionClause(new ExceptionClause());
	}
	
	public TypeReference returnTypeReference() {
		return header().returnTypeReference();
	}

	public void setReturnTypeReference(TypeReference type) {
		header().setReturnTypeReference(type);
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
  private SingleAssociation<RegularMethod<E,H,S>,ExceptionClause> _exceptionClause = new SingleAssociation<RegularMethod<E,H,S>,ExceptionClause>(this);


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

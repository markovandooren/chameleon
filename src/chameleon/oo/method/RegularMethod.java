package chameleon.oo.method;

import org.rejuse.association.SingleAssociation;

import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.member.DeclarationWithParametersSignature;
import chameleon.oo.method.exception.ExceptionClause;
import chameleon.oo.type.TypeReference;

public abstract class RegularMethod extends Method {

	public RegularMethod(MethodHeader header) {
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
		setAsParent(_implementationLink,implementation);
	}

  /**
   * EXCEPTION CLAUSE
   */
  private SingleAssociation<RegularMethod,ExceptionClause> _exceptionClause = new SingleAssociation<RegularMethod,ExceptionClause>(this);


  public ExceptionClause getExceptionClause() {
    return _exceptionClause.getOtherEnd();
  }

  public void setExceptionClause(ExceptionClause clause) {
    setAsParent(_exceptionClause,clause);
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

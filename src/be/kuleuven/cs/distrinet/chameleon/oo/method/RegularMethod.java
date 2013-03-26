package be.kuleuven.cs.distrinet.chameleon.oo.method;

import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.method.exception.ExceptionClause;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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

	private Single<Implementation> _implementationLink = new Single<Implementation>(this);

	public Implementation implementation() {
		return _implementationLink.getOtherEnd();
	}

	public void setImplementation(Implementation implementation) {
		set(_implementationLink,implementation);
	}

  /**
   * EXCEPTION CLAUSE
   */
  private Single<ExceptionClause> _exceptionClause = new Single<ExceptionClause>(this);


  public ExceptionClause getExceptionClause() {
    return _exceptionClause.getOtherEnd();
  }

  public void setExceptionClause(ExceptionClause clause) {
    set(_exceptionClause,clause);
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

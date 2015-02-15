package be.kuleuven.cs.distrinet.chameleon.oo.method;

import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.method.exception.ExceptionClause;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public abstract class RegularMethod extends Method {

	public RegularMethod(MethodHeader header) {
		super(header);
		setExceptionClause(new ExceptionClause());
	}
	
	@Override
   public TypeReference returnTypeReference() {
		return header().returnTypeReference();
	}

	@Override
   public void setReturnTypeReference(TypeReference type) {
		header().setReturnTypeReference(type);
	}

	private Single<Implementation> _implementationLink = new Single<Implementation>(this);

	@Override
   public Implementation implementation() {
		return _implementationLink.getOtherEnd();
	}

	@Override
   public void setImplementation(Implementation implementation) {
		set(_implementationLink,implementation);
	}

  /**
   * EXCEPTION CLAUSE
   */
  private Single<ExceptionClause> _exceptionClause = new Single<ExceptionClause>(this);


  @Override
public ExceptionClause getExceptionClause() {
    return _exceptionClause.getOtherEnd();
  }

  @Override
public void setExceptionClause(ExceptionClause clause) {
    set(_exceptionClause,clause);
  }
  
	@Override
	public Verification verifySelf() {
		Verification result = super.verifySelf();
		if(returnTypeReference() == null) {
			result = result.and(new BasicProblem(this, "Method "+name()+" has no return type."));
		}
		return result;
	}

}

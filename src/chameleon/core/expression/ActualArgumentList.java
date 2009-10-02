package chameleon.core.expression;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class ActualArgumentList extends NamespaceElementImpl<ActualArgumentList, Element> {

	
	/**
	 * ACTUAL PARAMETERS
	 */
	private OrderedMultiAssociation<ActualArgumentList,ActualArgument> _parametersLink = new OrderedMultiAssociation<ActualArgumentList,ActualArgument>(this);


  public OrderedMultiAssociation<ActualArgumentList,ActualArgument> getParametersLink() {
    return _parametersLink;
  }

  public void addParameter(ActualArgument parameter) {
    _parametersLink.add(parameter.parentLink());
  }

  public void removeParameter(ActualArgument parameter) {
    _parametersLink.remove(parameter.parentLink());
  }

  public List<ActualArgument> getActualParameters() {
    return _parametersLink.getOtherEnds();
  }
  
 /*@
   @ public behavior
   @
   @ post \result == getActualParameters().size;
   @*/
  public int nbActualParameters() {
  	return _parametersLink.size();
  }

	@Override
	public ActualArgumentList clone() {
		ActualArgumentList result = new ActualArgumentList();
		for(ActualArgument param: getActualParameters()) {
			result.addParameter(param.clone());
		}
		return result;
	}

	public List<? extends Element> children() {
		return getActualParameters();
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}

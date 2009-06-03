package chameleon.core.expression;

import java.util.List;

import org.rejuse.association.OrderedReferenceSet;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.type.Type;
import chameleon.core.type.TypeDescendant;

public class ActualArgumentList extends ElementImpl<ActualArgumentList, TypeDescendant> implements TypeDescendant<ActualArgumentList, TypeDescendant>{

	
	/**
	 * ACTUAL PARAMETERS
	 */
	private OrderedReferenceSet<ActualArgumentList,ActualParameter> _parametersLink = new OrderedReferenceSet<ActualArgumentList,ActualParameter>(this);


  public OrderedReferenceSet<ActualArgumentList,ActualParameter> getParametersLink() {
    return _parametersLink;
  }

  public void addParameter(ActualParameter parameter) {
    _parametersLink.add(parameter.parentLink());
  }

  public void removeParameter(ActualParameter parameter) {
    _parametersLink.remove(parameter.parentLink());
  }

  public List<ActualParameter> getActualParameters() {
    return _parametersLink.getOtherEnds();
  }

	@Override
	public ActualArgumentList clone() {
		ActualArgumentList result = new ActualArgumentList();
		for(ActualParameter param: getActualParameters()) {
			result.addParameter(param.clone());
		}
		return result;
	}

	public Type getNearestType() {
		return parent().getNearestType();
	}

	public CompilationUnit getCompilationUnit() {
		return parent().getCompilationUnit();
	}

	public NamespacePart getNearestNamespacePart() {
		return parent().getNearestNamespacePart();
	}

	public List<? extends Element> children() {
		return getActualParameters();
	}

	public Namespace getNamespace() {
		return parent().getNamespace();
	}

}

package chameleon.oo.type;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class ParameterBlock<E extends ParameterBlock<E,T>, T extends Parameter> extends NamespaceElementImpl<E> {

	public ParameterBlock(Class<T> parameterType) {
		_parameterType = parameterType;
	}
	
	public Class<T> parameterType() {
		return _parameterType;
	}
	
	private Class<T> _parameterType;
	
	public List<? extends Element> children() {
		return parameters();
	}

	private OrderedMultiAssociation<ParameterBlock, T> _parameters = new OrderedMultiAssociation<ParameterBlock, T>(this);
	
	public List<T> parameters() {
		return _parameters.getOtherEnds();
	}
	
	public int nbTypeParameters() {
		return _parameters.size();
	}
	
	/**
	 * Indices start at 1.
	 */
	public T parameter(int index) {
		return _parameters.elementAt(index);
	}
	
	public void add(Parameter parameter) {
		if(parameter != null) {
			_parameters.add(parameter.parentLink());
		}
	}

	public void remove(T parameter) {
		if(parameter != null) {
			_parameters.remove(parameter.parentLink());
		}
	}
	
	public void replace(T oldParameter, T newParameter) {
		if((oldParameter != null) && (newParameter != null)){
			_parameters.replace(oldParameter.parentLink(), newParameter.parentLink());
		}
	}

	@Override
	public E clone() {
		E result = cloneThis();
		for(T parameter: parameters()) {
			result.add(parameter.clone());
		}
		return result;
	}

	public E cloneThis() {
		return (E) new ParameterBlock(parameterType());
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public List<? extends Declaration> declarations() throws LookupException {
		return parameters();
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}
	
}

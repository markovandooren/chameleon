package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.rejuse.association.Association;

public class ParameterBlock<T extends Parameter> extends ElementImpl {

	public ParameterBlock(Class<T> parameterType) {
		_parameterType = parameterType;
	}
	
	public Class<T> parameterType() {
		return _parameterType;
	}
	
	private Class<T> _parameterType;
	
	private Multi<T> _parameters = new Multi<T>(this, "parameters");
	{
		_parameters.enableCache();
	}
	
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
	
	public void add(T parameter) {
		add(_parameters,parameter);
	}

	public void remove(T parameter) {
		remove(_parameters,parameter);
	}
	
	public void replace(T oldParameter, T newParameter) {
		if((oldParameter != null) && (newParameter != null)){
			_parameters.replace((Association)oldParameter.parentLink(), (Association)newParameter.parentLink());
		}
	}

	@Override
   protected ParameterBlock<T> cloneSelf() {
		return new ParameterBlock<T>(parameterType());
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public List<? extends Declaration> declarations() throws LookupException {
		return parameters();
	}

	public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}
	
}

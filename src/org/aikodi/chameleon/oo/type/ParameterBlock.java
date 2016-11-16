package org.aikodi.chameleon.oo.type;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.rejuse.association.Association;

public class ParameterBlock<T extends Parameter> extends ElementImpl implements DeclarationContainer {

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
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return parameters();
	}

}

package chameleon.aspect.oo.model.advice.modifier;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.modifier.ModifierImpl;
import chameleon.oo.variable.FormalParameter;
import chameleon.util.Util;
import chameleon.util.association.Single;

/**
 * 
 * @author Marko van Dooren
 */
public abstract class ModifierWithParameters extends ModifierImpl implements DeclarationContainer {

	/**
	 * 	The return parameter
	 */
	private Single<FormalParameter> _parameter = new Single<FormalParameter>(this);

	public ModifierWithParameters() {
		super();
	}

	/**
	 * 	Check if this returning modifier defines a return parameter
	 * 
	 * 	@return	True if there is a return parameter defined, false otherwise
	 */
	public boolean hasParameter() {
		return (parameter() != null);
	}

	/**
	 * 	Set the parameter
	 * 
	 * 	@param 	parameter
	 * 			The return parameter
	 */
	public void setParameter(FormalParameter parameter) {
		set(_parameter, parameter);
	}

	/**
	 * 	Get the return parameter
	 * 
	 * 	@return	The return parameter
	 */
	public FormalParameter parameter() {
		return _parameter.getOtherEnd();
	}

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public List<? extends Declaration> declarations() throws LookupException {
		return Util.createNonNullList(parameter());
	}

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public ModifierWithParameters clone() {
		FormalParameter exceptionParameterClone = null;
		
		if (hasParameter())
			exceptionParameterClone = parameter().clone();
		ModifierWithParameters clone = cloneThis();
		clone.setParameter(exceptionParameterClone);
		
		return clone;
	}

	protected abstract ModifierWithParameters cloneThis();
	
	@Override
	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		return language().lookupFactory().createLexicalLookupStrategy(localStrategy(), this);
	}

	public LookupStrategy localStrategy() {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}

}
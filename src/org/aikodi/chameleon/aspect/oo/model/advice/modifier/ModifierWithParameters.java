package org.aikodi.chameleon.aspect.oo.model.advice.modifier;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.oo.variable.FormalParameter;
import org.aikodi.chameleon.util.Util;
import org.aikodi.chameleon.util.association.Single;

/**
 * 
 * @author Marko van Dooren
 */
public abstract class ModifierWithParameters extends ModifierImpl implements DeclarationContainer {

	/**
	 * 	The return parameter
	 */
	private Single<FormalParameter> _parameter = new Single<FormalParameter>(this, "parameter");

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
	public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public LookupContext lookupContext(Element child) throws LookupException {
		return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
	}

	@Override
   public LookupContext localContext() {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}

}

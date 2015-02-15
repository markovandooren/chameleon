package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.modifier;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
	public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
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

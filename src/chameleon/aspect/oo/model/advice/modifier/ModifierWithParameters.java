package chameleon.aspect.oo.model.advice.modifier;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.LookupStrategyFactory;
import chameleon.core.modifier.ModifierImpl;
import chameleon.oo.variable.FormalParameter;
import chameleon.util.Util;

/**
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 */
public abstract class ModifierWithParameters<E extends ModifierWithParameters<E>> extends ModifierImpl<E> implements DeclarationContainer<E> {

	/**
	 * 	The return parameter
	 */
	private SingleAssociation<ModifierWithParameters<E>, FormalParameter> _parameter = new SingleAssociation<ModifierWithParameters<E>, FormalParameter>(this);

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
		setAsParent(_parameter, parameter);
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

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public List<? extends Element> children() {
		List<? extends Element> children = super.children();
		Util.addNonNull(parameter(), children);
		return children;
	}

	@Override
	public E clone() {
		FormalParameter exceptionParameterClone = null;
		
		if (hasParameter())
			exceptionParameterClone = parameter().clone();
		E clone = cloneThis();
		clone.setParameter(exceptionParameterClone);
		
		return (E) clone;
	}

	protected abstract E cloneThis();
	
	@Override
	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		LookupStrategyFactory lookupFactory = language().lookupFactory();
		return lookupFactory.createLexicalLookupStrategy(lookupFactory.createLocalLookupStrategy(this), this);
	}

}
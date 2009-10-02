package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

/**
 * WARNING! If you use a parameter block as an subelement of a class X, then you must add
 * a lookupstrategy to X that directly returns parameters(). Declarations() returns lazy type aliases
 * to support recursion in generic parameters. If anything is wrong in a type reference of a generic parameter,
 * then it won't be noticed by the lazy alias until an operation is executed on the alias by the declaration
 * selector. At that point, there should be no exception. If you use parameters() instead, any problems will be
 * detected before the declaration selector can do its job. because of this, lazy alias ignores exceptions
 * during lookup of the aliased type (the upperbound of generic parameter) because they cannot occur anymore
 * at that stage.
 *   
 * @author Marko van Dooren
 */
public class TypeParameterBlock extends NamespaceElementImpl<TypeParameterBlock, Element> implements DeclarationContainer<TypeParameterBlock, Element> {

	@Override
	public TypeParameterBlock clone() {
		TypeParameterBlock result = new TypeParameterBlock();
		for(TypeParameter parameter: parameters()) {
			result.add(parameter.clone());
		}
		return result;
	}

	public List<? extends Element> children() {
		return parameters();
	}

	private OrderedMultiAssociation<TypeParameterBlock, TypeParameter> _parameters = new OrderedMultiAssociation<TypeParameterBlock, TypeParameter>(this);
	
	public List<TypeParameter> parameters() {
		return _parameters.getOtherEnds();
	}
	
	public void add(TypeParameter parameter) {
		if(parameter != null) {
			_parameters.add(parameter.parentLink());
		}
	}

	public void remove(TypeParameter parameter) {
		if(parameter != null) {
			_parameters.remove(parameter.parentLink());
		}
	}
	
	public void replace(TypeParameter oldParameter, TypeParameter newParameter) {
		if((oldParameter != null) && (newParameter != null)){
			_parameters.replace(oldParameter.parentLink(), newParameter.parentLink());
		}
	}

	public List<? extends Declaration> declarations() throws LookupException {
//	return parameters();
		List<Declaration> result = new ArrayList<Declaration>();
		Stub stub = new Stub();
		stub.setUniParent(parent());
		for(TypeParameter parameter:parameters()) {
			TypeParameter clone = parameter.clone();
			result.add(clone);
			stub.add(clone);
		}
    return result;
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}
	
	public LookupStrategy lexicalLookupStrategy(Element element) {
		return language().lookupFactory().createLexicalLookupStrategy(language().lookupFactory().createLocalLookupStrategy(this), this);
	}
	
	public static class Stub extends ElementImpl<Stub, Element> implements DeclarationContainer<Stub, Element>{

		@Override
		public Stub clone() {
			Stub result = new Stub();
			for(TypeParameter parameter: parameters()) {
				result.add(parameter.clone());
			}
			return result;
		}
		
		public LookupStrategy lexicalLookupStrategy(Element element) {
			return language().lookupFactory().createLexicalLookupStrategy(language().lookupFactory().createLocalLookupStrategy(this), this);
		}
		

		public List<? extends Declaration> declarations() throws LookupException {
				List<Declaration> result = new ArrayList<Declaration>();
				for(TypeParameter parameter: parameters()) {
					result.add(parameter.resolveForRoundTrip());
				}
		    return result;
		}

		public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
			return selector.selection(declarations());
		}

		public List<? extends Element> children() {
			return parameters();
		}
		
		private OrderedMultiAssociation<Stub, TypeParameter> _parameters = new OrderedMultiAssociation<Stub, TypeParameter>(this);
		
		private List<TypeParameter> parameters() {
			return _parameters.getOtherEnds();
		}
		
		public void add(TypeParameter parameter) {
			if(parameter != null) {
				_parameters.add(parameter.parentLink());
			}
		}

		public void remove(TypeParameter parameter) {
			if(parameter != null) {
				_parameters.remove(parameter.parentLink());
			}
		}
		
		public void replace(TypeParameter oldParameter, TypeParameter newParameter) {
			if((oldParameter != null) && (newParameter != null)){
				_parameters.replace(oldParameter.parentLink(), newParameter.parentLink());
			}
		}

		@Override
		public VerificationResult verifySelf() {
			return Valid.create();
		}


		
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
	
}

package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedReferenceSet;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacepart.NamespaceElementImpl;
import chameleon.core.type.ConstructedType;
import chameleon.core.type.Type;

public class TypeParameterBlock extends NamespaceElementImpl<TypeParameterBlock, Type> implements DeclarationContainer<TypeParameterBlock, Type> {

	@Override
	public TypeParameterBlock clone() {
		TypeParameterBlock result = new TypeParameterBlock();
		for(GenericParameter parameter: parameters()) {
			result.add(parameter.clone());
		}
		return result;
	}

	public List<? extends Element> children() {
		return parameters();
	}

	private OrderedReferenceSet<TypeParameterBlock, GenericParameter> _parameters = new OrderedReferenceSet<TypeParameterBlock, GenericParameter>(this);
	
	public List<GenericParameter> parameters() {
		return _parameters.getOtherEnds();
	}
	
	public void add(GenericParameter parameter) {
		if(parameter != null) {
			_parameters.add(parameter.parentLink());
		}
	}

	public void remove(GenericParameter parameter) {
		if(parameter != null) {
			_parameters.remove(parameter.parentLink());
		}
	}
	
	public void replace(GenericParameter oldParameter, GenericParameter newParameter) {
		if((oldParameter != null) && (newParameter != null)){
			_parameters.replace(oldParameter.parentLink(), newParameter.parentLink());
		}
	}

	public List<? extends Declaration> declarations() throws LookupException {
//	return parameters();
		List<Declaration> result = new ArrayList<Declaration>();
		for(GenericParameter parameter:parameters()) {
			result.add(parameter.resolveForRoundTrip());
		}
    return result;
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}
	
	public LookupStrategy lexicalContext(Element element) {
		return language().lookupFactory().createLexicalLookupStrategy(language().lookupFactory().createLocalLookupStrategy(this), this);
	}

}

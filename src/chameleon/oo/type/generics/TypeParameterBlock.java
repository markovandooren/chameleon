package chameleon.oo.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.Association;
import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.ParameterBlock;

/**
 * WARNING! If you use a parameter block as an subelement of a class X, then you must add
 * a lookupstrategy to X that directly returns parameters(). Declarations() returns stubs
 * to support recursion in generic parameters. If anything is wrong in a type reference of a generic parameter,
 * then it won't be noticed by the lazy alias until an operation is executed on the alias by the declaration
 * selector. At that point, there should be no exception. If you use parameters() instead, any problems will be
 * detected before the declaration selector can do its job. because of this, lazy alias ignores exceptions
 * during lookup of the aliased type (the upperbound of generic parameter) because they cannot occur anymore
 * at that stage.
 *   
 * @author Marko van Dooren
 */
public class TypeParameterBlock extends ParameterBlock<TypeParameter> implements DeclarationContainer {

	public TypeParameterBlock() {
		super(TypeParameter.class);
	}
	
	@Override
	public TypeParameterBlock cloneThis() {
		TypeParameterBlock result = new TypeParameterBlock();
//		for(TypeParameter<?> parameter: parameters()) {
//			result.add(parameter.clone());
//		}
		return result;
	}


	public List<? extends Declaration> declarations() throws LookupException {
//	return parameters();
		List<Declaration> result = new ArrayList<Declaration>();
		Stub stub = new Stub();
//		stub.setUniParent(parent());
		stub.setUniParent(this);
		for(TypeParameter parameter:parameters()) {
			//FIXME must create subclass of formalparameter that keeps a reference to the original formal
			// parameter OR use origin() for that.
//			TypeParameter clone = new StubTypeParameter(parameter);
			TypeParameter clone = parameter.cloneForStub();
			clone.setOrigin(parameter);
			result.add(clone);
			stub.add(clone);
		}
    return result;
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}
	
	public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
		if(element instanceof Stub) {
			return parent().lexicalLookupStrategy(this);
		} else {
		  return language().lookupFactory().createLexicalLookupStrategy(localStrategy(), this);
		}
	}

	public LookupStrategy localStrategy() {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}
	
	public static class Stub extends ElementImpl implements DeclarationContainer{

		@Override
		public Stub clone() {
			Stub result = new Stub();
			for(TypeParameter parameter: parameters()) {
				result.add((TypeParameter) parameter.clone());
			}
			return result;
		}
		
		public LookupStrategy lexicalLookupStrategy(Element element) {
			return language().lookupFactory().createLexicalLookupStrategy(localStrategy(), this);
		}

		public LookupStrategy localStrategy() {
			return language().lookupFactory().createLocalLookupStrategy(this);
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
			add(_parameters,parameter);
		}

		public void remove(TypeParameter parameter) {
			remove(_parameters,parameter);
		}
		
		public void replace(TypeParameter oldParameter, TypeParameter newParameter) {
			if((oldParameter != null) && (newParameter != null)){
				_parameters.replace((Association)oldParameter.parentLink(), (Association)newParameter.parentLink());
			}
		}

		@Override
		public VerificationResult verifySelf() {
			return Valid.create();
		}

		public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
			return declarations();
		}

	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}
	
//	public static class StubTypeParameter extends TypeParameter<StubTypeParameter> {
//
//		public StubTypeParameter(TypeParameter original) {
//			super(original.signature().clone());
//			setOriginalTypeParameter(original);
//		}
//		
//		public void setOriginalTypeParameter(TypeParameter original) {
//			_original = original;
//		}
//		
//		private TypeParameter _original;
//
//		public TypeParameter originalTypeParameter() {
//			return _original;
//		}
//		
//		@Override
//		public StubTypeParameter clone() {
//			return new StubTypeParameter(originalTypeParameter());
//		}
//
//		@Override
//		public Type lowerBound() throws LookupException {
//			return originalTypeParameter().lowerBound();
//		}
//
//		@Override
//		public Declaration resolveForRoundTrip() throws LookupException {
//			return originalTypeParameter().resolveForRoundTrip();
//		}
//
//		@Override
//		public boolean uniSameAs(Element other) throws LookupException {
//			return originalTypeParameter().uniSameAs(other);
//		}
//
//		@Override
//		public Type upperBound() throws LookupException {
//			return originalTypeParameter().upperBound();
//		}
//
//		public Declaration<?, ?, ?, Type> selectionDeclaration() throws LookupException {
//			return originalTypeParameter().selectionDeclaration();
//		}
//
//		public List<? extends Element> children() {
//			return Util.createSingletonList(signature());
//		}
//
//		@Override
//		public TypeReference upperBoundReference() throws LookupException {
//			return originalTypeParameter().upperBoundReference();
//		}
//		
//	}
}

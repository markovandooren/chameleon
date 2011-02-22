package chameleon.oo.type;

import java.util.Iterator;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;
import org.rejuse.predicate.TypePredicate;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.member.Member;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.type.generics.TypeParameterBlock;
import chameleon.oo.type.inheritance.AbstractInheritanceRelation;
import chameleon.oo.type.inheritance.InheritanceRelation;
import chameleon.util.Util;

public abstract class TypeWithBody extends AbstractType {

	protected SingleAssociation<Type, ClassBody> _body = new SingleAssociation<Type, ClassBody>(this);

	
	public List<InheritanceRelation> nonMemberInheritanceRelations() {
		return _inheritanceRelations.getOtherEnds();
	}
	
	public List<Element> children() {
		List<Element> result = super.children();
		result.addAll(parameterBlocks());
		Util.addNonNull(body(), result);
		return result;
	}

	public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
		List<ParameterBlock> parameterBlocks = parameterBlocks();
		if(parameterBlocks.contains(element)) { // || element.isDerived()
			int index = parameterBlocks.indexOf(element);
			if(index <= 0) {
			  return parent().lexicalLookupStrategy(this);
			} else {
				return parameterBlocks.get(index-1).lexicalLookupStrategy(element);
			}
		} else {
			return super.lexicalLookupStrategy(element);
		}
	}

	private OrderedMultiAssociation<Type, InheritanceRelation> _inheritanceRelations = new OrderedMultiAssociation<Type, InheritanceRelation>(this);

	public ClassBody body() {
		return _body.getOtherEnd();
	}

	public void setBody(ClassBody body) {
		setAsParent(_body,body);
	}

	public void add(TypeElement element) {
		  body().add(element);
		}

	@Override
	public void remove(TypeElement element) throws ChameleonProgrammerException {
		body().remove(element);
	}

	/**
	 * Return the members directly declared by this type.
	 * @return
	 * @throws LookupException 
	 */
	public List<Member> localMembers() throws LookupException {
	   return body().members();
	}

	protected OrderedMultiAssociation<Type, ParameterBlock> _parameters = new OrderedMultiAssociation<Type, ParameterBlock>(this);

	public void removeNonMemberInheritanceRelation(InheritanceRelation relation) {
		_inheritanceRelations.remove(relation.parentLink());
	}

	@Override
	public List<InheritanceRelation> inheritanceRelations() throws LookupException {
		return _inheritanceRelations.getOtherEnds();
	}

	@Override
	public void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException {
		_inheritanceRelations.add(relation.parentLink());
	}

	@Override
	public <D extends Member> List<D> localMembers(DeclarationSelector<D> selector) throws LookupException {
	//		return selector.selection(localMembers());
			return body().members(selector);
		}

	public void replace(TypeElement oldElement, TypeElement newElement) {
		body().replace(oldElement, newElement);
	}

	public static class MissingClassBody extends BasicProblem {
	
		public MissingClassBody(Element element) {
			super(element, "Class body is missing.");
		}
		
	}

	public List<ParameterBlock> parameterBlocks() {
		return _parameters.getOtherEnds();
	}

	public <P extends Parameter> void addParameter(Class<P> kind, P parameter) {
		parameterBlock(kind).add(parameter);
	}

	public <P extends Parameter> void removeParameter(Class<P> kind, P parameter) {
		parameterBlock(kind).add(parameter);
	}

	public <P extends Parameter> void replaceParameter(Class<P> kind, P oldParameter, P newParameter) {
		parameterBlock(kind).replace(oldParameter, newParameter);
	}

	@Override
	public List<? extends TypeElement> directlyDeclaredElements() {
		return body().elements();
	}

	public <T extends TypeElement> List<T> directlyDeclaredElements(Class<T> kind) {
  	List<TypeElement> tmp = (List<TypeElement>) directlyDeclaredElements();
  	new TypePredicate<TypeElement,T>(kind).filter(tmp);
    return (List<T>)tmp;
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult tmp = super.verifySelf();
		if(body() != null) {
		  return tmp;
		} else {
		  return tmp.and(new MissingClassBody(this));	
		}
	}

	public TypeWithBody(SimpleNameSignature sig) {
		super(sig);
		_body.connectTo(new ClassBody().parentLink());
		_parameters.add(new TypeParameterBlock().parentLink());
	}

	public <P extends Parameter> void replaceAllParameter(Class<P> kind, List<P> newParameters) {
		int size = newParameters.size();
		List<P> old = parameters(kind);
		if(old.size() != size) {
			throw new ChameleonProgrammerException("Trying to substitute "+old.size()+" type parameters with "+size+" new parameters.");
		}
		for(int i = 0; i< size; i++) {
			replaceParameter(kind, old.get(i), newParameters.get(i));
		}
	}
	
	public void substituteParameters(List<ParameterSubstitution> substitutions) {
		for(ParameterSubstitution substitution: substitutions) {
			substituteParameters(substitution);
		}
	}
	public void substituteParameters(ParameterSubstitution substitution) {
		substituteParameters(substitution.parameterKind(),substitution.parameters());
	}

	public <P extends Parameter> void substituteParameters(Class<P> kind, List<P> parameters) {
		Iterator<P> parametersIterator = parameters(kind).iterator();
		Iterator<P> argumentsIterator = parameters.iterator();
		while (parametersIterator.hasNext()) {
			P parameter = parametersIterator.next();
			P argument = argumentsIterator.next();
			// The next call does not change the parent of 'argument'. It is stored in InstantiatedTypeParameter
			// using a regular reference.
			replaceParameter(kind, parameter, argument);
		}
	}
	
	public Declaration declarator() {
		return this;
	}

	public void addParameterBlock(ParameterBlock block) {
		if(block != null && parameterBlock(block.parameterType()) != null) {
			throw new ChameleonProgrammerException("There is already a parameter block containing the following kind of element "+block.parameterType().getName());
		}
		setAsParent(_parameters, block);
	}

	public Class<? extends Parameter> kindOf(ParameterBlock block) throws LookupException {
		for(ParameterBlock p: parameterBlocks()) {
			if(p.sameAs(block)) {
				return p.parameterType();
			}
		}
		return null;
	}

	public <P extends Parameter> ParameterBlock<?, P> parameterBlock(Class<P> kind) {
		for(ParameterBlock p: parameterBlocks()) {
			if(p.parameterType().equals(kind)) {
				return p;
			}
		}
		return null;
	}

	public void removeParameterBlock(ParameterBlock block) {
		if(block != null) {
		  _parameters.remove(block.parentLink());
		}
	}


}
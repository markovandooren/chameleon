package chameleon.core.type;

import java.util.Iterator;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;
import org.rejuse.predicate.TypePredicate;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.member.Member;
import chameleon.core.type.generics.TypeParameter;
import chameleon.core.type.generics.TypeParameterBlock;
import chameleon.core.type.inheritance.InheritanceRelation;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.util.Util;

public abstract class AbstractType extends Type {

	protected SingleAssociation<Type, ClassBody> _body = new SingleAssociation<Type, ClassBody>(this);

	public List<Element> children() {
		List<Element> result = super.children();
		Util.addNonNull(parameterBlock(), result);
		Util.addNonNull(body(), result);
		return result;
	}

	public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
		if(element == parameterBlock()) { // || element.isDerived()
			return parent().lexicalLookupStrategy(this);
		} else {
			return super.lexicalLookupStrategy(element);
		}
	}

	private OrderedMultiAssociation<Type, InheritanceRelation> _inheritanceRelations = new OrderedMultiAssociation<Type, InheritanceRelation>(this);

	public ClassBody body() {
		return _body.getOtherEnd();
	}

	public void setBody(ClassBody body) {
		if(body == null) {
			throw new ChameleonProgrammerException("Body passed to setBody is null.");
		} else {
			_body.connectTo(body.parentLink());
		}
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

	protected SingleAssociation<Type, TypeParameterBlock> _parameters = new SingleAssociation<Type, TypeParameterBlock>(this);

	public void removeInheritanceRelation(InheritanceRelation relation) {
		_inheritanceRelations.remove(relation.parentLink());
	}

	@Override
	public List<InheritanceRelation> inheritanceRelations() {
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

	public TypeParameterBlock parameterBlock() {
		return _parameters.getOtherEnd();
	}

	public List<TypeParameter> parameters() {
		return parameterBlock().parameters();
	}

	public void addParameter(TypeParameter parameter) {
		parameterBlock().add(parameter);
	}

	public void removeParameter(TypeParameter parameter) {
		parameterBlock().add(parameter);
	}

	public void replaceParameter(TypeParameter oldParameter, TypeParameter newParameter) {
		parameterBlock().replace(oldParameter, newParameter);
	}

	@Override
	public List<? extends TypeElement> directlyDeclaredElements() {
		return body().elements();
	}

	public <T extends TypeElement> List<T> directlyDeclaredElements(Class<T> type) {
  	List<TypeElement> tmp = (List<TypeElement>) body().elements();
  	new TypePredicate<TypeElement,T>(type).filter(tmp);
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

	public AbstractType(SimpleNameSignature sig) {
		super(sig);
		_body.connectTo(new ClassBody().parentLink());
		_parameters.connectTo(new TypeParameterBlock().parentLink());
	}

	@Override
	public void replaceAllParameter(List<TypeParameter> newParameters) {
		int size = newParameters.size();
		List<TypeParameter> old = parameters();
		if(old.size() != size) {
			throw new ChameleonProgrammerException("Trying to substitute "+parameters().size()+" type parameters with "+size+" new parameters.");
		}
		for(int i = 0; i< size; i++) {
			replaceParameter(old.get(i), newParameters.get(i));
		}
	}

	public void substituteParameters(List<TypeParameter> typeParameters) {
		Iterator<TypeParameter> parametersIterator = parameters().iterator();
		Iterator<TypeParameter> argumentsIterator = typeParameters.iterator();
		while (parametersIterator.hasNext()) {
			TypeParameter parameter = parametersIterator.next();
			TypeParameter argument = argumentsIterator.next();
			// The next call does not change the parent of 'argument'. It is stored in InstantiatedTypeParameter
			// using a regular reference.
			replaceParameter(parameter, argument);
		}
	}

}
package chameleon.core.type;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.member.Member;
import chameleon.core.method.Method;
import chameleon.core.type.generics.TypeParameter;
import chameleon.core.type.generics.TypeParameterBlock;
import chameleon.core.type.inheritance.InheritanceRelation;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.util.Util;

public class RegularType extends Type {

	public RegularType(SimpleNameSignature sig) {
		super(sig);
		_body.connectTo(new ClassBody().parentLink());
		_parameters.connectTo(new TypeParameterBlock().parentLink());
	}
	
	public RegularType(String name) {
		this(new SimpleNameSignature(name));
	}
	
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
	@Override
	public RegularType clone() {
		RegularType result = cloneThis();
		result.copyContents(this);
		return result;
	}

	protected RegularType cloneThis() {
		return new RegularType(signature().clone());
	}

	private SingleAssociation<Type, ClassBody> _body = new SingleAssociation<Type, ClassBody>(this);

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
//		if(element instanceof Method) {
//			Method method = (Method)element;
//		List<Member> dmembers = directlyDeclaredMembers();
//		for(Member member:dmembers) {
//			try {
//				if(method.signature().sameAs(member.signature())) {
//					System.out.println("ola");
//				}
//			} catch (Exception e) {
//			}
//		}
//		}
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
 /*@
   @ public behavior
   @
   @ post \result == body.members();
   @*/
  public List<Member> localMembers() throws LookupException {
     return body().members();
  }

	private OrderedMultiAssociation<Type, InheritanceRelation> _inheritanceRelations = new OrderedMultiAssociation<Type, InheritanceRelation>(this);

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
		return selector.selection(localMembers());
	}
	
  public void replace(TypeElement oldElement, TypeElement newElement) {
		body().replace(oldElement, newElement);
  }

	@Override
	public Type baseType() {
		return this;
	}

	private SingleAssociation<Type, TypeParameterBlock> _parameters = new SingleAssociation<Type, TypeParameterBlock>(this);
	
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

	@Override
	public VerificationResult verifySelf() {
		VerificationResult tmp = super.verifySelf();
		if(body() != null) {
		  return tmp;
		} else {
		  return tmp.and(new MissingClassBody(this));	
		}
	}

	public static class MissingClassBody extends BasicProblem {

		public MissingClassBody(Element element) {
			super(element, "Class body is missing.");
		}
		
	}
}

package chameleon.aspect.oo.model.advice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.modifier.Modifier;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.Block;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.oo.variable.FormalParameter;
import chameleon.util.Util;

public abstract class ProgrammingAdvice extends Advice<Block> implements DeclarationContainer {

	public ProgrammingAdvice() {
		
	}
	
	@Override
	public LookupStrategy localStrategy() throws LookupException {
		return localLookupStrategy();
	}
	private SingleAssociation<ProgrammingAdvice, TypeReference> _returnType = new SingleAssociation<ProgrammingAdvice, TypeReference>(this);

	protected TypeReference returnType() {
		return _returnType.getOtherEnd();
	}

	public abstract Type actualReturnType() throws LookupException;

	public void setReturnType(TypeReference returnType) {
		setAsParent(_returnType, returnType);
	}

	public ProgrammingAdvice(TypeReference returnType) {
		setReturnType(returnType);
	}


	private OrderedMultiAssociation<ProgrammingAdvice, FormalParameter> _parameters = new OrderedMultiAssociation<ProgrammingAdvice, FormalParameter>(this);

	public List<FormalParameter> formalParameters() {
		return _parameters.getOtherEnds();
	}

	public void addFormalParameter(FormalParameter param) {
		setAsParent(_parameters, param);
	}

	public void addFormalParameters(List<FormalParameter> params) {
		if (params == null) {
			throw new IllegalArgumentException();
		}

		for (FormalParameter p : params) {
			addFormalParameter(p);
		}
	}

  @Override
  public List<Element> children() {
  	List<Element> result = super.children();
		result.addAll(formalParameters());
		Util.addNonNull(returnType(), result);
  	return result;
  }
  
  @Override
  public ProgrammingAdvice clone() {
  	ProgrammingAdvice result = (ProgrammingAdvice) super.clone();
  	for(FormalParameter param: formalParameters()) {
  		result.addFormalParameter(param.clone());
  	}
		if (returnType() != null) {
		  result.setReturnType(returnType().clone());
		}
  	return result;
  }
  
//	private List<FormalParameter> unresolvedParameters() {
//		List<FormalParameter> unresolved = new ArrayList<FormalParameter>();
//
//		for (FormalParameter fp : (List<FormalParameter>) formalParameters())
//			if (!pointcutExpression().hasParameter(fp))
//				unresolved.add(fp);
//
//		return unresolved;
//	}

  @Override
  public VerificationResult verifySelf() {
  	VerificationResult result = super.verifySelf();
//		List<FormalParameter> unresolved = unresolvedParameters();
//		if (!unresolved.isEmpty()) {
//
//			StringBuffer unresolvedList = new StringBuffer();
//			Iterator<FormalParameter> it = unresolved.iterator();
//			unresolvedList.append(it.next().getName());
//
//			while (it.hasNext()) {
//				unresolvedList.append(", ");
//				unresolvedList.append(it.next().getName());
//			}
//
//			result = result.and(new BasicProblem(this,
//					"The following parameters cannot be resolved: "
//							+ unresolvedList));
//		}

  	return result;
  }
  
	@Override
	public List<Declaration> declarations() throws LookupException {
		List<Declaration> declarations =  new ArrayList<Declaration>();
		for (Modifier m : modifiers()) {
			if (m instanceof DeclarationContainer)
				declarations.addAll(((DeclarationContainer) m).declarations());
		}
  	declarations.addAll(formalParameters());
		return declarations;
	}

	@Override
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
		if (element.equals(pointcutExpression()) || element.equals(body())) {
			if (_lexical == null) {
				_lexical = language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(),this);
			}
			return _lexical;
		} else {
			return parent().lexicalLookupStrategy(this);
		}
	}

	public LookupStrategy localLookupStrategy() {
		if (_local == null) {
			_local = language().lookupFactory().createTargetLookupStrategy(this);
		}
		return _local;
	}

	private LookupStrategy _local;

	private LookupStrategy _lexical;
}

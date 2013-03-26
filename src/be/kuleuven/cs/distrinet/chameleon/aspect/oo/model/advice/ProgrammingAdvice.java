package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.Advice;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupStrategy;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.Modifier;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public abstract class ProgrammingAdvice extends Advice<Block> implements DeclarationContainer {

	public ProgrammingAdvice() {
		
	}
	
	private Single<TypeReference> _returnType = new Single<TypeReference>(this);

	protected TypeReference returnType() {
		return _returnType.getOtherEnd();
	}

	public abstract Type actualReturnType() throws LookupException;

	public void setReturnType(TypeReference returnType) {
		set(_returnType, returnType);
	}

	public ProgrammingAdvice(TypeReference returnType) {
		setReturnType(returnType);
	}


	private Multi<FormalParameter> _parameters = new Multi<FormalParameter>(this);

	public List<FormalParameter> formalParameters() {
		return _parameters.getOtherEnds();
	}

	public void addFormalParameter(FormalParameter param) {
		add(_parameters, param);
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
	public LookupStrategy localStrategy() throws LookupException {
		return localLookupStrategy();
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

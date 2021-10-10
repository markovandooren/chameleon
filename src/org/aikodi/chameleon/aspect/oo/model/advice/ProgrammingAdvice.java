package org.aikodi.chameleon.aspect.oo.model.advice;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.variable.FormalParameter;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

import java.util.List;

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
  public Verification verifySelf() {
  	Verification result = super.verifySelf();
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
		List<Declaration> declarations =  Lists.create();
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
	public LookupContext localContext() throws LookupException {
		return localLookupStrategy();
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public LookupContext lookupContext(Element element) throws LookupException {
		if (element.equals(pointcutExpression()) || element.equals(body())) {
			if (_lexical == null) {
				_lexical = language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(),this);
			}
			return _lexical;
		} else {
			return parent().lookupContext(this);
		}
	}

	public LookupContext localLookupStrategy() {
		if (_local == null) {
			_local = language().lookupFactory().createTargetLookupStrategy(this);
		}
		return _local;
	}

	private LookupContext _local;

	private LookupContext _lexical;
}

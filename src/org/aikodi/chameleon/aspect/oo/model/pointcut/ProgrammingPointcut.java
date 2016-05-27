package org.aikodi.chameleon.aspect.oo.model.pointcut;

import java.util.List;

import org.aikodi.chameleon.aspect.core.model.pointcut.Pointcut;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.member.SignatureWithParameters;
import org.aikodi.chameleon.oo.member.SimpleNameDeclarationWithParametersHeader;
import org.aikodi.chameleon.oo.variable.FormalParameter;
import org.aikodi.chameleon.util.association.Single;

public class ProgrammingPointcut extends Pointcut implements DeclarationContainer {

	public ProgrammingPointcut(SimpleNameDeclarationWithParametersHeader header) {
		setHeader(header);
	}

	public ProgrammingPointcut(SimpleNameDeclarationWithParametersHeader header, PointcutExpression expression) {
		super(expression);
		setHeader(header);
	}

	private Single<SimpleNameDeclarationWithParametersHeader> _header = new Single<SimpleNameDeclarationWithParametersHeader>(this);
	
	public SimpleNameDeclarationWithParametersHeader header() {
		return _header.getOtherEnd();
	}
	
	protected void setHeader(SimpleNameDeclarationWithParametersHeader header) {
		set(_header, header);
	}
	
  @Override
  protected ProgrammingPointcut cloneSelf() {
  	return new ProgrammingPointcut(null, null);
  }
  
	@Override
	public boolean sameSignatureAs(Declaration declaration)
			throws LookupException {
		return signature().sameAs(declaration.signature());
	}

	public List<FormalParameter> parameters() {
		return header().formalParameters();
	}
	
//	private List<FormalParameter> unresolvedParameters() {
//		List<FormalParameter> unresolved = new ArrayList<FormalParameter>();
//		
//		for (FormalParameter fp : (List<FormalParameter>) header().formalParameters())
//			if (!expression().hasParameter(fp))
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
//			result = result.and(new BasicProblem(this, "The following parameters cannot be resolved: " + unresolvedList));
//		}
		
  	return result;
  }
  
	@Override
	public SignatureWithParameters signature() {
		return header().signature();
	}

	@Override
	public String name() {
		return signature().name();
	}

	@Override
	public void setSignature(Signature signature) {
		setHeader(header().createFromSignature(signature));
	}

	@Override
	public void setName(String name) {
		header().setName(name);
	}

	@Override
   public LookupContext lookupContext(Element element) throws LookupException {
		if (element == header()) {
			return parent().lookupContext(this);
		} else {
			if (_lexical == null) {
				_lexical = language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(),this);
			}
			return _lexical;
		}
	}

	public LookupContext localLookupStrategy() {
		if (_local == null) {
			_local = language().lookupFactory().createTargetLookupStrategy(this);
		}
		return _local;
	}
	
	@Override
	public LookupContext localContext() throws LookupException {
		return localLookupStrategy();
	}

	private LookupContext _local;

	private LookupContext _lexical;

	@Override
	public List<? extends Declaration> declarations() throws LookupException {
		return header().declarations();
	}

	@Override
	public List<? extends Declaration> locallyDeclaredDeclarations()
			throws LookupException {
		return declarations();
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return header().declarations(selector);
	}

	@Override
	public LocalLookupContext<?> targetContext() throws LookupException {
		throw new LookupException("Requesting the target context of a pointcut.");
	}


}

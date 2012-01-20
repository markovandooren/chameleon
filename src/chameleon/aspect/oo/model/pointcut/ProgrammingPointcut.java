package chameleon.aspect.oo.model.pointcut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.aspect.core.model.pointcut.Pointcut;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.member.SimpleNameDeclarationWithParametersHeader;
import chameleon.oo.member.SimpleNameDeclarationWithParametersSignature;
import chameleon.oo.variable.FormalParameter;
import chameleon.util.Util;

public class ProgrammingPointcut<E extends ProgrammingPointcut<E>> extends Pointcut<E> implements DeclarationContainer<E> {

	public ProgrammingPointcut(SimpleNameDeclarationWithParametersHeader header) {
		setHeader(header);
	}

	public ProgrammingPointcut(SimpleNameDeclarationWithParametersHeader header, PointcutExpression expression) {
		super(expression);
		setHeader(header);
	}

	private SingleAssociation<Pointcut<E>, SimpleNameDeclarationWithParametersHeader> _header = new SingleAssociation<Pointcut<E>, SimpleNameDeclarationWithParametersHeader>(this);
	
	public SimpleNameDeclarationWithParametersHeader header() {
		return _header.getOtherEnd();
	}
	
	protected void setHeader(SimpleNameDeclarationWithParametersHeader header) {
		setAsParent(_header, header);
	}
	
  @Override
  public E clone() {
  	E clone = super.clone();
		clone.setHeader((SimpleNameDeclarationWithParametersHeader) header().clone());
		return clone;
  }
  
  @Override
  protected E cloneThis() {
  	return (E) new ProgrammingPointcut((SimpleNameDeclarationWithParametersHeader) header().clone(), expression().clone());
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
//			result = result.and(new BasicProblem(this, "The following parameters cannot be resolved: " + unresolvedList));
//		}
		
  	return result;
  }
  
  @Override
  public List<Element> children() {
  	List<Element> result = super.children();
		Util.addNonNull(header(), result);
		return result;
  }
  
	@Override
	public SimpleNameDeclarationWithParametersSignature signature() {
		return header().signature();
	}

	@Override
	public void setSignature(Signature signature) {
		setHeader(header().createFromSignature(signature));
	}

	@Override
	public void setName(String name) {
		header().setName(name);
	}

	public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
		if (element == header()) {
			return parent().lexicalLookupStrategy(this);
		} else {
			if (_lexical == null) {
				_lexical = language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(),this);
			}
			return _lexical;
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
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return header().declarations(selector);
	}

	@Override
	public LookupStrategy targetContext() throws LookupException {
		throw new LookupException("Requesting the target context of a pointcut.");
	}


}

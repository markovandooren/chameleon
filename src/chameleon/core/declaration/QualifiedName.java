package chameleon.core.declaration;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;

public class QualifiedName<E extends QualifiedName, P extends Element> extends NamespaceElementImpl<E,P> {

	
	public List<Signature> signatures() {
		return _signatures.getOtherEnds();
	}
	
	public void append(Signature signature) {
		setAsParent(_signatures, signature);
	}
	
	public void prefix(Signature signature) {
		if(signature != null) {
			_signatures.addInFront(signature.parentLink());
		}
	}
	
	public void remove(Signature signature) {
		if(signature != null) {
			_signatures.remove(signature.parentLink());
		}
	}
	
	private OrderedMultiAssociation<QualifiedName, Signature> _signatures = new OrderedMultiAssociation<QualifiedName, Signature>(this);

	@Override
	public E clone() {
		QualifiedName result = new QualifiedName();
		for(Signature signature: signatures()) {
			result.append(signature.clone());
		}
		return (E)result;
	}

	@Override
	public VerificationResult verifySelf() {
		return new BasicProblem(this, "TODO: implement verifySelf of FullyQualifiedName");
	}

	public List<? extends Element> children() {
		return signatures();
	} 
}

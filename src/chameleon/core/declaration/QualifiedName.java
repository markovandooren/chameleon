package chameleon.core.declaration;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;

public abstract class QualifiedName<E extends QualifiedName, P extends Element> extends NamespaceElementImpl<E,P> {

	public abstract List<Signature> signatures();
	
	public abstract Signature lastSignature();
	
	public abstract E clone();
	
	public abstract int length();
	
	public QualifiedName<?,?> popped() {
		CompositeQualifiedName<?, ?> result = new CompositeQualifiedName();
		List<Signature> signatures = signatures();
		int length = signatures.size();
		for(int i=0; i< length-1; i++) {
			result.append(signatures.get(i).clone());
		}
		return result;
	}
	
}

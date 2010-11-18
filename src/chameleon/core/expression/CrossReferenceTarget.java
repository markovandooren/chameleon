package chameleon.core.expression;

import chameleon.core.declaration.QualifiedName;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.reference.CrossReference;
import chameleon.core.reference.SpecificReference;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.exception.ChameleonProgrammerException;

public class CrossReferenceTarget<D extends TargetDeclaration> extends SpecificReference<CrossReferenceTarget<D>, Element, D> implements InvocationTarget<CrossReferenceTarget<D>, Element> {

	public CrossReferenceTarget(CrossReference<?, ?, ? extends TargetDeclaration> target, SimpleNameSignature signature, Class<D> specificClass) {
		super(target, signature, specificClass);
	}

	public CrossReferenceTarget(CrossReference<?, ?, ? extends TargetDeclaration> target, String name, Class<D> specificClass) {
		super(target, name, specificClass);
	}

	public CrossReferenceTarget(QualifiedName<?, ?> name, Class<D> specificClass) {
		super(name, specificClass);
	}

	public CrossReferenceTarget(String fqn, Class<D> specificClass) {
		super(fqn, specificClass);
	}

	public LookupStrategy targetContext() throws LookupException {
		return getElement().targetContext();
	}

	public CheckedExceptionList getAbsCEL() throws LookupException {
		throw new ChameleonProgrammerException("Must rework exception handling");
	}

	public CheckedExceptionList getCEL() throws LookupException {
		throw new ChameleonProgrammerException("Must rework exception handling");
	}

	@Override
	public CrossReferenceTarget<D> clone() {
		CrossReference<?, ?, ? extends TargetDeclaration> target = getTarget();
		CrossReference<?, ?, ? extends TargetDeclaration> clone = (target != null ? target.clone() : null);
		return new CrossReferenceTarget<D>(clone, (SimpleNameSignature)signature().clone(), specificType());
	}


}

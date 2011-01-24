/**
 * 
 */
package chameleon.core.lookup;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.relation.WeakPartialOrder;
import chameleon.exception.ModelException;

public abstract class SelectorWithoutOrder<D extends Declaration> extends TwoPhaseDeclarationSelector<D> {
	
	public SelectorWithoutOrder(Class<D> selectedClass) {
		_class = selectedClass;
	}
	
	public abstract Signature signature();
	
	@Override
	public boolean selectedRegardlessOfName(D declaration) throws LookupException {
		return true;
	}

	@Override
	public WeakPartialOrder<D> order() {
		return new EqualityOrder<D>();
	}

	private Class<D> _class;
	
	@Override
	public Class<D> selectedClass() {
		return _class;
	}
	
	public static class EqualityOrder<D> extends WeakPartialOrder<D> {
		@Override
		public boolean contains(D first, D second) throws LookupException {
			return first.equals(second);
		}
	}

	public static interface SignatureSelector {
		public Signature signature();
	}

	@Override
	public boolean selectedBasedOnName(Signature signature) throws LookupException {
		return signature!=null && signature.sameAs(signature());
	}

	@Override
	public String selectionName(DeclarationContainer container) {
		return signature().name();
	}

}
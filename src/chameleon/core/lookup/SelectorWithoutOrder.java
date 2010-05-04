/**
 * 
 */
package chameleon.core.lookup;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.relation.WeakPartialOrder;
import chameleon.exception.ModelException;

public class SelectorWithoutOrder<D extends Declaration> extends DeclarationSelector<D> {
	
	public SelectorWithoutOrder(SignatureSelector selector, Class<D> selectedClass) {
		_selector = selector;
		_class = selectedClass;
	}
	
	private SignatureSelector _selector;
	
	public Signature signature() {
		return _selector.signature();
	}
	
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
	public String selectionName() {
		return signature().name();
	}

}
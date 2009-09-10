/**
 * 
 */
package chameleon.core.lookup;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.relation.WeakPartialOrder;

public class SelectorWithoutOrder<D extends Declaration> extends DeclarationSelector<D> {
	
	public SelectorWithoutOrder(SignatureSelector selector, Class<D> selectedClass) {
		_selector = selector;
		_class = selectedClass;
	}
	
	private SignatureSelector _selector;
	
	public Signature signature() {
		return _selector.signature();
	}
	
	@Override @SuppressWarnings("unchecked")
	public D filter(Declaration declaration) throws LookupException {
		Signature sig = declaration.signature();
		D result = null;
		if((selectedClass().isInstance(declaration)) && 
			 (sig.sameAs(signature()))) {
			  result = (D) declaration;
		}
		return result;
	}

	@Override
	public WeakPartialOrder<D> order() {
		return new WeakPartialOrder<D>() {

			@Override
			public boolean contains(D first, D second) throws LookupException {
				return first.equals(second);
			}
			
		};
	}

	private Class<D> _class;
	
	@Override
	public Class<D> selectedClass() {
		return _class;
	}
	
	public static interface SignatureSelector {
		public Signature signature();
	}
}
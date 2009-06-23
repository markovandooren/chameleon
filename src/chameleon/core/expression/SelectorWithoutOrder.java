/**
 * 
 */
package chameleon.core.expression;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationSelector;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.relation.WeakPartialOrder;

public class SelectorWithoutOrder<D extends Declaration> extends DeclarationSelector<D> {
	
	public SelectorWithoutOrder(Signature sig, Class<D> selectedClass) {
		_signature = sig;
		_class = selectedClass;
	}
	
	private Signature _signature;
	
	public Signature signature() {
		return _signature;
	}
	
	@Override
	public D filter(Declaration declaration) throws MetamodelException {
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
			public boolean contains(D first, D second) throws MetamodelException {
				return first.equals(second);
			}
			
		};
	}

	private Class<D> _class;
	
	@Override
	public Class<D> selectedClass() {
		return _class;
	}
}
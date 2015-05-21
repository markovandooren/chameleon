package org.aikodi.chameleon.oo.lookup;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.lookup.TwoPhaseDeclarationSelector;
import org.aikodi.chameleon.core.relation.WeakPartialOrder;
import org.aikodi.chameleon.oo.member.MoreSpecificTypesOrder;
import org.aikodi.chameleon.oo.member.SignatureWithParameters;
import org.aikodi.chameleon.oo.type.Type;

public abstract class SimpleNameCrossReferenceWithArgumentsSelector<D extends Declaration>
		extends TwoPhaseDeclarationSelector<D> {
	
	@Override
	public boolean isGreedy() {
		return false;
	}

	@Override
	public boolean selectedRegardlessOfName(D declaration)
			throws LookupException {
		boolean result = false;
		Signature signature = declaration.signature();
		if (signature instanceof SignatureWithParameters) {
		   SignatureWithParameters sig = (SignatureWithParameters) signature;
			if (sig.nbTypeReferences() == nbActualParameters()) {
				List<Type> actuals = getActualParameterTypes();
				List<Type> formals = sig.parameterTypes();
				result = MoreSpecificTypesOrder.create().contains(actuals, formals);
			} else {
				result = false;
			}
		}

		return result;
	}

	@Override
	public String selectionName(DeclarationContainer container) {
		return name();
	}

	@Override
	public boolean selectedBasedOnName(Signature signature)
			throws LookupException {
		boolean result = false;
		if (signature instanceof SignatureWithParameters) {
		   SignatureWithParameters sig = (SignatureWithParameters) signature;
			result = sig.name().equals(name()); // (_nameHash == sig.nameHash())
												// &&
		}
		return result;
	}

	@Override
	protected void applyOrder(List<SelectionResult<D>> tmp) throws LookupException {
		order().removeBiggerElements(tmp);
	}
	
	public WeakPartialOrder<SelectionResult<D>> order() {
		return new WeakPartialOrder<SelectionResult<D>>() {
			@Override
			public boolean contains(SelectionResult<D> first, SelectionResult<D> second) throws LookupException {
				return MoreSpecificTypesOrder
						.create()
						.contains(
								((SignatureWithParameters) first.finalDeclaration().signature()).parameterTypes(),
								((SignatureWithParameters) second.finalDeclaration().signature()).parameterTypes());
			}
		};
	}
	
	public abstract String name();

	public abstract int nbActualParameters();

	public abstract List<Type> getActualParameterTypes() throws LookupException;
}

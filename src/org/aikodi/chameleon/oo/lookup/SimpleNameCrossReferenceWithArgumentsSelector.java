package org.aikodi.chameleon.oo.lookup;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.lookup.TwoPhaseDeclarationSelector;
import org.aikodi.chameleon.core.relation.WeakPartialOrder;
import org.aikodi.chameleon.oo.member.DeclarationWithParametersSignature;
import org.aikodi.chameleon.oo.member.MoreSpecificTypesOrder;
import org.aikodi.chameleon.oo.member.SimpleNameDeclarationWithParametersSignature;
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
		if (signature instanceof SimpleNameDeclarationWithParametersSignature) {
			SimpleNameDeclarationWithParametersSignature sig = (SimpleNameDeclarationWithParametersSignature) signature;
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
		if (signature instanceof SimpleNameDeclarationWithParametersSignature) {
			SimpleNameDeclarationWithParametersSignature sig = (SimpleNameDeclarationWithParametersSignature) signature;
			result = sig.name().equals(name()); // (_nameHash == sig.nameHash())
												// &&
		}
		return result;
	}

	@Override
	protected void applyOrder(List<SelectionResult> tmp) throws LookupException {
		order().removeBiggerElements(tmp);
	}
	
	public WeakPartialOrder<SelectionResult> order() {
		return new WeakPartialOrder<SelectionResult>() {
			@Override
			public boolean contains(SelectionResult first, SelectionResult second) throws LookupException {
				return MoreSpecificTypesOrder
						.create()
						.contains(
								((DeclarationWithParametersSignature) first.finalDeclaration().signature()).parameterTypes(),
								((DeclarationWithParametersSignature) second.finalDeclaration().signature()).parameterTypes());
			}
		};
	}
	
	public abstract String name();

	public abstract int nbActualParameters();

	public abstract List<Type> getActualParameterTypes() throws LookupException;
}

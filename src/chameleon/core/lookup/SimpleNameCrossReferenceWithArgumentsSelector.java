package chameleon.core.lookup;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.DeclarationWithParametersHeader;
import chameleon.core.declaration.DeclarationWithParametersSignature;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameDeclarationWithParametersSignature;
import chameleon.core.member.MoreSpecificTypesOrder;
import chameleon.core.relation.WeakPartialOrder;
import chameleon.oo.type.Type;

public abstract class SimpleNameCrossReferenceWithArgumentsSelector<D extends Declaration>
		extends TwoPhaseDeclarationSelector<D> {
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
				result = MoreSpecificTypesOrder.create().contains(actuals,
						formals);
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
	public WeakPartialOrder<D> order() {
		return new WeakPartialOrder<D>() {
			@Override
			public boolean contains(D first, D second) throws LookupException {
				return MoreSpecificTypesOrder
						.create()
						.contains(
								((DeclarationWithParametersSignature) first.signature()).parameterTypes(),
								((DeclarationWithParametersSignature) second.signature()).parameterTypes());
			}
		};
	}
	
	public abstract String name();

	public abstract int nbActualParameters();

	public abstract List<Type> getActualParameterTypes() throws LookupException;
}

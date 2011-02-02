package chameleon.core.declaration;

import chameleon.core.element.Element;

public interface DeclarationWithHeader<	E extends DeclarationWithHeader<E, S, D, H>, 
										S extends DeclarationWithParametersSignature, 
										D extends Declaration, 
										H extends DeclarationWithParametersHeader> extends Declaration<E, S, D> {
	public H header();
}

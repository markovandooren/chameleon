package chameleon.core.declaration;

import chameleon.core.element.Element;

public interface DeclarationWithHeader<	E extends DeclarationWithHeader<E, P, S, D, H>, 
										P extends Element, 
										S extends DeclarationWithParametersSignature, 
										D extends Declaration, 
										H extends DeclarationWithParametersHeader> extends Declaration<E, P, S, D> {
	public H header();
}

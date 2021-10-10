package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.util.association.Multi;

import java.util.List;

public class BlockFixer extends TypeParameterFixer {
	@Override
	public TypeParameterFixer cloneSelf() {
		return new BlockFixer();
	}

	private Multi<TypeParameter> _parameters = new Multi<TypeParameter>(this);

	protected List<TypeParameter> parameters() {
		return _parameters.getOtherEnds();
	}

	public void add(TypeParameter parameter) {
		add(_parameters,parameter);
	}

	public void remove(TypeParameter parameter) {
		remove(_parameters,parameter);
	}
}
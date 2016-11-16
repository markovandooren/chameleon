package org.aikodi.chameleon.oo.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.Util;
import org.aikodi.rejuse.association.Association;
import org.aikodi.rejuse.association.SingleAssociation;

public class TypeParameterSubstitution {

	public TypeParameterSubstitution(AbstractInstantiatedTypeParameter param, List<? extends CrossReference> targets) {
		_param = param;
		_targets = new ArrayList<CrossReference>(targets);
	}

	private AbstractInstantiatedTypeParameter _param;
	
	public AbstractInstantiatedTypeParameter parameter() {
		return _param;
	}
	
	public List<CrossReference> targets() {
		return new ArrayList<CrossReference>(_targets);
	}
	
	public void apply() {
		for(CrossReference cref: _targets) {
			SingleAssociation parentLink = cref.parentLink();
			Association childLink = parentLink.getOtherRelation();
			TypeReference namedTargetExpression = Util.clone(parameter().argument().substitutionReference());
			childLink.replace(parentLink, namedTargetExpression.parentLink());
		}
	}
	
	private List<CrossReference> _targets;
}

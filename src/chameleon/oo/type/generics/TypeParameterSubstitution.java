package chameleon.oo.type.generics;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import chameleon.core.reference.CrossReference;
import chameleon.oo.type.TypeReference;

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
			TypeReference namedTargetExpression = parameter().argument().substitutionReference().clone();
			childLink.replace(parentLink, namedTargetExpression.parentLink());
		}
	}
	
	private List<CrossReference> _targets;
}

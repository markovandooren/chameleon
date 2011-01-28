package chameleon.core.member;

import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;

public class OverridesRelationSelector<M extends Member> extends MemberRelationSelector<M> {

	public OverridesRelationSelector(Class<M> kind, M member, OverridesRelation<M> relation) {
		super(kind,member);
		_overridesSelector = relation;
	}

	public OverridesRelation<M> overridesSelector() {
		return _overridesSelector;
	}
	
	private OverridesRelation<M> _overridesSelector;
	
	@Override
	public boolean selectedBasedOnName(Signature signature) throws LookupException {
		return overridesSelector().containsBasedOnName(declaration().signature(), signature);
	}

	@Override
	public boolean selectedRegardlessOfName(M declaration) throws LookupException {
		return overridesSelector().containsBasedOnRest(declaration(), declaration);
	}

}
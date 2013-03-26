package be.kuleuven.cs.distrinet.chameleon.oo.member;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public class OverridesRelationSelector<M extends Member> extends MemberRelationSelector<M> {

	public OverridesRelationSelector(Class<M> kind, M member, OverridesRelation<M> relation) {
		super(kind,member,relation);
	}

}

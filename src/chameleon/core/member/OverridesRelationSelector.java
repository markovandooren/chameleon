package chameleon.core.member;

import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;

public class OverridesRelationSelector<M extends Member> extends MemberRelationSelector<M> {

	public OverridesRelationSelector(Class<M> kind, M member, OverridesRelation<M> relation) {
		super(kind,member,relation);
	}

}
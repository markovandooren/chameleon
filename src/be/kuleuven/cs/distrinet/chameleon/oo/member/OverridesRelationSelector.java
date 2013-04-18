package be.kuleuven.cs.distrinet.chameleon.oo.member;


public class OverridesRelationSelector<M extends Member> extends MemberRelationSelector<M> {

	public OverridesRelationSelector(Class<M> kind, M member, OverridesRelation<M> relation) {
		super(kind,member,relation);
	}

}

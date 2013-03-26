package be.kuleuven.cs.distrinet.chameleon.oo.variable;

import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.member.MemberRelationSelector;

public interface MemberVariable extends Variable, Member {

  public MemberRelationSelector<Member> aliasSelector();

  public abstract MemberVariable clone();
	
}

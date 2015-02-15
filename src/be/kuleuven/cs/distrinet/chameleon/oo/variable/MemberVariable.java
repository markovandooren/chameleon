package be.kuleuven.cs.distrinet.chameleon.oo.variable;

import be.kuleuven.cs.distrinet.chameleon.core.variable.Variable;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.member.MemberRelationSelector;

public interface MemberVariable extends Variable, Member {

  @Override
public MemberRelationSelector<Member> aliasSelector();

}

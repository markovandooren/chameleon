package org.aikodi.chameleon.oo.variable;

import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.oo.member.Member;
import org.aikodi.chameleon.oo.member.MemberRelationSelector;

public interface MemberVariable extends Variable, Member {

  @Override
public MemberRelationSelector<Member> aliasSelector();

}

package chameleon.oo.variable;

import chameleon.oo.member.Member;
import chameleon.oo.member.MemberRelationSelector;

public interface MemberVariable extends Variable, Member {

  public MemberRelationSelector<Member> aliasSelector();

  public abstract MemberVariable clone();
	
}
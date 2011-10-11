package chameleon.oo.variable;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.oo.member.Member;
import chameleon.oo.member.MemberRelationSelector;

public interface MemberVariable<E extends MemberVariable<E>> 
       extends Variable<E>, Member<E,SimpleNameSignature> {

  public MemberRelationSelector<? extends Member> aliasSelector();

	
}
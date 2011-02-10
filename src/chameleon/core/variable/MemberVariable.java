package chameleon.core.variable;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.member.Member;
import chameleon.core.member.MemberRelationSelector;

public interface MemberVariable<E extends MemberVariable<E>> 
       extends Variable<E,MemberVariable>, Member<E,SimpleNameSignature,MemberVariable> {

  public MemberRelationSelector<? extends Member> aliasSelector();

	
}
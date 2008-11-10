package chameleon.core.variable;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.member.Member;
import chameleon.core.type.Type;

public interface MemberVariable<E extends MemberVariable<E>> 
       extends Variable<E,Type>, Member<E,Type,SimpleNameSignature,MemberVariable> {

}
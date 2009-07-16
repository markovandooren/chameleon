package chameleon.core.variable;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.member.Member;

public interface MemberVariable<E extends MemberVariable<E>> 
       extends Variable<E,DeclarationContainer,MemberVariable>, Member<E,DeclarationContainer,SimpleNameSignature,MemberVariable> {

}
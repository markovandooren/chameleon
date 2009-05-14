package chameleon.core.member;

import org.rejuse.association.Reference;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;

public abstract class FixedSignatureMember<E extends MemberImpl<E,P,S,F>,P extends DeclarationContainer, S extends Signature, F extends Member> extends MemberImpl<E,P,S,F> {
  public void setSignature(S signature) {
    if(signature != null) {
      _signature.connectTo(signature.parentLink());
    } else {
      _signature.connectTo(null);
    }
  }
  
  /**
   * Return the signature of this member.
   */
  public S signature() {
    return _signature.getOtherEnd();
  }
  
  private Reference<Member, S> _signature = new Reference<Member, S>(this);

}

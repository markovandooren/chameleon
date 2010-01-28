package chameleon.core.member;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;

public abstract class FixedSignatureMember<E extends FixedSignatureMember<E,P,S,F>,P extends Element, S extends Signature, F extends Member> extends MemberImpl<E,Element,S,F> {
	
	public FixedSignatureMember() {
		
	}
	
	public FixedSignatureMember(S signature) {
	  setSignature(signature);
	}
	
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
  
  private SingleAssociation<Member, S> _signature = new SingleAssociation<Member, S>(this);

}

package chameleon.core.member;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.exception.ChameleonProgrammerException;

public abstract class FixedSignatureMember<E extends Member<E,Element,S,F>,P extends Element, S extends Signature, F extends Member> extends MemberImpl<E,Element,S,F> {
	
	public FixedSignatureMember() {
		
	}
	
	public FixedSignatureMember(S signature) {
	  setSignature(signature);
	}
	
	public abstract Class<S> signatureType();
	
  public void setSignature(Signature signature) {
  	if(signatureType().isInstance(signature)) {
  		if(signature != null) {
  			_signature.connectTo(signature.parentLink());
  		} else {
  			_signature.connectTo(null);
  		}
  	} else {
  		throw new ChameleonProgrammerException("Setting wrong type of signature. Provided: "+(signature == null ? null :signature.getClass().getName())+" Expected "+signatureType().getName());
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

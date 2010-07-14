package chameleon.core.member;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.util.Util;

public abstract class FixedSignatureMember<E extends Member<E,P,S,F>,P extends Element, S extends Signature, F extends Member> extends MemberImpl<E,P,S,F> {
	
	public FixedSignatureMember() {
		
	}
	
	public FixedSignatureMember(S signature) {
	  setSignature(signature);
	}
	
	public abstract Class<S> signatureType();
	
  public void setSignature(Signature signature) {
  	if(signatureType().isInstance(signature)) {
  			_signature.connectTo(signature.parentLink());
  	} else if(signature == null) {
			_signature.connectTo(null);
  	}
    else {
  		throw new ChameleonProgrammerException("Setting wrong type of signature. Provided: "+(signature == null ? null :signature.getClass().getName())+" Expected "+signatureType().getName());
  	}
  }
  
  public List<Element> children() {
    List<Element> result = super.children();
    Util.addNonNull(signature(), result);
    return result;
  }
  
  /**
   * Return the signature of this member.
   */
  public S signature() {
    return _signature.getOtherEnd();
  }
  
  private SingleAssociation<Member, S> _signature = new SingleAssociation<Member, S>(this);

}

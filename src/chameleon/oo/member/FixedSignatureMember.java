package chameleon.oo.member;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.util.Util;

public abstract class FixedSignatureMember extends MemberImpl {
	
	public FixedSignatureMember() {
		
	}
	
	public FixedSignatureMember(Signature signature) {
	  setSignature(signature);
	}
	
	public abstract Class<? extends Signature> signatureType();
	
  public void setSignature(Signature signature) {
  	if(signatureType().isInstance(signature) || signature == null) {
  		setAsParent(_signature,signature);
  	} else {
  		throw new ChameleonProgrammerException("Setting wrong type of signature. Provided: "+(signature == null ? null :signature.getClass().getName())+" Expected "+signatureType().getName());
  	}
  }
  
  /**
   * Return the signature of this member.
   */
  public Signature signature() {
    return _signature.getOtherEnd();
  }
  
  private SingleAssociation<Member, Signature> _signature = new SingleAssociation<Member, Signature>(this);

}

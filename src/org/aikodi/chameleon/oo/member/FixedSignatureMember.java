package org.aikodi.chameleon.oo.member;

import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.association.Single;

public abstract class FixedSignatureMember extends MemberImpl {
	
	/**
	 * Constructor for types without fixed signatures.
	 * We don't have multiple inheritance so there has
	 * to be some crap code (delegation is crap too),
	 * so in this case, the _signature field isn't used
	 * and the setters for the name and the signature
	 * should throw exceptions.
	 */
	protected FixedSignatureMember() {
	}
	
	/**
	 * Create a new member with the given signature.
	 * 
	 * @param signature The signature of the member
	 */
 /*@
   @ public behavior
   @
   @ post signature() == signature;
   @*/
	public FixedSignatureMember(Signature signature) {
	  setSignature(signature);
	}
	
	public abstract Class<? extends Signature> signatureType();
	
	/**
	 * Set the signature of this member.
	 */
 /*@
   @ public behavior
   @
   @ post signature() == signature;
   @*/
  @Override
public void setSignature(Signature signature) {
  	if(signatureType().isInstance(signature) || signature == null) {
  		set(_signature,signature);
  	} else {
  		throw new ChameleonProgrammerException("Setting wrong type of signature. Provided: "+(signature == null ? null :signature.getClass().getName())+" Expected "+signatureType().getName());
  	}
  }
  
  /**
   * Return the signature of this member.
   */
  @Override
public Signature signature() {
    return _signature.getOtherEnd();
  }
  
  private Single<Signature> _signature = new Single<Signature>(this,true);

	@Override
	public void setName(String name) {
		signature().setName(name);
	}


}

package chameleon.core.reference;

import java.util.List;

import org.apache.log4j.Logger;
import org.rejuse.association.SingleAssociation;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.util.Util;

/**
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class ElementReference<E extends CrossReference, P extends Element, S extends Signature, D extends Declaration> extends CrossReferenceImpl<E,P,D> {

	private static Logger logger = Logger.getLogger("lookup.elementreference");
	
	public Logger lookupLogger() {
		return logger;
	}
	
	public ElementReference() {
	}
	
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName().equals(name);
   @*/
	public ElementReference(Signature signature) {
		setSignature(signature);
	}
	
// /*@
//   @ public behavior
//   @
//   @ pre name != null;
//   @
//   @ post signature().equals(signature);
//   @*/
//	public ElementReference(SimpleNameSignature signature) {
//		setSignature(signature);
//	}
	

// /*@
//   @ public behavior
//   @
//   @ post \result != null;
//   @*/
//  public String getName() {
//    return signature().name();
//  }
  
  private SingleAssociation<ElementReference,Signature> _signature = new SingleAssociation<ElementReference,Signature>(this);

  /**
   * Return the signature of this element reference.
   */
  public Signature signature() {
  	return _signature.getOtherEnd();
  }
  
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName() == name;
   @*/
  public void setSignature(Signature signature) {
  	setAsParent(_signature, signature);
  }
 
  private D _cache;
  
  @Override
  public void flushLocalCache() {
  	super.flushLocalCache();
  	_cache = null;
  }
  
  protected D getCache() {
  	if(Config.cacheElementReferences() == true) {
  	  return _cache;
  	} else {
  		return null;
  	}
  }
  
  protected void setCache(D value) {
  	if(! value.isDerived()) {
    	if(Config.cacheElementReferences() == true) {
    		_cache = value;
    	}
  	} else {
  		_cache = null;
  	}
  }
  
  public List<Element> children() {
  	return Util.createNonNullList(signature());
  }
  
//  public abstract D getElement() throws LookupException;
}

package chameleon.oo.type;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;

/**
 * @author Marko van Dooren
 */
public interface TypeReference<E extends TypeReference> extends CrossReference<E,Type> {

//  public TypeReference(String fqn) {
//    super(fqn, Type.class);
//  }
//  
//  public TypeReference(CrossReference<?, ?, ? extends TargetDeclaration> target, String name) {
//    super(target, name, Type.class);
//  }
//  
//  public TypeReference(CrossReference<?, ?, ? extends TargetDeclaration> target, SimpleNameSignature signature) {
//    super(target, signature, Type.class);
//  }
//  
//  public Type getType() throws LookupException {
//  	return getElement();
//  }
//
//  public TypeReference clone() {
//  	return new TypeReference((getTarget() == null ? null : getTarget().clone()),(SimpleNameSignature)signature().clone());
//  }
  
	public Type getType() throws LookupException;
	
	public Type getElement() throws LookupException;
	
	public E clone();

	public TypeReference intersection(TypeReference other);
	
	public TypeReference intersectionDoubleDispatch(TypeReference other);
	
	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference<?> other);
}

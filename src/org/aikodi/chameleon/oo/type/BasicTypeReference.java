package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.NameReference;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marko van Dooren
 */
public class BasicTypeReference extends NameReference<Type> implements TypeReference {

	public BasicTypeReference(String fqn) {
		super(fqn, Type.class);
	}

	public BasicTypeReference(CrossReferenceTarget target, String name) {
		super(target, name, Type.class);
	}

	@Override
	public BasicTypeReference cloneSelf() {
		return new BasicTypeReference(null,name());
	}
	
	@Override
	public String toString() {
		return toString(new HashSet<>());
	}
	
	@Override
  public String toString(Set<Element> visited) {
		CrossReferenceTarget target = getTarget();
		if(target instanceof TypeReference) {
			return (target == null ? "" : ((TypeReference)target).toString(visited)+".")+name();
		} else {
			return (target == null ? "" : target.toString()+".")+name();
		}
	}
	
}

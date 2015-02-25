package org.aikodi.chameleon.core.reference;

import java.util.Collections;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.TargetDeclaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.MultiTypeSelector;
import org.aikodi.chameleon.util.Util;

import com.google.common.collect.ImmutableSet;

public class MultiTypeReference<D extends Declaration> extends ElementReference<D> {

	
	
	
	
  public MultiTypeReference(String fqn, Set<Class<? extends D>> classes, boolean recursiveLimit) {
    this(createTarget(fqn, classes, recursiveLimit), 
                   Util.getLastPart(fqn), 
                   classes);
  }

	protected static MultiTypeReference createTarget(String fqn, Set classes, boolean recursiveLimit) {
		String allButLastPart = Util.getAllButLastPart(fqn);
		if(allButLastPart == null) {
			return null;
		} else {
			return new MultiTypeReference(allButLastPart, recursiveLimit ? classes : Collections.singleton(TargetDeclaration.class), recursiveLimit);
		}
	}

  
	public MultiTypeReference(CrossReferenceTarget target, String name, Set<Class<? extends D>> classes) {
		super(name);
		setTarget(target); 
		_classes = ImmutableSet.copyOf(classes);
	}

	public MultiTypeReference(String fqn, Set<Class<? extends D>> classes) {
		this(createTarget(fqn, classes, false), 
        Util.getLastPart(fqn),classes);
	}

	@Override
	public DeclarationSelector<D> selector() {
		return new MultiTypeSelector<D>(_classes) {

			@Override
			public String name() {
				return MultiTypeReference.this.name();
			}
		};
	}

	@Override
	protected Element cloneSelf() {
		return new MultiTypeReference<D>(null,name(),_classes);
	}

	private Set<Class<? extends D>> _classes;

}

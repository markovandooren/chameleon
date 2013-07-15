package be.kuleuven.cs.distrinet.chameleon.core.reference;

import java.util.Collections;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.MultiTypeSelector;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

import com.google.common.collect.ImmutableSet;

public class MultiTypeReference<D extends Declaration> extends ElementReference<D> {

	
	
	
	
	/**
	 * Initialize a new simple reference given a fully qualified name. The name is split at every dot, and
	 * multiple objects are created to form a chain of references.
	 * @param fqn
	 * @param specificClass
	 */
	public MultiTypeReference(String fqn, Set<Class<? extends D>> classes) {
		this(fqn,classes,false);
	}

  public MultiTypeReference(String fqn, Set<Class<? extends D>> classes, boolean recursiveLimit) {
    this(createTarget(fqn, classes, recursiveLimit), 
                   new SimpleNameSignature(Util.getLastPart(fqn)), 
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
		this(target, new SimpleNameSignature(name), classes);
	}

	public MultiTypeReference(CrossReferenceTarget  target, Signature signature, Set<Class<? extends D>> classes) {
		super(signature);
		setTarget(target); 
		_classes = classes;
	}

	public MultiTypeReference(Signature signature, Set<Class<? extends D>> classes) {
		super(signature);
		_classes = ImmutableSet.copyOf(classes);
	}

	@Override
	public DeclarationSelector<D> selector() {
		return new MultiTypeSelector<D>(_classes) {

			@Override
			public Signature signature() {
				return MultiTypeReference.this.signature();
			}
		};
	}

	@Override
	protected Element cloneSelf() {
		return new MultiTypeReference<D>(null,(Signature)null,_classes);
	}

	private Set<Class<? extends D>> _classes;

}

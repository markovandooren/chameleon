package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.util.association.Single;

/**
 * A class that provides default implementations for declarations with a signature.
 * 
 * @author Marko van Dooren
 */
public abstract class BasicDeclaration extends DeclarationImpl implements Declaration {

	/**
	 * The link to the signature.
	 */
	private final Single<Signature> _signature = new Single<Signature>(this);

	/**
	 * Create a new declaration with the given name.
	 * 
	 * @param name The name of the declaration.
	 *             The name cannot be null.
	 */
	public BasicDeclaration(String name) {
		setSignature(new Name(name));
	}

	public BasicDeclaration(Signature signature) {
		setSignature(signature);
	}

	protected BasicDeclaration() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSignature(Signature signature) {
		set(_signature, signature);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Signature signature() {
		return _signature.getOtherEnd();
	}

}

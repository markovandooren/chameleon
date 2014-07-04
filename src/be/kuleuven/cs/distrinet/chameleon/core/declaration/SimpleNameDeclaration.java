package be.kuleuven.cs.distrinet.chameleon.core.declaration;

/**
 * FIXME This really isn't a good idea. The speedup isn't worth the coupling & traceability penalty.
 * @author Marko van Dooren
 *
 */
public interface SimpleNameDeclaration extends Declaration {

	public SimpleNameSignature signature();
}

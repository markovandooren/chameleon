package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.core.validation.BasicProblem;

/**
 * A problem class used to report that a signature has no name.
 * 
 * @author Marko van Dooren
 */
public class SignatureWithoutNameProblem extends BasicProblem {

  /**
   * Create a new problem that indicates that the given signature has no
   * name.
   *  
   * @param signature
   */
	public SignatureWithoutNameProblem(Signature signature) {
		super(signature, "the signature has no name");
	}

}

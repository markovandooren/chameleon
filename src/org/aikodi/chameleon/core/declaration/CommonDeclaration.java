package org.aikodi.chameleon.core.declaration;

import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.ElementWithModifiers;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.util.association.Multi;

/**
 * A convenience class for declarations with a fixed signature and optional
 * modifiers.
 * 
 * @author Marko van Dooren
 */
public abstract class CommonDeclaration extends BasicDeclaration implements ElementWithModifiers {

  /**
   * Instantiate a new common declaration without a signature.
   */
  public CommonDeclaration() {
  }

  /**
   * Instantiate a new common declaration with the given signature.
   * 
   * @param signature The signature of the declaration
   */
  public CommonDeclaration(Signature signature) {
    super(signature);
  }

}

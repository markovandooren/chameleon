package chameleon.support.statement;

import chameleon.core.namespace.NamespaceElementImpl;

/**
 * An abstract class of labels for a switch statement.
 * 
 * @author Marko van Dooren
 */

public abstract class SwitchLabel extends NamespaceElementImpl {

  public SwitchLabel() {
	}

  public abstract SwitchLabel clone();

}

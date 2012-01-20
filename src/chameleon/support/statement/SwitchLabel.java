package chameleon.support.statement;

import chameleon.core.namespace.NamespaceElementImpl;

/**
 * An abstract class of labels for a switch statement.
 * 
 * @author Marko van Dooren
 */

public abstract class SwitchLabel<E extends SwitchLabel> extends NamespaceElementImpl<E> {

  public SwitchLabel() {
	}

  public abstract E clone();

}

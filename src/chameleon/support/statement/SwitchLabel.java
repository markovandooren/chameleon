package chameleon.support.statement;

import chameleon.core.element.ElementImpl;

/**
 * An abstract class of labels for a switch statement.
 * 
 * @author Marko van Dooren
 */

public abstract class SwitchLabel extends ElementImpl {

  public SwitchLabel() {
	}

  public abstract SwitchLabel clone();

}

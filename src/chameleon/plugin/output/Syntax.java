package chameleon.plugin.output;

import chameleon.core.element.Element;
import chameleon.exception.ModelException;
import chameleon.plugin.PluginImpl;

/**
 * @author Marko van Dooren
 */
public abstract class Syntax extends PluginImpl {

  public abstract String toCode(Element element) throws ModelException;

}

package chameleon.output;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;

/**
 * @author marko
 */
public abstract class Syntax {

  public abstract String toCode(Element element) throws MetamodelException;

}

/*
 * Created on Mar 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package chameleon.core.context;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;

/**
 * @author Marko van Dooren
 */
public abstract class StubContextElement extends ElementImpl {

	public StubContextElement(LexicalContext context) {
        setContext(context);
	}

  public LexicalContext getContext(Element element) {
    return _context;
  }

  public void setContext(LexicalContext context) {
    _context = context;
  }

  private LexicalContext _context;

}

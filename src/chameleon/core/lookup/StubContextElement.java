/*
 * Created on Mar 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package chameleon.core.lookup;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;

/**
 * @author Marko van Dooren
 */
public abstract class StubContextElement extends ElementImpl {

	public StubContextElement(LexicalLookupStrategy context) {
        setContext(context);
	}

  public LexicalLookupStrategy getContext(Element element) {
    return _context;
  }

  public void setContext(LexicalLookupStrategy context) {
    _context = context;
  }

  private LexicalLookupStrategy _context;

}

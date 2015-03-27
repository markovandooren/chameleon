package org.aikodi.chameleon.core.comment;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;

/**
 * @author Marko van Dooren
 */
public class CommentBlock extends ElementImpl {

	public CommentBlock(String content) {
	  if(content == null) {
	    throw new IllegalArgumentException("The comment of a comment block cannot be null.");
	  }
		_content = content;
	}
	
  public String getContent() {
    return _content;
  }

  private final String _content;

	@Override
	public Element cloneSelf() {
		return new CommentBlock(getContent());
	}
}

package chameleon.core.comment;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class CommentBlock extends ElementImpl {

	public CommentBlock(String content) {
		_content = content;
	}
	
  public String getContent() {
    return _content;
  }

  private String _content;

	/**
	 * 
	 * @uml.property name="_parentLink"
	 * @uml.associationEnd 
	 * @uml.property name="_parentLink" multiplicity="(1 1)"
	 */
	private Single _parentLink = new Single(this);

  public SingleAssociation getParentLink() {
    return _parentLink;
  }

	@Override
	public Element clone() {
		return new CommentBlock(getContent());
	}
}

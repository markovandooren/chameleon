package chameleon.core.comment;

import org.rejuse.association.Reference;

/**
 * @author Marko van Dooren
 */
public class CommentBlock {

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
	private Reference _parentLink = new Reference(this);

  public Reference getParentLink() {
    return _parentLink;
  }
}

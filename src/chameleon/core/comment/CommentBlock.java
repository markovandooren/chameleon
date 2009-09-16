package chameleon.core.comment;

import org.rejuse.association.SingleAssociation;

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
	private SingleAssociation _parentLink = new SingleAssociation(this);

  public SingleAssociation getParentLink() {
    return _parentLink;
  }
}

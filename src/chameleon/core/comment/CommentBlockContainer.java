package chameleon.core.comment;

import org.rejuse.association.SingleAssociation;

/**
 * @author Marko van Dooren
 */

public interface CommentBlockContainer {

	/**
	 * 
	 * @uml.property name="commentLink"
	 * @uml.associationEnd 
	 * @uml.property name="commentLink" multiplicity="(0 1)"
	 */
	public SingleAssociation getCommentLink();

}

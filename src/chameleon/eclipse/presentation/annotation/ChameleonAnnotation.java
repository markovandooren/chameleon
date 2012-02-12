package chameleon.eclipse.presentation.annotation;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * ChameleonAnnotation managed by an org.eclipse.jface.text.source.IAnnotationModel.
 * This ChameleonAnnotation contains an annotation and its corresponding position  
 */
public class ChameleonAnnotation extends ProjectionAnnotation {

	//The Decorator
	private Position pos;
	
	
	//stating whether the annotation is for comments
	private boolean fIsComment;

	//the annotation where this is wrapped around
	private ProjectionAnnotation annotation;
	
	/**
	 * 
	 * @return the annotation where this is wrapped around
	 */
	public ProjectionAnnotation getAnnotation(){
		
		return annotation;
	}

	/**
	 * Creates a new ChameleonAnnotation from given annotation & position
	 * @pre	annotation != null & pos != null
	 * @param anntation
	 * 	The annotation used
	 * @param pos
	 * 	The position for the  annotation
	 * @param isComment
	 * 	Whether the annotation is for comment blocks
	 * @param isCollapsed
	 * 	Whether the annotation is collapsed from the beginning
	 * 
	 * 	
	 */
	public ChameleonAnnotation( Position pos,boolean isComment,boolean isCollapsed) {
		super(isCollapsed);
		fIsComment = isComment;
		this.pos = pos;
	}


	/**
	 * Creates a new ChameleonAnnotation from given annotation & position
	 * The annotation is not collapsed
	 * @pre	annotation != null & pos != null
	 * @param anntation
	 * 	The annotation used
	 * @param pos
	 * 	The position for the  annotation
	 * @param isComment
	 * 	Whether the annotation is for comment blocks
	 *
	 * 
	 * 	
	 */
	public ChameleonAnnotation(ProjectionAnnotation annotation, Position pos2,boolean isComment) {
		super(false);
		fIsComment = isComment;
		this.annotation = annotation;
		pos = pos2;
	}

	/**
	 * 
	 * @return the decorator of this chameleonAnnotation
	 */
	public Position getPosition() {
		return pos;
	}
	
	public boolean isComment() {
		return fIsComment;
	}
	
	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "ChameleonAnnotation:\n" + //$NON-NLS-1$
				"\telement: \t"+pos.toString()+"\n" + //$NON-NLS-1$ //$NON-NLS-2$
				"\tcollapsed: \t" + isCollapsed() + "\n" + //$NON-NLS-1$ //$NON-NLS-2$
				"\tcomment: \t" + fIsComment + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
	}



	/**
	 * Sets that annotation belongs to comment 
	 * @param isComment
	 * 		whether annotation belongs to comment or not
	 */
	public void setIsComment(boolean isComment) {
		fIsComment= isComment;
	}

}

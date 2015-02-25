package org.aikodi.chameleon.eclipse.presentation.annotation;

import java.util.Iterator;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * Provides the information to be displayed in a hover popup window which appears 
 * over the presentation area of annotations. 
 * 
 *
 */
public class ChameleonAnnotationHover implements IAnnotationHover {

	/**
	 * returns null if no matching annotation is found;
	 * else returns the text which is found in the annotation.
	 * @param sourceViewer
	 * 		The sourceViewer to be used
	 * @param lineNumber
	 * 		The lineNumber of the annotation pointed at
	 */
	@Override
   public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
		IDocument document= sourceViewer.getDocument();
		IAnnotationModel model= sourceViewer.getAnnotationModel();
		
		Iterator e= model.getAnnotationIterator();
		while (e.hasNext()) {
			try {
				Annotation annotation= (Annotation) e.next();
				Position position= model.getPosition(annotation);
				int annotationLine = document.getLineOfOffset(position.getOffset());
				if (annotationLine == lineNumber)
					return annotation.getText();
			} catch (BadLocationException e1) {
				
			}
		}
		return null;
	}


}

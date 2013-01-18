package chameleon.eclipse.editors;

import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextEvent;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A Listener for chameleon documents. Detects when changes are made, and updates the presentation 
 * accordingly
 */
public class ChameleonTextListener implements ITextListener  {

	
	//the document this listener listens to
	private EclipseDocument document;
	//the managers where the information of ht epresenation is get
	//private PresentationManager presentationManager;
	//the viewer of the document
	private ITextViewer viewer;

	public ChameleonTextListener(EclipseDocument document/*, PresentationManager presentationManager*/, ITextViewer textviewer) {
		this.document = document;
		//this.presentationManager = presentationManager;
		this.viewer = textviewer;
	}

	public void textChanged(TextEvent event) {
		// CHANGE: in Tim's later version, the offset and length are ignored
		//         The method in ChameleonDocument that takes an offset and length
		//         as arguments is commented out.
//		document.doPresentation(viewer, event.getOffset(), event.getLength());
		document.doPresentation(viewer);
	}

}

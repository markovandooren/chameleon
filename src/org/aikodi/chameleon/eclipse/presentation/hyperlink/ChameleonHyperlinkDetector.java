/**
 * Created on 16-okt-06
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hyperlink;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.eclipse.connector.EclipseEditorTag;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;

/**
 * This class will check for Hyperlinks in the ChameleonEditor.
 * 
 * @author Tim Vermeiren
 * 
 */
public class ChameleonHyperlinkDetector implements IHyperlinkDetector {

	/**
	 * Returns the ChameleonHyperlinks detected in the specified region. 
	 * Returns null if no hyperlinks found.
	 * 
	 * @param	textViewer
	 * 			The textviewer wherein the hyperlinks are being searched
	 * @param 	region
	 * 			The region covered by the mousepointer. Normally the length of region is 0,
	 * 			only if a selection is covered this will be longer.
	 */
	@Override
   public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		// Get the ChameleonDocument:
		EclipseDocument document;
		try {
			document = (EclipseDocument) textViewer.getDocument();
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}
		// print out the word covered by the mousepointer while holding CTRL:
		// System.out.println("Mouse-over word: " + document.findWord(region.getOffset()) ); // debugging
		// get the editorTag:
		EclipseEditorTag editorTag = document.getReferenceEditorTagAtRegion(region);
		if(editorTag == null) {
			return null;
		} else {
//			System.out.println("Found cross-reference taf at offset: "+editorTag.getOffset() +" length: "+editorTag.getLength());
			// get the element:
			Element element = editorTag.getElement();
			if (element instanceof CrossReference) {
				IRegion refRegion = new Region(editorTag.getOffset(), editorTag.getLength());
				ChameleonHyperlink hyperlink = new ChameleonHyperlink((CrossReference) element, refRegion, document);
				return new IHyperlink[] { hyperlink };
			} else {
				return null;
			}
		}
	}
}

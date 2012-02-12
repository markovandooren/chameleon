/**
 * Created on 16-okt-06
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.hyperlink;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;

import chameleon.core.element.Element;
import chameleon.core.reference.CrossReference;
import chameleon.eclipse.connector.EclipseEditorTag;
import chameleon.eclipse.editors.ChameleonDocument;

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
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		// Get the ChameleonDocument:
		ChameleonDocument document;
		try {
			document = (ChameleonDocument) textViewer.getDocument();
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




///**
// * Created on 16-okt-06
// * @author Tim Vermeiren
// */
//package chameleon.editor.presentation.hyperlink;
//
//import org.eclipse.jface.text.BadLocationException;
//import org.eclipse.jface.text.IDocument;
//import org.eclipse.jface.text.IRegion;
//import org.eclipse.jface.text.ITextViewer;
//import org.eclipse.jface.text.Region;
//import org.eclipse.jface.text.hyperlink.IHyperlink;
//import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
//
//import chameleon.core.Config;
//import chameleon.core.element.Element;
//import chameleon.core.reference.CrossReference;
//import chameleon.editor.connector.ChameleonEditorPosition;
//import chameleon.editor.editors.ChameleonDocument;
//
//  
//
///**
// * This class will check for Hyperlinks in the ChameleonEditor.
// * @author Tim Vermeiren
// *
// */
//public class ChameleonHyperlinkDetector implements IHyperlinkDetector {
//
//  public static boolean DEBUG;
//  static {
//  	if(Config.DEBUG) {
//  		//set to true or false to enable or disable debug info in this class
//  		DEBUG=true;
//  	} else {
//  		DEBUG=false;
//  	}
//  }
//
//	/**
//	 * returns the ChameleonHyperlinks detected in the specified region.
//	 * Returns null if no hyperlinks found.
//	 */
//	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
//		int offset = region.getOffset();
//		ChameleonDocument document;
//		try{
//			document = (ChameleonDocument)textViewer.getDocument();
//		} catch(ClassCastException e){
//			e.printStackTrace();
//			return null;
//		}
////		 get text -------------------------------
//		IRegion wordRegion = findWord(document, offset);
//		String word="ILLEGAL_LOCATION";
//		try {
//			word = document.get(wordRegion.getOffset(), wordRegion.getLength());
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//		}
//		// ----------------------------------------
//		ChameleonEditorPosition decorator = document.getReferencePositionAtRegion(region);
//		// Tag decorator = document.getDecoratorAtRegionOfType(region, refclass);
//		IHyperlink[] result = null;
//		if (decorator != null) {
//			try {
//				CrossReference element = (CrossReference) decorator.getElement();
//				
//				if(DEBUG) {
//				  System.out.println("Highlighted word: " + word);
//				  System.out.println("Element: " + element);
//				  System.out.println("Decoratorname: " + decorator.getName());
//				}
//				
//				ChameleonHyperlink hyperlink = new ChameleonHyperlink(element, new Region(decorator.getOffset(), decorator.getLength()),((ChameleonDocument)textViewer.getDocument()));
//				result = new IHyperlink[] { hyperlink };
//			} catch (ClassCastException exc) {
//				exc.printStackTrace();
//			}
//		} 
//		return result;
//	}
//	
//	/**
//	 * @see org.eclipse.jdt.internal.ui.text.JavaWordFinder
//	 */
//	public static IRegion findWord(IDocument document, int offset) {
//
//		int start= -2;
//		int end= -1;
//		
//		try {
//			int pos= offset;
//			char c;
//
//			while (pos >= 0) {
//				c= document.getChar(pos);
//				if (!Character.isJavaIdentifierPart(c))
//					break;
//				--pos;
//			}
//			start= pos;
//
//			pos= offset;
//			int length= document.getLength();
//
//			while (pos < length) {
//				c= document.getChar(pos);
//				if (!Character.isJavaIdentifierPart(c))
//					break;
//				++pos;
//			}
//			end= pos;
//
//		} catch (BadLocationException x) {
//		}
//
//		if (start >= -1 && end > -1) {
//			if (start == offset && end == offset)
//				return new Region(offset, 0);
//			else if (start == offset)
//				return new Region(start, end - start);
//			else
//				return new Region(start + 1, end - start - 1);
//		}
//
//		return null;
//	}
//	
//}

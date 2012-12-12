package chameleon.eclipse.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.swt.custom.StyleRange;

import chameleon.core.element.Element;
import chameleon.eclipse.connector.EclipseEditorTag;
import chameleon.eclipse.editors.ChameleonDocument;


/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 *
 *	The presentation manager is responsible for creating and updating
 *	a syntax coloring and folding for a single document.  This can be
 *	done by updating ranges of presentation
 *
 */
public class PresentationManager {

	/*
	 * The document where this presentationManager is used 
	 */
	private ChameleonDocument document;
	
	/*
	 * the presentation model used 
	 */
	private PresentationModel _presentationModel;

	//private ChameleonEditor editor;

	/**
	 * Creates a new presentation manager with a given document & model
	 * @param doc
	 * 	the document where this manager is to be used
	 * @param model
	 *   the supporting model for this presentationmanager
	 * @throws IllegalArgumentException
	 * 	 when model or document are not effective
	 */
	public PresentationManager(ChameleonDocument doc, PresentationModel model/*, ChameleonEditor editor*/) throws IllegalArgumentException{
		if (model == null)
			throw new IllegalArgumentException("model must be effective");
		if (doc==null)
			throw new IllegalArgumentException("document must be effective");
		
		this.document=doc;
		this._presentationModel = model;
		//this.editor = editor;
	}
	
	/**
	 * @return the textpresentation for the given document. 
	 * All styles for each of the elements are made
	 * @throws NullPointerException
	 * 		When the editor was shutdown during or preceding this method excecution
	 * 
	 */
	public TextPresentation createTextPresentation() throws NullPointerException{
		final TextPresentation pres = new TextPresentation();
		try {
			Position[] poss = document.getPositions(EclipseEditorTag.CHAMELEON_CATEGORY);
			if(poss.length > 0) {
				for (Position pos: poss) {
					EclipseEditorTag tag = (EclipseEditorTag) pos;
						// FIXME this is crap code, just pas the tag alright.
						StyleRange sr = getPresentationModel().map(tag.getOffset(), tag.getLength(), tag.getElement().getClass().getName().toLowerCase(), tag
								.getName());

						if (sr != null) {
							pres.mergeStyleRange(sr);
						}
				}
			} else {
				// Prevent the syntax coloring from throwing an IndexOutOfBoundsException
				// when the presentation style has no style range.
				OptionalColor foreground = new OptionalColor("0,0,0");
				OptionalColor background = new OptionalColor("255,255,255");
				pres.addStyleRange(new StyleRange(0, 0, foreground.getColor(), background.getColor()));
			}
		} catch (BadPositionCategoryException e) {
			e.printStackTrace();
			
		} catch (ClassCastException e) {
			e.printStackTrace();
			
		}
		return pres;
	}	

	/**
	 * checks whether the given element is foldable
	 * @param decorator 
	 * 		The decorator to be checked
	 * 		
	 */
	private boolean isFoldable(EclipseEditorTag decorator) {
		Element element = decorator.getElement();
		PresentationStyle presStyle = getPresentationModel().getRule(element.getClass().getName().toLowerCase(), decorator.getName());
		if(presStyle != null && presStyle.isfoldable())
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @return all the positions which are foldable, by definition of the XML file
	 * 		   if no foldable positions are found, then an empty Vector is returned
	 */
	public List<Position> getFoldablePositions(){
		try {
			if(document == null) {
				return new ArrayList<Position>();
			}
			Position[] poss = document.getPositions(EclipseEditorTag.CHAMELEON_CATEGORY);
			List<Position> foldablePos= new ArrayList<Position>();
 			for (int i = 0; i < poss.length; i++) {
				if(isFoldable(((EclipseEditorTag) poss[i]))){
					foldablePos.add( poss[i]);
				}
			}
 			
 			return foldablePos;
		} catch (BadPositionCategoryException e) {
			e.printStackTrace();
			return new ArrayList<Position>();
		} catch (NullPointerException npe){
			npe.printStackTrace();
			return new ArrayList<Position>();
		} 
	}
	
	/**
	 * returns all the elements that are marked to be folded from the model
	 */
	public Vector<EclipseEditorTag> getFoldedElementsFromModel() {
		try {
			Position[] poss = document.getPositions(EclipseEditorTag.CHAMELEON_CATEGORY);
			Vector<EclipseEditorTag> foldPos= new Vector<EclipseEditorTag>(0);
 			for (int i = 0; i < poss.length; i++) {
				if(isFolded(((EclipseEditorTag) poss[i]))){
					foldPos.add((EclipseEditorTag) poss[i]);
				}
			}
 			
 			return foldPos;
		} catch (BadPositionCategoryException e) {
			//When this happens, somethings wrong with the positions in the file
			e.printStackTrace();
		}
		//in case there are no foldablePositions
		return new Vector<EclipseEditorTag>(0);
	}	
	
	/**
	 * Checks whether the decorator is folded. 
	 * @param decorator
	 * 	The decorator that is being checked
	 * @return True when the decorator is folded in the editor. False otherwise
	 */
	private boolean isFolded(EclipseEditorTag decorator) {
		//FIXME Student code
		PresentationStyle presStyle = getPresentationModel().getRule(decorator.getElement().getClass().getName().toLowerCase(), decorator.getName());
		if(presStyle != null && presStyle.isFolded())
			return true;
		else
			return false;
		
		
	}

	public TextPresentation createTextPresentation(TextPresentation pres, int offset, int length) {
		try {
			System.out.println("doing presentation on " + document);
			Position[] poss = document.getPositions(EclipseEditorTag.CHAMELEON_CATEGORY);

			for (int i = 0; i < poss.length; i++) {
				if (poss[i].getOffset()+poss[i].getLength() > offset && poss[i].getOffset() < offset+length){
					EclipseEditorTag dec = (EclipseEditorTag) poss[i];
					StyleRange sr = getPresentationModel().map(dec.getOffset(),dec.getLength(),dec.getElement().getClass().getName().toLowerCase(), dec.getName());
					if (sr!=null) pres.mergeStyleRange(sr);
				}
			}
		} catch (BadPositionCategoryException e)
		{
			System.out.println(e);
			
		} catch (ClassCastException e)
		{
			System.out.println(e);
			
		}
		return pres;
	}

//	public List<String> getOutlineElements(){
//		
//		List<String[]> pm = getPresentationModel().getOutlineElements();
//		
//		List<String> v = new ArrayList<String>();
//		for (Iterator<String[]> iter = pm.iterator(); iter.hasNext();) {
//			String[] element = iter.next();
//			v.add(element[0]);
//			
//		}		
//		return v;
//	}

	public PresentationModel getPresentationModel() {
		return _presentationModel;
	}

	public List<String> getDefaultOutlineElements() {
		return getPresentationModel().getDefaultOutlineElements();
	}

	
	
}

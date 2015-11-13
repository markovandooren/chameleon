package org.aikodi.chameleon.eclipse.connector;

import java.util.Comparator;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.tag.Metadata;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Position;

import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

/**
 * A tag intended for linking a position to a model element.
 * 
 * Eclipse registers positions under certain categories. The Chameleon positions are registered
 * under the category ChameleonEditorPosition.CHAMELEON_CATEGORY. To request the Chameleon positions, 
 * use someDocument.getPositions(ChameleonEditorPosition.CHAMELEON_CATEGORY);
 *
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * @author Marko van Dooren
 */
public class EclipseEditorTag extends Position implements Metadata {

	/**
	 * Initialize a new Chameleon editor position with the given offset and length, and
	 * with the given name.
	 * 
	 * @param offset
	 * @param length
	 * @param element
	 * @param name
	 */
	public EclipseEditorTag(EclipseDocument document, int offset, int length, Element element, String name){
		super(Math.max(0,offset),Math.max(0,length));
  	if(element == null) {
  		throw new ChameleonProgrammerException("Initializing position tag with null element.");
  	}
  	if(document == null) {
  		throw new ChameleonProgrammerException("Initializing position tag with null document.");
  	}
  	_document = document;
  	try {
			_document.addPosition(EclipseEditorTag.CHAMELEON_CATEGORY,this);
			setElement(element,name);
			setName(name);
		} catch (BadLocationException e) {
			throw new IllegalArgumentException();
//			e.printStackTrace();
		} catch (BadPositionCategoryException e) {
//			e.printStackTrace();
		}
	}
	
	@Override
   public void disconnect() {
		setElement(null,getName());
	}
	
	public void shift(int shift) {
		setOffset(getOffset() + shift);
	}
	
	private EclipseDocument _document;
	
	public EclipseDocument getDocument() {
		return _document;
	}

	/**
	 * COPIED FROM TAGIMPL BECAUSE WE INHERIT FROM POSITION
	 * 
	 * DO NOT MODIFY!!!
	 */
	
	private Element _element;
	
  @Override
public final Element getElement() {
  	return _element;
  }
  
  @Override
public void setElement(Element element, String name) {
  	if(element != _element) {
  		// Set new pointer, backup old for removal.
  		_element = element;
  		// Remove from current element.
  		if((_element != null) && (_element.metadata(name) == this)){
  			_element.removeMetadata(name);
  		}
  		// Add to new element.
  		if((_element != null) && (_element.metadata(name) != this)) {
  		  _element.setMetadata(this, name);
  		}
  	}
  	EclipseDocument oldDocument = getDocument();
		EclipseDocument newDocument = oldDocument.getProjectNature().document(element);
		_document = newDocument;
		if(newDocument != oldDocument) {
  		try {
  			if(oldDocument != null) {
  			  oldDocument.removePosition(EclipseEditorTag.CHAMELEON_CATEGORY,this);
  			}
  			if(newDocument != null) {
				  newDocument.addPosition(EclipseEditorTag.CHAMELEON_CATEGORY,this);
  			}
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch(NullPointerException e) {
				e.printStackTrace();
			} catch (BadPositionCategoryException e) {
				e.printStackTrace();
			}
		}
  }

/**
 * END COPYING
 */
	
	/**
	 * Eclipse registers positions under certain categories. The Chameleon positions are registered
	 * under the category ChameleonEditorPosition.CHAMELEON_CATEGORY.
	 */
	public final static String CHAMELEON_CATEGORY= "__ChameleonDecorator";

	private String _name;

	private void setName(String name) {
		_name = name;
	}
	
	/**
	 * Return the name of this editor tag. The name represents the meaning of the location.
	 * @return
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Check whether this editor tag is of the given kind.
	 * @param kind The kind to be checked.
	 */
 /*@
   @ public behavior
   @
   @ pre kind != null;
   @
   @ post \result == getName().equals(kind);
   @*/
	public boolean is(String kind) {
		return getName().equals(kind);
	}
	
	/** Decorator spanning an entire element **/
	
	@Override
   public String toString(){
		return "Offset : "+getOffset()+"\tLength : "+getLength()+"\tElement : "+getElement();
	}


	// FIXME Tim wrote a hashCode() method, but no equals. Do we need the hashCode method?
	
	/**
	 * Position overrides the hashCode method and does not take
	 * the name and the element into account.
	 * @post	The hashcode will be different for editortags with a different position
	 * or a different name or a different element.
	 */
//	@Override
//	public int hashCode() {
//		return super.hashCode() ^ _name.hashCode() ^ _element.hashCode();
//	}

	
	/**
	 * This comparator will compare editorTags and they will be sorted (ascending) by:
	 * - length
	 * - offset
	 * - hashCode
	 * 
	 * So the shortest EditorTags will come first. if the length is the same, 
	 * the EditorTag with the smallest offset comes first and if they are still the same
	 * the EditorTag with the smallest hashCode comes first.
	 * 
	 * @author Tim Vermeiren
	 */
	public final static Comparator<EclipseEditorTag> lengthComparator = new Comparator<EclipseEditorTag>() {
		@Override
      public int compare(EclipseEditorTag t1, EclipseEditorTag t2) {
			// compare by length:
			int compare = new Integer(t1.getLength()).compareTo(t2.getLength());
			// if no difference found
			if(compare!=0)
				return compare;
			// compare by offset:
			compare = new Integer(t1.getOffset()).compareTo(t2.getOffset());
			// if still the same
			if(compare!=0)
				return compare;
			// compare by hashCode:
			return new Integer(t1.hashCode()).compareTo(t2.hashCode());
		}
	};
	
	/**
	 * This comparator will compare editorTags and they will be sorted (ascending) by:
	 * - offset
	 * - length
	 * - hashCode
	 * 
	 * So the EditorTags with the lowest begin-offset will come first. if this is the same, 
	 * the smallest EditorTag comes first and if they are still the same
	 * the EditorTag with the smallest hashCode comes first.
	 * 
	 * @author Tim Vermeiren
	 */
	public final static Comparator<EclipseEditorTag> beginoffsetComparator = new Comparator<EclipseEditorTag>() {
		@Override
      public int compare(EclipseEditorTag t1, EclipseEditorTag t2) {
			// 1) compare by offset:
			int compare = new Integer(t1.getOffset()).compareTo(t2.getOffset());
			// if no difference found
			if(compare==0) {
				// 2) compare by length:
				compare = new Integer(t1.getLength()).compareTo(t2.getLength());
			}
			// if still the same
			if(compare==0) {
				// 3) compare by tag name:
				compare = t1.getName().compareTo(t2.getName());
			}
			// if still the same
			if(compare==0) {
				// 4) order does not matter. MUST NOT RETURN 0, that would remove the tag in the treeset.
				compare = -1;
			}
			return compare;
		}
	};

	
	
	/**
	 * Predicate that checks wheter an EditorTag has a given tagname
	 */
	public static class NamePredicate extends SafePredicate<EclipseEditorTag>{
		private String _tagName;
		/**
		 * @param 	tagName
		 * 			Must be a constant of EditorTagTypes
		 */
		public NamePredicate(String tagName) {
			this._tagName = tagName;
		}
		@Override
		public boolean eval(EclipseEditorTag tag) {
			return tag.getName().equals(_tagName);
		}
	}



	/**
	 * Predicate that checks wheter an EditorTag has a given tagname
	 */
	public static class SurroundsOffsetPredicate extends SafePredicate<EclipseEditorTag>{
		private int offset;
		public SurroundsOffsetPredicate(int offset) {
			this.offset = offset;
		}
		@Override
		public boolean eval(EclipseEditorTag editorTag) {
			return editorTag.includes(offset);
		}
	}
	

}

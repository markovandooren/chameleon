/*
 * Created on 17-okt-06
 *
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.hyperlink;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.ChameleonEditor;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.EclipseDocument;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;

/**
 * The Hyperlink of a cross reference element in the editor. When clicked, it will open
 * the document of the element referenced by the cross reference.
 * 
 * @author Tim Vermeiren
 * @author Marko van Dooren
 */
public class ChameleonHyperlink implements IHyperlink {
	
	/**
	 * The cross reference in the model.
	 */
	CrossReference _element;
	
	IRegion _region;
	
	EclipseDocument _document;
	
	public ChameleonHyperlink(CrossReference element, IRegion region,EclipseDocument document){
		_element = element;
		_region = region;
		_document = document;
	}
	
	@Override
   public String getHyperlinkText() {
		return getReference().toString();
	}
	
	public EclipseDocument document() {
		return _document;
	}
	
	public CrossReference getReference(){
		return this._element;
	}
	
	public Declaration getDeclarator() throws ModelException {
		return getReference().getDeclarator();
	}
	
	@Override
   public String getTypeLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * Status of a hyperlink that refers to a valid element with a valid compilationunit
	 */
	public static final String VALID_STATUS = "valid";
	/**
	 * Status of a hyperlink refering to a valid element in the metamodel
	 * whitout a valid document or compilationunit
	 * eg.: double in java is valid but had no CU assosiated with it
	 */
	public static final String SEMIVALID_STATUS = "semivalid";
	
	/**
	 * Status of a hyperlink that doesn't refer to a valid element 
	 */
	public static final String INVALID_STATUS = "invalid";
	
	/**
	 * Returns the status of this hyperlink
	 * 
	 * @return See the static status constants of this class
	 */
	public String getStatus(){
		try {
			// perform lookup:
			Declaration refElement = getDeclarator();
			if(refElement==null)
				return INVALID_STATUS;
			// check whether the compilationUnit and the document are found:
			Document refCU = refElement.nearestAncestor(Document.class);
			EclipseDocument refDoc = _document.getProjectNature().document(refCU);
			if(refDoc != null){
				return VALID_STATUS;
			} else {
				return SEMIVALID_STATUS;
			}
		} catch (Exception e) {
			// System.err.println(e.getMessage());
			// e.printStackTrace();
			return INVALID_STATUS;
		}
	}

	
	@Override
   public void open() {
		try {
			Declaration referencedElement = getDeclarator();
			if (referencedElement != null) {
				ChameleonEditor.showInEditor(referencedElement, true, true, null);
			}
		} catch (ModelException exc) {
			// We don't do anything if the user decides to click on an unresolvable cross-reference.
		}
	}

	@Override
   public IRegion getHyperlinkRegion() {
		return _region;
	}

}

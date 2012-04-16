/*
 * Created on 17-okt-06
 *
 */
package chameleon.eclipse.presentation.hyperlink;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;

import chameleon.core.declaration.Declaration;
import chameleon.core.document.Document;
import chameleon.core.reference.CrossReference;
import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.editors.ChameleonEditor;
import chameleon.exception.ModelException;

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
	
	ChameleonDocument _document;
	
	public ChameleonHyperlink(CrossReference element, IRegion region,ChameleonDocument document){
		_element = element;
		_region = region;
		_document = document;
	}
	
	public String getHyperlinkText() {
		return getReference().toString();
	}
	
	public ChameleonDocument document() {
		return _document;
	}
	
	public CrossReference getReference(){
		return this._element;
	}
	
	public Declaration getDeclarator() throws ModelException {
		return getReference().getDeclarator();
	}
	
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
			// check wheter the compilationUnit and the document are found:
			Document refCU = refElement.nearestAncestor(Document.class);
			ChameleonDocument refDoc = _document.getProjectNature().document(refCU);
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

	
	public void open() {
		try {
			Declaration referencedElement = getDeclarator();
			if (referencedElement != null) {
				ChameleonEditor.showInEditor(referencedElement, true, true, null);
			}
//				System.out.println("De link wordt geopend...");
//				CompilationUnit cu = referencedElement.nearestAncestor(CompilationUnit.class);
//				ChameleonDocument doc = document().getProjectNature().document(cu);
//				if(doc == null) {
//					System.out.println("Document of referenced object is null");
//				}
//
//				IWorkbench wb = PlatformUI.getWorkbench();
//				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
//				IWorkbenchPage page = win.getActivePage();
//				IFile file = doc.getFile();
//				
//				//create Marker to jump immediately to the referenced element.
//				ChameleonEditorPosition dec = (ChameleonEditorPosition) referencedElement.tag(ChameleonEditorPosition.ALL_TAG);
//				IMarker marker = null;
//				
//				try {
//					int lineNumber = 0;
//					if(dec != null) {
//						int offset = dec.getOffset();
//						System.out.println("Offset: "+offset);
//						lineNumber = doc.getLineOfOffset(offset);
//					}
////					 HashMap map = new HashMap();
////					 map.put(IMarker.LINE_NUMBER, new Integer(lineNumber));
//					 marker = file.createMarker(IMarker.TEXT);
//					 marker.setAttribute(IMarker.LINE_NUMBER,lineNumber);
//				} catch (BadLocationException exc) {
//					System.out.println("Could not calculate line number of declaration of referenced element when opening a Chameleon hyperlink.");
//					exc.printStackTrace();
//				} catch (CoreException exc) {
//					System.out.println("Could not create text marker for referenced element when opening a Chameleon hyperlink.");
//					exc.printStackTrace();
//				}
//				// map.put(IWorkbenchPage.EDITOR_ID_ATTR,"org.eclipse.ui.DefaultTextEditor");
//				// IMarker marker = file.createMarker(IMarker.TEXT);
//				// marker.setAttributes(map);
//				// page.openEditor(marker); //2.1 API
//				try {
//					if(marker == null) {
//					  IDE.openEditor(page, file);
//					} else {
//						IDE.openEditor(page, marker);
//					}
//				} catch (PartInitException e) {
//					e.printStackTrace();
//				}
//				// marker.delete();
//			}
		} catch (ModelException exc) {
//			exc.printStackTrace();
			System.out.println("Referenced element not found.");
		}
	}

	public IRegion getHyperlinkRegion() {
		return _region;
	}

}

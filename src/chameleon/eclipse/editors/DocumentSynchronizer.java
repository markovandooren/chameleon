/*
 * Created on 8-okt-2004
 *
 */
package chameleon.eclipse.editors;

import org.eclipse.jface.text.IDocument;

import chameleon.eclipse.connector.EclipseEditorTag;
import chameleon.oo.method.Method;
import chameleon.oo.type.Type;


/**
 * @author Jef Geerinckx
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A class used to synchronize a ChameleonDocument.
 * It detects when the document is changed and reacts accordingly
 */
public class DocumentSynchronizer {
	
	private ChameleonDocument _document; // the document to sync
	
	public DocumentSynchronizer(ChameleonDocument document){
		_document = document;
	}
	
	/**
	 * Triggered when a type name is changed. 
	 * All necessary changes are made.
	 */
	public void onTypeNameChangedEvent(Type type){
		System.out.println("Synchroniser : Detected Type Name Change");
		EclipseEditorTag dec = (EclipseEditorTag)type.tag(EclipseEditorTag.NAME_TAG);
//		for (int i = 0; i < decs.length; i++) {
//			Decorator dec = (Decorator) decs[i];
			replaceInDocument(dec.getOffset(),dec.getLength(),type.getName());
//		}
	}
	
	
	/**
	 * Triggered when a variable name is changed. 
	 * All necessary changes are made.
	 */
	public void onVariableNameChangedEvent(Type type){
		System.out.println("Synchroniser : Detected Variable Name Change");
		EclipseEditorTag deco = (EclipseEditorTag) type.tag(EclipseEditorTag.ALL_TAG);
//		for (int i = 0; i < deco.length; i++) {
//			Decorator pos = (Decorator) deco[i];
			try{
				IDocument document = _document;
				document.replace(deco.getOffset(),deco.getLength(),type.getName());
			}catch(Exception exc){
			 throw new Error("replacement in document not possible!");
			}
//		}
	}
	
	
	/**
	 * Triggered when a method name is changed. 
	 * All necessary changes are made.
	 */
	public void onMethodNameChangedEvent(Method method){
		System.out.println("Synchroniser : Detected Method Name Change");
		
		EclipseEditorTag deco = (EclipseEditorTag) method.tag(EclipseEditorTag.NAME_TAG);
		//for (int i = 0; i < deco.length; i++) {
			try{
				IDocument document = _document;
				document.replace(deco.getOffset(),deco.getLength(),method.name());
			}
			catch (Exception exc) {
				   throw new Error("replacement in document not possible!");
			}
	}
	
	/**
	 * Replaces the piece of code at offset with given length by the new code 
	 * @param offset
	 * 		The offset of the old code
	 * @param length
	 * 		The length of the old code
	 * @param code
	 * 		The new code
	 */
	public void replaceInDocument(int offset, int length, String code){
		//IEditorPart editor = ChameleonEditorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IDocument document = _document; //((TextEditor)editor).getDocumentProvider().getDocument(editor.getEditorInput());
		try{
			document.replace(offset,length,code);
		}
		catch (Exception exc) {
			   throw new Error("replacement in document not possible!");
		}
	}	
	
	/**
	 * Replaces the piece of code with given startline, start column, endline and endcolumn by the new code
	 * @param startLine
	 * 		startline of the old code
	 * @param startCol
	 * 		startColumn of the old code
	 * @param endLine
	 *      endline of the old code
	 * @param endCol
	 *      endcolumn of the old code
	 * @param code
	 * 		The new code
	 */
	public void replaceInDocument(int startLine, int startCol, int endLine, int endCol , String code){
		IDocument document = _document; //((TextEditor)editor).getDocumentProvider().getDocument(editor.getEditorInput());
		try{
			int offset = document.getLineOffset(startLine-1)+startCol-1;
			int length = document.getLineOffset(endLine-1)+endCol-offset;
			document.replace(offset,length,code);
		}
		catch (Exception exc) {
			   throw new Error("replacement in document not possible!");
		}
	}
	

}


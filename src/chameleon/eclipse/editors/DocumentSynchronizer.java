/*
 * Created on 8-okt-2004
 *
 */
package chameleon.eclipse.editors;

import org.eclipse.jface.text.IDocument;


/**
 * @author Jef Geerinckx
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx
 * @author Marko van Dooren 
 * 
 * A class used to synchronize a ChameleonDocument.
 * It detects when the document is changed and reacts accordingly
 */
public class DocumentSynchronizer {
	
	private EclipseDocument _document; // the document to sync
	
	public DocumentSynchronizer(EclipseDocument document){
		_document = document;
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


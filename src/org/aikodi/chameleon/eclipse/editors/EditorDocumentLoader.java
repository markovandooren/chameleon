//package org.aikodi.chameleon.eclipse.editors;
//
//import org.aikodi.chameleon.workspace.DocumentLoader;
//import org.aikodi.chameleon.workspace.DocumentLoaderImpl;
//import org.aikodi.chameleon.workspace.InputException;
//
//public class EditorDocumentLoader extends DocumentLoaderImpl {
//
//	private DocumentLoader _original;
//	
//	public EditorDocumentLoader(DocumentLoader original, EclipseDocument editorDocument) throws InputException {
//		if(original == null) {
//			throw new IllegalArgumentException();
//		}
//		_original = original;
//		takeOver(original);
//	}
//	
//	public void close() throws InputException {
//		_original.takeOver(this);
//	}
//	
//	@Override
//	protected String resourceName() {
//		return "Eclipse editor";
//	}
//
//	@Override
//	protected void doRefresh() throws InputException {
//		// This should do something. The reconciler should probably call this method instead of doing
//		// the work itself.
//	}
//
//}

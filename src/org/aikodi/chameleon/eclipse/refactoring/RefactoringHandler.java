package org.aikodi.chameleon.eclipse.refactoring;

import org.aikodi.chameleon.eclipse.editors.ChameleonEditor;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class RefactoringHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		System.out.println("*** Refactoring handler executed ***");
		return null;
	}

	public EclipseDocument document() {
		return editor().getDocument();
	}

	public ChameleonEditor editor() {
		return ChameleonEditor.getActiveEditor();
	}
	
}

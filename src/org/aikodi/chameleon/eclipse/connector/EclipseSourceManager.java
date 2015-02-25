package org.aikodi.chameleon.eclipse.connector;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.input.NoLocationException;
import org.aikodi.chameleon.input.PositionMetadata;
import org.aikodi.chameleon.input.SourceManager;
import org.aikodi.chameleon.plugin.ViewPluginImpl;
import org.eclipse.jface.text.BadLocationException;

public class EclipseSourceManager extends ViewPluginImpl implements SourceManager {

	public EclipseSourceManager(ChameleonProjectNature nature) {
		setProjectNature(nature);
	}


	public ChameleonProjectNature projectNature() {
		return _projectNature;
	}

	public void setProjectNature(ChameleonProjectNature nature) {
		_projectNature = nature;
	}

	private ChameleonProjectNature _projectNature;

	@Override
	public EclipseSourceManager clone() {
		return new EclipseSourceManager(null);
	}

	@Override
   public String text(Element element) throws NoLocationException {
		EclipseEditorTag location = (EclipseEditorTag) element.metadata(PositionMetadata.ALL);
		if(location == null) {
			throw new NoLocationException(element);
		}
		EclipseDocument document = projectNature().document(element);
		if(document == null) {
			throw new ChameleonProgrammerException("No document found for element.");
		} else {
			try {
				return document.get(location.getOffset(), location.getLength());
			} catch (BadLocationException e) {
				throw new ChameleonProgrammerException("Bad location");
			}
		}
	}

}

package be.kuleuven.cs.distrinet.chameleon.eclipse.connector;

import org.eclipse.jface.text.BadLocationException;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.EclipseDocument;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.input.NoLocationException;
import be.kuleuven.cs.distrinet.chameleon.input.PositionMetadata;
import be.kuleuven.cs.distrinet.chameleon.input.SourceManager;
import be.kuleuven.cs.distrinet.chameleon.plugin.ViewPluginImpl;

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

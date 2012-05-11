package chameleon.eclipse.connector;

import org.eclipse.jface.text.BadLocationException;

import chameleon.core.element.Element;
import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.project.ChameleonProjectNature;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.input.NoLocationException;
import chameleon.input.PositionMetadata;
import chameleon.input.SourceManager;
import chameleon.plugin.Plugin;
import chameleon.plugin.PluginImpl;

public class EclipseSourceManager extends PluginImpl implements SourceManager {

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
	public Plugin clone() {
		return new EclipseSourceManager(null);
	}

	public String text(Element element) throws NoLocationException {
		EclipseEditorTag location = (EclipseEditorTag) element.metadata(PositionMetadata.ALL);
		if(location == null) {
			throw new NoLocationException(element);
		}
		ChameleonDocument document = projectNature().document(element);
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

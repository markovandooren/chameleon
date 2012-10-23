package chameleon.eclipse.connector;

import java.util.Collection;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.rejuse.association.Association;
import org.rejuse.association.SingleAssociation;

import chameleon.core.document.Document;
import chameleon.core.element.Element;
import chameleon.core.tag.Metadata;
import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.editors.reconciler.ChameleonPresentationReconciler;
import chameleon.eclipse.project.ChameleonProjectNature;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.input.InputProcessor;
import chameleon.input.ParseException;
import chameleon.plugin.Processor;
import chameleon.plugin.ViewProcessorImpl;

public class EclipseEditorInputProcessor extends ViewProcessorImpl implements InputProcessor {

	public EclipseEditorInputProcessor(ChameleonProjectNature nature) {
		setProjectNature(nature);
	}
	
	private ChameleonProjectNature _projectNature;
	
	public ChameleonProjectNature projectNature() {
		return _projectNature;
	}
	
	public void setProjectNature(ChameleonProjectNature nature) {
		_projectNature = nature;
	}
	
	@Override
	public EclipseEditorInputProcessor clone() {
		return new EclipseEditorInputProcessor(null);
	}

	public void reportParseError(ParseException exc) {
		ChameleonDocument doc = document(exc.compilationUnit());
		if (doc!=null) {
			doc.handleParseError(exc);
		}
	}

	public ChameleonDocument document(Element element) {
		return projectNature().document(element);
	}

	public void setLocation(Element element, int offset, int length, Document compilationUnit, String tagType) {
		if(element == null) {
			throw new ChameleonProgrammerException("Trying to set decorator to a null element.");
		}
		// ECLIPSE NEEDS A +1 INCREMENT FOR THE LENGTH
		length++;
		setSingleLocation(element, offset, length, compilationUnit, tagType);
	}


	private void setSingleLocation(Element element, int offset, int length, Document document, String tagType) {
		ChameleonDocument doc = document(document);
		if(doc == null) {
			System.out.println("debug");
		} else {
			Element parent = element.parent();
			// 1. Replace element with stub in the parent
			// 2. Force the document to be an ancestors of the element
			// 3. Add metadata
			// 4. Replace stub with element in the parent to restore the original state
			SingleAssociation stub = new SingleAssociation(new Object());
			if(! element.hasMetadata(tagType)) {
				SingleAssociation parentLink = element.parentLink();
				Association childLink = parentLink.getOtherRelation();
				boolean cleanup = false;
				if(element != document) {
					cleanup = true;
					if(childLink != null) {
						childLink.replace(parentLink, stub);
					}
					// 2: We force the document to be an ancestor of the element.
					//    This is needed because adding the metadata requires additional
					//    synchronisation with the eclipse ChameleonDocument that corresponds
					//    to the Document
					element.setUniParent(document);
				}
				EclipseEditorTag dec = new EclipseEditorTag(doc,offset,length,element,tagType);
				if(cleanup) {
					element.setUniParent(null);
					if(childLink != null) {
						// The setUniParent(null) call above create a new parentLink for the element, so we
						// must obtain a new reference.
						parentLink = element.parentLink();
						childLink.replace(stub,parentLink);
					}
				}
			}
			if(element.parent() != parent) {
				throw new ChameleonProgrammerException();
			}
		}
	}

	public void markParseError(int offset, int length, String message,Element element) {
		ChameleonDocument document = document(element);
		if(document != null) {
			String header;
			int lineNumber;
			try {
				lineNumber = document.getLineOfOffset(offset);
				int offsetWithinLine = offset - document.getLineOffset(lineNumber);
				lineNumber++;
				offsetWithinLine++;
				header = "line "+lineNumber+":"+(offsetWithinLine);
			} catch (BadLocationException e) {
				header = "cannot determine position of syntax error:";
				lineNumber = 0;
			}
			//FIXME don't like that all this static code is in ChameleonPresentationReconciler.
			Map<String,Object> attributes = ChameleonPresentationReconciler.createProblemMarkerMap(header+" "+message);
			document.setProblemMarkerPosition(attributes, offset, length);
			document.addProblemMarker(attributes);
		}
	}

	public void removeLocations(Element element) {
		Collection<Metadata> tags = element.metadata(); 
		for(Metadata tag:tags) {
			if(tag instanceof EclipseEditorTag) {
				((EclipseEditorTag) tag).disconnect();
			}
		}
	}

}

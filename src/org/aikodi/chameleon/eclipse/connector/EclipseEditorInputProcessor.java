package org.aikodi.chameleon.eclipse.connector;

import java.util.Collection;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.tag.Metadata;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.input.InputProcessor;
import org.aikodi.chameleon.input.ParseProblem;
import org.aikodi.chameleon.plugin.ViewProcessorImpl;
import org.eclipse.jface.text.BadLocationException;

import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

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

	public EclipseDocument document(Element element) {
		return projectNature().document(element);
	}

	@Override
   public void setLocation(Element element, int offset, int length, Document compilationUnit, String tagType) {
		if(element == null) {
			throw new ChameleonProgrammerException("Trying to set decorator to a null element.");
		}
		// ECLIPSE NEEDS A +1 INCREMENT FOR THE LENGTH
		length++;
		setSingleLocation(element, offset, length, compilationUnit, tagType);
	}


	private void setSingleLocation(Element element, int offset, int length, Document document, String tagType) {
		EclipseDocument doc = document(document);
		if(doc != null) {
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
				try {
				EclipseEditorTag dec = new EclipseEditorTag(doc,offset,length,element,tagType);
				} catch(IllegalArgumentException exc) {
					// Setting the location failed. Ignore.
				}
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

	@Override
   public void markParseError(int offset, int length, String message, Element element) {
		EclipseDocument document = document(element);
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
			ParseProblem pb = new ParseProblem(element, header + " " + message, offset, length);
			document.markParseError(pb);
		}
	}

	@Override
   public void removeLocations(Element element) {
		Collection<Metadata> tags = element.metadata(); 
		for(Metadata tag:tags) {
			if(tag instanceof EclipseEditorTag) {
				tag.disconnect();
			}
		}
	}

}

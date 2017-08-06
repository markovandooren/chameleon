/**
 * Created on 21-mrt-07
 */
package org.aikodi.chameleon.eclipse.presentation.formatting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.eclipse.connector.EclipseEditorTag;
import org.aikodi.chameleon.eclipse.editors.ChameleonEditor;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.chameleon.eclipse.presentation.PresentationManager;
import org.aikodi.chameleon.eclipse.presentation.PresentationModel;
import org.aikodi.chameleon.input.PositionMetadata;
import org.aikodi.chameleon.util.Util;
import org.aikodi.rejuse.predicate.SafePredicate;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;

/**
 * Is used to format the document. This option can be chosen in the context
 * menu and will set the indentation correctly.
 * 
 * @author Tim Vermeiren
 */
public class ChameleonFormatterStrategy implements IFormattingStrategy {

	private ChameleonEditor editor;
	
	public ChameleonFormatterStrategy(ChameleonEditor editor) {
		this.editor = editor;
			PresentationManager presentationManager = editor.getPresentationManager();
			if(presentationManager != null) {
				PresentationModel presentationModel = presentationManager.getPresentationModel();
				if(presentationModel != null) {
					presentationModel.initIndentElementsByDefaults();
				}
			}
	}
	
	/**
	 * @see IFormattingStrategy#format(String, boolean, String, int[])
	 */
	@Override
   public String format(String content, boolean isLineStart, String indentation, int[] positions) {
		String result = content;
		// get the selection if any:
		ISelectionProvider selProv = editor.getSelectionProvider();
		ISelection sel = null;
		if (selProv != null){
			selProv.getSelection();
		}
		if (sel == null || sel.isEmpty()) {
			// if no valid selection, format the whole document
			result = formatWholeDocument(editor.getDocument(), content);
		} //else {
//			// TODO: formatting of selection
//			// get the region of the selection:
//			int offset;
//			int length;
//			if (sel instanceof TextSelection) {
//				offset = ((TextSelection) sel).getOffset();
//				length = ((TextSelection) sel).getLength();
//			} else if (sel instanceof MarkSelection) {
//				offset = ((MarkSelection) sel).getOffset();
//				length = ((MarkSelection) sel).getLength();
//			}
	//	}
		return result;
	}

	/**
	 * Will reformat the whole document
	 * Only the indentation will be set correctly
	 * 
	 * @param document the current document
	 * @param content the whole document as a string
	 */
	public String formatWholeDocument(EclipseDocument document, String content) {
		String result = "";
		String eol = null;
		String tab = "\t";
		try {
			eol = document.getLineDelimiter(0);
		} catch (BadLocationException e) {
			eol = "\r\n";
		}
		Scanner scanner = new Scanner(content);
		String line;
		int lineNb = 0;
		int indentationSteps = 0;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			// compute number of indentation steps
			indentationSteps = getNbIndentationStepsForLine(document, lineNb);
			// add tabs:
			result += Util.repeatString(tab, indentationSteps);
			// add line and end-of-line:
			result += line.trim() + eol;
			lineNb++;
		}
		return result;
	}

	/**
	 * Returns the number of indentation steps of the line with the given linenumber
	 * 
	 * @param document the current document
	 * @param lineNb the current line number
	 * @param identSteps the current number of indentation steps (of the previous line)
	 * @return
	 */
	private int getNbIndentationStepsForLine(EclipseDocument document, int lineNb){
		SortedSet<EclipseEditorTag> editorTags = new TreeSet<EclipseEditorTag>(EclipseEditorTag.lengthComparator);
		document.getEditorTagsWithPredicate( new AllEditorTagSurroundingLinePredicate(document, lineNb) , editorTags);
		//document.getEditorTagsWithPredicate( new EditorTagAtLinePredicate(document, lineNb) , editorTags);
		if(editorTags.size()==0)
			return 0;
		// use every editorTag or should we better filter them first
		Element firstElement = editorTags.iterator().next().getElement();
		return getElementDepth(firstElement);
	}

	/**
	 * Predicate that checks wheter an ALL-EditorTag is surrounding a certain line
	 */
	public static class AllEditorTagSurroundingLinePredicate extends SafePredicate<EclipseEditorTag>{
		private final int lineNb;
		private final EclipseDocument document;
		public AllEditorTagSurroundingLinePredicate(EclipseDocument document, int lineNb) {
			this.lineNb = lineNb;
			this.document = document;
		}
		@Override
		public boolean eval(EclipseEditorTag tag) {
			try {
				// consider only ALL-editorTags
				if(!tag.getName().equals(PositionMetadata.ALL))
					return false;
				// get editorTags surrounding this line
				int editorTagStartingLineNumber = document.getLineOfOffset(tag.getOffset());
				int editorTagEndingLineNumber = document.getLineOfOffset(tag.getOffset()+tag.getLength());
				return (editorTagStartingLineNumber < lineNb) && (editorTagEndingLineNumber > lineNb);
			} catch (BadLocationException e) {
				return false; // editorTag had invalid offset
			}
		}
	}

	/**
	 * Predicate that checks wheter an ALL-EditorTag is surrounding a certain line
	 */
	public static class EditorTagAtLinePredicate extends SafePredicate<EclipseEditorTag>{
		private final int lineNb;
		private final EclipseDocument document;
		public EditorTagAtLinePredicate(EclipseDocument document, int lineNb) {
			this.lineNb = lineNb;
			this.document = document;
		}
		@Override
		public boolean eval(EclipseEditorTag tag) {
			try {
				// get editorTags at this line
				int editorTagStartingLineNumber = document.getLineOfOffset(tag.getOffset());
				// int editorTagEndingLineNumber = document.getLineOfOffset(tag.getOffset()+tag.getLength());
				return (editorTagStartingLineNumber == lineNb);
			} catch (BadLocationException e) {
				return false; // editorTag had invalid offset
			}
		}
	}

	/**
	 * Returns the depth of the given element in the metamodel of Chameleon
	 * This is the number of parents that satisfy the isFormattingRelevantElementPredicate
	 * untill the CompilationUnit is reached
	 * 
	 * @param element
	 */
	private int getElementDepth(Element element){
		if(element instanceof Document || element == null)
			return 0;
		Element parent = element.lexical().parent();
		if(isIndentElement(element)){
			return getElementDepth(parent) + 1;
		} else {
			return getElementDepth(parent);
		}
	}
	
	/**
	 * For every language (first string) there is a collection of strings
	 * representing the SimpleClassName of the elements that must be indented.
	 */
	private static HashMap<String, Collection<String>> indentElements = new HashMap<String, Collection<String>>(2);

	public static void setIndentElements(String lang, Collection<String> elementNames) {
		indentElements.put(lang, elementNames);	
	}
	
	/**
	 * Checks if the given element is relevant for formatting and thus must be indented
	 * 
	 * @param element
	 */
	private boolean isIndentElement(Element element){
		String languageString = element.language().name();
		Collection<String> indentElementNames = indentElements.get(languageString);
		if(indentElementNames!= null){
			return indentElementNames.contains(element.getClass().getSimpleName());
		} else {
			System.err.println("No indentation elements found for current language ("+languageString+")");
			System.err.println("The XML-presentation file of all languages should contain the default indentation elements");
			return true; // indent for all elements
		}
	}

	@Override
   public void formatterStarts(String initialIndentation) {
		// NO-OP
	}

	@Override
   public void formatterStops() {
		// NO-OP
	}
	
	
}

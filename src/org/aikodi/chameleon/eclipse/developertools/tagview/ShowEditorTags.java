package org.aikodi.chameleon.eclipse.developertools.tagview;

import org.aikodi.chameleon.eclipse.connector.EclipseEditorTag;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.chameleon.input.PositionMetadata;
import org.aikodi.chameleon.util.Util;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.predicate.Predicate;
import org.aikodi.rejuse.predicate.SafePredicate;
import org.aikodi.rejuse.predicate.UniversalPredicate;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Position;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Contains static methods to create the text representation of the editor tags of a ChameleonDocument
 * This is meant to be used by the ChameleonProgrammer for debugging
 * 
 * @author Tim Vermeiren
 */
public class ShowEditorTags {
	
	
	/**
	 * Returns a string with the content of the given ChameleonDocument and with
	 * the ChameleonEditorPositions inserted as XML-tags. This makes the ChameleonEditorPositions in the document
	 * visable and these can then be printed out.
	 * 
	 * @param	document
	 * 			the Chameleon Document whose editorTags-string should be returned
	 * @param 	content
	 * 			default: document.get()
	 * @param	showOnlyAllChameleonEditorPositions
	 * 			if true, only the ChameleonEditorPositions with name ChameleonEditorPositionTypes.ALL will be shown.
	 */
	public static String getChameleonEditorPositionsStringOfDocument(EclipseDocument document, String content, boolean showOnlyAllChameleonEditorPositions) {
		Predicate<? super EclipseEditorTag, Nothing> otherConditions;
		// filter all editor tags?
		if(showOnlyAllChameleonEditorPositions){
			otherConditions = new EclipseEditorTag.NamePredicate(PositionMetadata.ALL);
		} else {
			otherConditions = UniversalPredicate.isTrue();
		}
		return getChameleonEditorPositionsStringOfDocument(document, content, otherConditions);
	}
		
	/**
	 * Returns a string with the content of the given ChameleonDocument and with
	 * the ChameleonEditorPositions inserted as XML-tags. This makes the ChameleonEditorPositions in the document
	 * visable and these can then be printed out.
	 * 
	 * @param	document
	 * 			the Chameleon Document whose editorTags-string should be returned
	 * @param 	content
	 * 			default: document.get()
	 */
	public static String getChameleonEditorPositionsStringOfDocument(EclipseDocument document, String content, Predicate<? super EclipseEditorTag, Nothing> otherConditions) {
		int[] positions = getPositions(document);
		String result = "";
		int currPos = positions[0];
		int lastPos;
		int indentation = 0;
		for(int i=0; i<positions.length+1; i++){
			// get starting editorTags 
			SortedSet<EclipseEditorTag> startingChameleonEditorPositions = new TreeSet<EclipseEditorTag>(Collections.reverseOrder(EclipseEditorTag.lengthComparator));
			Predicate<EclipseEditorTag,Nothing> predicate = new ChameleonEditorPositionsStartingAtOffsetPredicate(currPos).and(otherConditions);
			document.getEditorTagsWithPredicate(predicate, startingChameleonEditorPositions);
			// add starting editorTags 
			for(EclipseEditorTag currDec : startingChameleonEditorPositions){
				//String label = currDec.getElement().toString();
				String label = Util.getLastPart(currDec.element().getClass().getName());
				result += "\n" + Util.repeatString("\t", indentation) + "<EDITORTAG element=\""+label+ "\" name=\""+currDec.getName()+"\" beginoffset=\"" + currDec.getOffset() + "\">\n";
				indentation++;
			}
			// get ending editorTags 
			SortedSet<EclipseEditorTag> endingChameleonEditorPositions = new TreeSet<EclipseEditorTag>(EclipseEditorTag.lengthComparator);
			predicate = new ChameleonEditorPositionsEndingAtOffsetPredicate(currPos).and(otherConditions);
			document.getEditorTagsWithPredicate(predicate, endingChameleonEditorPositions);
			// add ending editorTags 
			for(EclipseEditorTag currDec : endingChameleonEditorPositions){
				// String label = currDec.getElement().toString();
				String label = Util.getLastPart(currDec.element().getClass().getName());
				indentation--;
				result += "\n" + Util.repeatString("\t", indentation) + "</EDITORTAG element=\""+label+ "\" name=\""+currDec.getName()+"\" endoffset=\"" + (currDec.getOffset()+currDec.getLength()) + "\">\n";
			}
			// add content
			if(i<positions.length){
				lastPos = currPos;
				currPos = positions[i];
				result += content.substring(lastPos, currPos);
			}
		}
		return result;
	}

	/**
	 * Returns all offsets in the given document where
	 * one ore more editor tags start or stop.
	 * The positions will be sorted in ascending order and
	 * no positions will appear twice in the array
	 * 
	 * @param document
	 * @return an array of integers
	 * 
	 */
	private static int[] getPositions(EclipseDocument document){
		try {
			Position[] editorTags = document.getPositions(EclipseEditorTag.CHAMELEON_CATEGORY);
			TreeSet<Integer> integers = new TreeSet<Integer>();
			integers.add(0);
			integers.add(document.getLength());
			for(Position pos : editorTags){
				if(pos instanceof EclipseEditorTag){
					EclipseEditorTag tag = ((EclipseEditorTag)pos);
					integers.add(tag.getOffset());
					integers.add(tag.getOffset()+tag.getLength());
				}
			}
			// convert array:
			int[] result = new int[integers.size()];
			int i = 0;
			for(Integer number : integers){
				result[i++] = number;
			}
			return result;
		} catch (BadPositionCategoryException e) {
			e.printStackTrace();
		}
		return new int[0];
	}

//	 PREDICATES:
	
	/**
	 * Predicate that checks whether an ChameleonEditorPosition is starting at a certain offset
	 */
	private static class ChameleonEditorPositionsStartingAtOffsetPredicate extends SafePredicate<EclipseEditorTag>{
		private final int offSet;
		public ChameleonEditorPositionsStartingAtOffsetPredicate(int offSet) {
			this.offSet = offSet;
		}
		@Override
		public boolean eval(EclipseEditorTag tag) {
			return tag.getOffset()==offSet;

		}
	}

	/**
	 * Predicate that checks wheter an ChameleonEditorPosition is ending at a certain offset
	 */
	private static class ChameleonEditorPositionsEndingAtOffsetPredicate extends SafePredicate<EclipseEditorTag>{
		private final int offSet;
		public ChameleonEditorPositionsEndingAtOffsetPredicate(int offSet) {
			this.offSet = offSet;
		}
		@Override
		public boolean eval(EclipseEditorTag tag) {
			return tag.getOffset()+tag.getLength()==offSet;

		}
	}
		
}

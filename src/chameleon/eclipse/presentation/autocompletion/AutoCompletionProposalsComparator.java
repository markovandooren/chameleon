package chameleon.eclipse.presentation.autocompletion;

import java.util.Comparator;

import org.rejuse.predicate.SafePredicate;

import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.editors.preferences.ChameleonEditorPreferencePage;
import chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import chameleon.oo.type.Type;

/**
 * Used to sort the results of the auto-completion.
 * The elements will be sorted by:
 * - appropriate Type (return type for methods)
 * - matching case
 * - alfabetically by label
 * - hashcode
 * 
 * @author Tim Vermeiren
 */
public class AutoCompletionProposalsComparator implements Comparator<Element> {

	protected String nameStart;
	protected SafePredicate<Type> typePredicate;
	protected ChameleonLabelProvider labelProvider;
	
	
	public static int nbOfDefiningTypeChecks = 
		ChameleonEditorPlugin.getDefault().getPreferenceStore().getInt(ChameleonEditorPreferencePage.NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS);
	
	/**
	 * 
	 * @param nameStart the beginning of the name of the element that is completed.
	 * @param typePredicate 
	 * @param language the language we are currently working in
	 */
	public AutoCompletionProposalsComparator(String nameStart, SafePredicate<Type> typePredicate, Language language) {
		this.nameStart = nameStart;
		this.typePredicate = typePredicate;
		labelProvider = new ChameleonLabelProvider(language, true, true, false);
	}

	/**
	 * The elements will be compared by:
	 * - appropriate type (return type for methods or defining type for fields and variables)
	 * - matching case
	 * - alfabetically by label
	 * - hashcode
	 * 
	 * @return 	result < 0    <=>    e1 < e2  (e1 comes before e2)
	 * 			result == 0   <=>	 e1 == e2
	 * 			result > 0    <=>	 e1 > e2  (e1 comes after e2)
	 */
	public int compare(Element e1, Element e2) {
		//FIXME !!!!
		return -1;
//		// first try to compare by type:
//		int result = compareType(e1, e2);
//		if(result != 0)
//			return result;
//		
//		// results with matching case before results with different case then searched word
//		result = compareCase(e1, e2);
//		if(result != 0) {
//			return result;
//		}
//		// when they both do/don't match case, compare by label:
//		result = compareLabel(e1, e2);
//		if(result != 0) {
//			return result;
//		}
//
//		// if the same label as well, compare by hashcode
//		result = e1.compareTo(e2);
//		return result;

	}

//	/**
//	 * @param e1
//	 * @param e2
//	 */
//	private int compareType(Element e1, Element e2) {
//		try {
//			if(typePredicate != null){
//				Boolean e1IsTypeDef = e1 instanceof TypeDefiningElement;
//				Boolean e2IsTypeDef = e2 instanceof TypeDefiningElement;
//				if(e1IsTypeDef && ! e2IsTypeDef)
//					return -1;
//				else if(! e1IsTypeDef && e2IsTypeDef)
//					return 1;
//				else if(e1IsTypeDef && e2IsTypeDef){
//					Type type1 = ((TypeDefiningElement)e1).getType();
//					Type type2 = ((TypeDefiningElement)e2).getType();
//					Boolean type1Matches = typePredicate.eval(type1);
//					Boolean type2Matches = typePredicate.eval(type2);
//					if( ! type1Matches.equals(type2Matches)){
//						return -1 * type1Matches.compareTo(type2Matches);
//					} else if(!type1Matches && nbOfDefiningTypeChecks>=1){
//						// both types doesn't match, but define types, so look to defining types
//						return compareDefiningType(Collections.singleton(type1), Collections.singleton(type2), nbOfDefiningTypeChecks-1);
//					}
//				} 
//			}
//		} catch (MetamodelException e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}
	
//	/**
//	 * 
//	 * @param defTypes
//	 * @param defTypes2
//	 * @param nbLevelsToGo
//	 * 			nb of levels to go after this compare
//	 * @return
//	 */
//	private int compareDefiningType(Set<Type> defTypes1, Set<Type> defTypes2, int nbLevelsToGo) {
//		Set<Type> e1DefTypes = getDefiningTypesOfSet(defTypes1);
//		Set<Type> e2DefTypes = getDefiningTypesOfSet(defTypes2);
//		Boolean e1DefinesType = typePredicate.exists(e1DefTypes);
//		Boolean e2DefinesType = typePredicate.exists(e2DefTypes);
//		int result = -1 * e1DefinesType.compareTo(e2DefinesType);
//		// if still the same, and want to look further:
//		if(result == 0 && nbLevelsToGo > 0){
//			nbLevelsToGo--;
//			return compareDefiningType(e1DefTypes, e2DefTypes, nbLevelsToGo);
//		}
//		return result;
//	}
	
//	/**
//	 * Calculates all the types defined by the all-accessible 
//	 * members of one of the given types
//	 */
//	private Set<Type> getDefiningTypesOfSet(Set<Type> types){
//		final Set<Type> result = new HashSet<Type>();
//		new Visitor<Type>(){
//			@Override
//			public void visit(Type type) {
//				 result.addAll(type.getAccessibleDefiningTypes());
//			}
//		}.applyTo(types);
//		return result;
//	}
	
//	/**
//	 * @param e1
//	 * @param e2
//	 */
//	private int compareCase(Element e1, Element e2) {
//		if(e1 instanceof Declaration && e2 instanceof Declaration){
//			boolean e1MatchesCase = (((Declaration)e1).signature().startsWith(nameStart));
//			boolean e2MatchesCase = (((NamedElement)e2).getName().startsWith(nameStart));
//			// An element that matches the case, comes before an element that doesn't match case
//			if(e1MatchesCase && ! e2MatchesCase)
//				return -1;
//			// An element that doesn't match the case, comes after an element that matches case
//			if(! e1MatchesCase && e2MatchesCase)
//				return 1;
//		}
//		return 0;
//	}
	
	/**
	 * @param e1
	 * @param e2
	 * @return
	 */
	private int compareLabel(Element e1, Element e2) {
		String e1Label = labelProvider.getText(e1);
		String e2Label = labelProvider.getText(e2);
		return e1Label.compareToIgnoreCase(e2Label);
	}

	
}

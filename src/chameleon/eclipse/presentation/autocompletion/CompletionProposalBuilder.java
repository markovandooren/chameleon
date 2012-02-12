package chameleon.eclipse.presentation.autocompletion;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.method.RegularMethod;

/**
 * Generates the CompletionProposals for the ContentAssistProcessor.
 * 
 * @author Tim Vermeiren
 */
public class CompletionProposalBuilder {
	
	/**
	 * @return An auto-comletion proposal containing the given NamedElement
	 * 
	 * @param element an element that's a possible completion 
	 * @param document the chameleondocument we're working in
	 * @param offset the place in the document where the auto completion is called
	 */
	public static ICompletionProposal buildProposal(Element element, ChameleonDocument document, int offset){
		if(element instanceof RegularMethod){
			return  buildTemplateProposal((RegularMethod)element, document, offset);
		} else if (element instanceof Declaration){
			return buildProposal((Declaration)element, document, offset);
		} else{
			System.err.println("The element "+element.getClass().getName()+" is not supported in the CompletionProposalBuilder.buildProposal method");
			return null;
		}
	}
	
	/**
	 * @return An auto-comletion proposal containing the given NamedElement
	 * 
	 * @param element an element that's a possible completion 
	 * @param document the chameleondocument we're working in
	 * @param offset the place in the document where the auto completion is called
	 */
	public static ICompletionProposal buildProposal(Declaration element, ChameleonDocument document, int offset){
		// get the elementName of the element (for completion)
		ChameleonLabelProvider labelProvider = new ChameleonLabelProvider(element.language(), true, true, false);
		String elementName = labelProvider.getLabel(element);
		// get the label (for information in the menu)
		String label = labelProvider.getText(element);
		Image image = labelProvider.getImage(element);
		// get the region to replace
		IRegion wordRegion = document.findWordRegion(offset);
		// build the proposal
		return new ChameleonCompletionProposal(elementName, wordRegion.getOffset(), wordRegion.getLength(), elementName.length(), 
				image, label, null, element, labelProvider );
	}
	
	
	/**
	 * @return The template of the method with the parameters as editable regions
	 * 
	 * @param method the method for which this TemplateProposal will be build
	 * @param document the chameleondocument we're working in
	 * @param offset the place in the document where the auto completion is called
	 */
	public static ICompletionProposal buildTemplateProposal(RegularMethod method, ChameleonDocument document, int offset){
		throw new ChameleonProgrammerException();
//		// get the pattern from the ChameleonEditorExtention (parameters are template regions)
//		EclipseEditorExtension ext = document.getProjectNature().getModel().language().plugin(EclipseEditorExtension.class);
//		String patternString = ext.getMethodTemplatePattern(method);
//		// use ChameleonLabelProvider for label and icon:
//		ChameleonLabelProvider labelProvider = new ChameleonLabelProvider(method.language(), true, true, false);
//		Image image = labelProvider.getImage(method);
//		String label = labelProvider.getLabel(method);
//		String description = labelProvider.getExtraInfo(method, true);
//		// build contextinformation (yellow text ballon shown after proposal is chosen)
//		IContextInformation info = new ContextInformation(label, label);
//		// build the TemplateProposal:
//		Template temp = new Template(label, description, ChameleonDocument.DEFAULT_CONTENT_TYPE , patternString, false);
//		IRegion replaceRegion = document.findWordRegion(offset);
//		return new ChameleonTemplateProposal(temp, new DocumentTemplateContext(new TemplateContextType(), document, replaceRegion.getOffset(), replaceRegion.getLength()), replaceRegion, image, info, method, labelProvider);
	}

}

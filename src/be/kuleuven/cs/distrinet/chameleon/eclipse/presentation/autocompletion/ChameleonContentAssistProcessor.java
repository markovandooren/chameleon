package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.autocompletion;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupStrategy;
import be.kuleuven.cs.distrinet.chameleon.eclipse.connector.EclipseEditorExtension;
import be.kuleuven.cs.distrinet.chameleon.eclipse.connector.EclipseEditorTag;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.EclipseDocument;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.MethodInvocation;

/**
 * Generates the auto completion proposals.
 * 
 * @author Tim Vermeiren
 */
public class ChameleonContentAssistProcessor implements IContentAssistProcessor {

	public ChameleonContentAssistProcessor() {
		
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		offset--;
		EclipseDocument chamDoc = (EclipseDocument)viewer.getDocument();
		try {
			IRegion wordRegion = chamDoc.findWordRegion(offset);
			String nameStart = chamDoc.get(wordRegion.getOffset(), wordRegion.getLength());
			EclipseEditorTag dec = chamDoc.getSmallestEditorTagAtOffset(wordRegion.getOffset());
			if(dec != null){
				Element el = dec.getElement();
//					ContextElement element = (ContextElement)el;
					LookupStrategy context;
					// see if element has a target (e.g. car.getOwner())

					//CHANGE COMMENTED OUT, NEEDS BETTER SOLUTION
//					if(el instanceof ExpressionWithTarget){
//						InvocationTarget target = ((ExpressionWithTarget)el).getTarget();
//						if(target != null) {
//							context = target.targetContext();
//						} else {
//							context = el.lexicalLookupStrategy();
//						}
//					} else {
//						context = el.lexicalLookupStrategy();
//					}
					
					
					
					
					// search for elements:
					Language language = chamDoc.getProjectNature().getModel().language();
//					SafePredicate<Type> typePredicate = el.getTypePredicate();
					// FIXME
					Set<Element> foundElements = new HashSet<Element>();
//					Set<Element> foundElements = new TreeSet<Element>(new AutoCompletionProposalsComparator(nameStart, typePredicate, language));
//					context.findElementsStartingWith(nameStart, foundElements);
					
					
					// Build proposals array:
					ICompletionProposal[] result = new ICompletionProposal[foundElements.size()];
					int i = 0;
					for (Element currElement : foundElements) {
						ICompletionProposal proposal;
//						proposal = language.connector(EclipseEditorExtension.class).completionProposal(currElement, chamDoc, offset);
						proposal = CompletionProposalBuilder.buildProposal(currElement, chamDoc, offset);
						result[i++] = proposal;						
					}
					return result;
			}
		} 
//		catch (LookupException e) {
//			e.printStackTrace();
//		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		try {
			offset--;
			EclipseDocument chamDoc = (EclipseDocument)viewer.getDocument();
			EclipseEditorTag dec = chamDoc.getSmallestEditorTagAtOffset(offset);
			if(dec!=null){
				Element element = dec.getElement();
				Language language = ((EclipseDocument)viewer.getDocument()).getProjectNature().getModel().language();
				EclipseEditorExtension ext = language.plugin(EclipseEditorExtension.class);
				String elementLabel = ext.getLabel(element);
				if(element instanceof MethodInvocation){
					MethodInvocation method = (MethodInvocation) element;
					elementLabel = ext.getLabel(method.getElement());
					return new IContextInformation[]{new ContextInformation(elementLabel, elementLabel)};
				} else {
					return null;
				}
			}
		} catch (ModelException e) {
			e.printStackTrace();
		}
		return null;

	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '.' };
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return new char[] { '(', ',', ' ' };
	}

	public IContextInformationValidator getContextInformationValidator() {
		return new ContextInformationValidator(this);
	}

	public String getErrorMessage() {
		return null;
	}


}

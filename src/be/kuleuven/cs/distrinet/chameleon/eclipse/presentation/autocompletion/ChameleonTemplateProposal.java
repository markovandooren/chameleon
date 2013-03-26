/**
 * Created on 18-apr-07
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.autocompletion;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;

/**
 * Extention on the TemplateProposal to support ContextInformation
 * with templates.
 * 
 * @author Tim Vermeiren
 */
public class ChameleonTemplateProposal extends TemplateProposal {
	
	protected IContextInformation contentInformation;
	protected String additionalProposalInfo;
	protected Element element;
	protected ChameleonLabelProvider labelProvider;
	
	public ChameleonTemplateProposal(Template template,
			TemplateContext context, IRegion region, Image image, IContextInformation contentInformation, Element element, ChameleonLabelProvider labelProvider) {
		super(template, context, region, image);
		this.contentInformation = contentInformation;
		this.element = element;
		this.labelProvider = labelProvider;
	}

	@Override
	public IContextInformation getContextInformation() {
		return contentInformation;
	}
	
	/*
	 * @see ICompletionProposal#getAdditionalProposalInfo()
	 */
	public String getAdditionalProposalInfo() {
		if(additionalProposalInfo==null)
			additionalProposalInfo = labelProvider.getCode(element);
		return additionalProposalInfo;
		
	}
	
}

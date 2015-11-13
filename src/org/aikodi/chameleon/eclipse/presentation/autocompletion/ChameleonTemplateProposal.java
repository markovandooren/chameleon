/**
 * Created on 18-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.autocompletion;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

/**
 * Extention on the TemplateProposal to support ContextInformation
 * with templates.
 * 
 * @author Tim Vermeiren
 */
public class ChameleonTemplateProposal extends TemplateProposal {
	
	private IContextInformation contentInformation;
	private String additionalProposalInfo;
	private Element element;
	private ChameleonLabelProvider labelProvider;
	
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
	@Override
   public String getAdditionalProposalInfo() {
		if(additionalProposalInfo==null)
			additionalProposalInfo = labelProvider.getCode(element);
		return additionalProposalInfo;
		
	}
	
}

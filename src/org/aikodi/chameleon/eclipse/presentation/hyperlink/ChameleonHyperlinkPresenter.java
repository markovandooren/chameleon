/**
 * @author Tim Vermeiren
 * @author Marko van Dooren
 */
package org.aikodi.chameleon.eclipse.presentation.hyperlink;

import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.hyperlink.DefaultHyperlinkPresenter;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.swt.graphics.Color;

public class ChameleonHyperlinkPresenter extends DefaultHyperlinkPresenter {

	private Color validColor;
	private Color semivalidColor;
	private Color invalidColor;
	private Color defaultColor;

	/**
	 * 
	 * @param validColor the color of a working hyperlink
	 * @param invalidColor the color of a hyperlink that not refers to a valid element with a valid compilationunit
	 * @param semivalidColor the color of a hyperlink that refers to a valid element without a valid compilationunit
	 * @param defaultColor the color of all other hyperlinks (not ChameleonHyperlinks)
	 * 
	 * @pre The colors cannot be disposed and must be valid (not null)
	 * @post The colors will be disposed after the uninstall
	 */
	public ChameleonHyperlinkPresenter(Color defaultColor, Color validColor, Color invalidColor, Color semivalidColor) {
		super(defaultColor);
		this.defaultColor = defaultColor;
		this.validColor = validColor;
		this.semivalidColor = semivalidColor;
		this.invalidColor = invalidColor;
	}

	@Override
	public boolean canShowMultipleHyperlinks() {
		return false;
	}

	@Override
	public void showHyperlinks(IHyperlink[] hyperlinks) {
		IHyperlink hyperlink = hyperlinks[0];
		if(hyperlink instanceof ChameleonHyperlink){
			String status = ((ChameleonHyperlink)hyperlink).getStatus();
			if(status == ChameleonHyperlink.VALID_STATUS){
				setColor(validColor);
			} else if (status == ChameleonHyperlink.SEMIVALID_STATUS){
				setColor(semivalidColor);
			} else {
				setColor(invalidColor);
			}
		} else {
			setColor(defaultColor);	
		}
		super.showHyperlinks(hyperlinks);
	}

	@Override
	public void uninstall() {
		defaultColor.dispose();
		validColor.dispose();
		invalidColor.dispose();
		super.uninstall();
	}
}

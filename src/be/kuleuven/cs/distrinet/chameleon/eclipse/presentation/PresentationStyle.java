package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.custom.StyleRange;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PresentationStyle {
	/**
     * @author Manuel Van Wesemael 
     * @author Joeri Hendrickx 
	 * 
	 * Creates a new presentation style based on a set of presentational XML nodes
	 * as described in the documentation
	 * @param childNodes
	 * 	the nodes to work from
	 */
	public PresentationStyle(NodeList childNodes) {
		for (int i=0; i<childNodes.getLength();i++) {
			Node current = childNodes.item(i);
			String nodeName = current.getNodeName();
			if (nodeName.equals("foreground"))
				foreground = new OptionalColor(current.getTextContent());
			else if (nodeName.equals("background"))
				background = new OptionalColor(current.getTextContent());
			else if (nodeName.equals("bold"))
				bold = true;
			else if (nodeName.equals("italic"))
				italic = true;
			else if (nodeName.equals("underline"))
				underline = true;
			else if (nodeName.equals("fold"))
				fold = true;
			else if (nodeName.equals("foldable"))
				foldable = true;
		}
			
	}
	
	/**
	 * 
	 * @param store
	 * @param fieldName
	 * @return the presentatation style as it can be formed from the preferences
	 * in the store, for the given fieldname
	 */
	public static PresentationStyle fromStore(IPreferenceStore store, String fieldName){
		
		boolean bold = store.getBoolean(fieldName+"bold");
		boolean italic = store.getBoolean(fieldName+"italic");
		boolean underline = store.getBoolean(fieldName+"underline");
		boolean folded = store.getBoolean(fieldName+"folded");
		boolean foldable = store.getBoolean(fieldName+"foldable");
		
		OptionalColor fg = new OptionalColor();
		OptionalColor bg = new OptionalColor();
		
		if (store.getBoolean(fieldName+"foreground_set")){
			fg.setColor(store.getString(fieldName+"foreground_color"));
		}
		if (store.getBoolean(fieldName+"background_set")){
			bg.setColor(store.getString(fieldName+"background_color"));
		}
		
		PresentationStyle ps = new PresentationStyle(fg, bg, bold, italic, underline, foldable, folded);
		return ps;
		
	}

	/**
	 * 
	 * @param foreground
	 * 	foreground text color for the new style
	 * @param background
	 *  background text color for the nex style
	 * @param bold
	 *  selects wether this text is rendered in bold
	 * @param italic
	 *  selects wether this text is rendered in italics
	 * @param underline
	 *  selects wether this text is underlined
	 * @param foldable
	 *  selects wether this text can be folded
	 * @param folded
	 *  selects wether this text is, by default, folded
	 * 
	 * 	creates a new presentaioionstyle from the given parameters
	 */
	public PresentationStyle(OptionalColor foreground, OptionalColor background, boolean bold, boolean italic, boolean underline, boolean foldable, boolean folded) {
		this.background=new OptionalColor(background);
		this.foreground=new OptionalColor(foreground);
		this.bold=bold;
		this.italic=italic;
		this.underline=underline;
		this.foldable=foldable;
		this.fold=folded;
	}


	//the background color
	private OptionalColor background = new OptionalColor();
	//the foreground color
	private OptionalColor foreground = new OptionalColor();
	//whether this style is bold
	private boolean bold = false;
	//whether this style is italic
	private boolean italic = false;
	//whether this style is underlined
	private boolean underline = false;
	//whether this style is folded
	private boolean fold = false;
	//whether this style is foldable
	private boolean foldable;
	
	/**
	 * 
	 * @param offset
	 * @param length
	 * @return a new stylerange for the given offset and length
	 */
	public StyleRange getStyleRange(int offset, int length) {
		StyleRange r = new StyleRange(offset,length,foreground.getColor(), background.getColor());
		int style=0;
		if (bold && !italic) {
			r.fontStyle = 1;
		} else if (!bold && italic) {
			r.fontStyle = 2;
		}
		if (bold && italic){
			r.fontStyle = 3;
		}
		if (underline) {
			r.underline=true;
		}
		return r;
	}
	
	/**
	 * @return the bacgroundColor
	 */
	public OptionalColor getBackground() {
		return background;
	}
	
	/**
	 * @return whether this style is bold
	 */
	public boolean isBold() {
		return bold;
	}
	
	/**
	 * @return whether this style is folded
	 */
	public boolean isFolded() {
		return fold;
	}
	
	/**
	 * @return the foregroundcolor
	 */
	public OptionalColor getForeground() {
		return foreground;
	}
	
	/**
	 * @return whether this style is italic
	 */
	public boolean isItalic() {
		return italic;
	}
	
	/**
	 * @return whether this style is underlined
	 */
	public boolean isUnderlined() {
		return underline;
	}
	
	/**
	 * @return whether this style is foldable
	 */
	public boolean isfoldable() {
		return foldable;
	}
}

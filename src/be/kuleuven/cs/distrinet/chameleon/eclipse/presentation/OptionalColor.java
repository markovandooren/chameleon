package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A class defining a color that can is allowed to be undefined.
 *
 */
public class OptionalColor {
	
	private boolean defined;
	private Color color;
	
	public OptionalColor(String coded){
		if (coded==null) {defined = false;}
		else if (coded.equals("")) {defined = false;}
		else if (coded.equals("null")) {defined = false;}
		else {
			defined = true;
			color = colorFromString(coded);
		}
	}
	
	private Color colorFromString(String coded){
		if (coded.toCharArray()[0]=='#'){
			int r = Integer.valueOf(coded.substring(1,3),16);
			int g = Integer.valueOf(coded.substring(3,5),16);
			int b = Integer.valueOf(coded.substring(4,6),16);
			return new Color(Display.getCurrent(),r,g,b);
		}
		String[] split = coded.split(",");
		int r = Integer.parseInt(split[0]);
		int g = Integer.parseInt(split[1]);
		int b = Integer.parseInt(split[2]);
		return new Color(Display.getCurrent(),r,g,b);
	}

	/**
	 * Clone Constructor
	 */
	public OptionalColor(OptionalColor oc) {
		this.defined = oc.defined;
		this.color = oc.color;
	}

	/**
	 * Create a new undefined color
	 */
	public OptionalColor() {
		defined =false;
		color=null;
	}

	/**
	 * create a new defined color from rgb
	 */
	public OptionalColor(RGB colorValue) {
		defined = true;
		color = new Color(Display.getCurrent(),colorValue);
	}

	public boolean isDefined(){
		return defined;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String toString(){
		if (!defined) return "null";
		return getColorString(color);
	}
	
	
	private String getColorString(Color c) {
		if (c==null)return ("0,0,0");
		String r = c.getRed()+","+c.getGreen()+","+c.getBlue();
		return r;
	}

	public String getColorString() {
		return getColorString(color);
	}

	public void setColor(String string) {
		if (string == null) setUndefined();
		else {
			color = colorFromString(string);
			defined = true;
		}
	}
	
	public void setUndefined(){
		color = null;
		defined = false;
		
	}
	

}

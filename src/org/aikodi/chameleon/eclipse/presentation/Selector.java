package org.aikodi.chameleon.eclipse.presentation;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A selector determines which positions in the code a certain style rule should map
 * to.  A null-string indicates that that criterium should be ignored.  All criteria
 * are combined using the AND rule.
 */
public class Selector {

	private String description;

	public Selector(String element, String decorator, String desc){
		this.description = desc;
		
		if (element.equals("*"))
			this.elementType=null;
		else
			this.elementType = element;
		
		if (decorator.equals("*"))
			this.decoratorType=null;
		else
			this.decoratorType = decorator;
	}
	
	/*
	 * The type of element this selector should map to. null means ignore
	 */
	private String elementType;
	/*
	 * The decoratortype this element should map to. null means ignore
	 */
	private String decoratorType;
	
	/**
	 * @return Whether this selector maps to a given element and decorator
	 * 
	 */
	public boolean map(String element, String decorator) {
		
		
		
		if (decoratorType!=null && !decoratorType.equalsIgnoreCase(decorator)) return false;
		if (elementType==null) return true;
		String[] elemh = element.split(";");
		for (int i=0; i<elemh.length;i++)
			if (elemh[i].equalsIgnoreCase(elementType)) return true;
		return false;
	}

	public String getPositionType() {
		return decoratorType;
	}

	public String getDescription(){
		return description;
	}
	
	public String getElementType() {
		return elementType;
	}

	
}

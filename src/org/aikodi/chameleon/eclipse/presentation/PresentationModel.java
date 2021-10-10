package org.aikodi.chameleon.eclipse.presentation;

import org.aikodi.chameleon.eclipse.ChameleonEditorPlugin;
import org.aikodi.chameleon.eclipse.LanguageMgt;
import org.aikodi.chameleon.eclipse.presentation.formatting.ChameleonFormatterStrategy;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.custom.StyleRange;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/***
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A Presentation Model is part of the language model of a ChameleonEditor instance.
 * 
 * It contains definitions about what and how to colour, to fold, and other presentational
 * parameters.  This object is created from a presentation XML node conform to the
 * DTD as described in the ChameleonEditor documentation.
 */
public class PresentationModel {
	
	/*A vector containing the style rules */
	private List<StyleRule> _rules = new ArrayList<StyleRule>();
	private List<String[]> _outlineElements = new ArrayList<String[]>();
	private List<String> _defaultOutlineElements = new ArrayList<String>();
	private List<String[]> _indentElements = new ArrayList<String[]>();	
	private List<String> _defaultIndentElements = new ArrayList<String>();;
	
	/**
	 * Creates a new presentation model with its rules from an XML document
	 * @param XMLFile
	 * 		The XML document being used
	 */
	public PresentationModel(String name, InputStream XMLFile){
		if(name == null) {
			throw new ChameleonProgrammerException("The language name for the presentation model is null.");
		}
		if(XMLFile == null) {
			throw new ChameleonProgrammerException("The input stream for the presentation model is null.");
		}
		_name=name;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			   DocumentBuilder builder = factory.newDocumentBuilder();
			   Document document = builder.parse(XMLFile);
			   NodeList stylerules = document.getChildNodes().item(0).getChildNodes(); // stylerules
			   for (int i = 0; i < stylerules.getLength(); i++) {
				   Node currentrule = stylerules.item(i);
						String nodeName = currentrule.getNodeName();
				   if (nodeName.equals("stylerule")) {
					   addRule(currentrule);
				   } else if (nodeName.equals("outline")) {
					   addOutlineElements(currentrule.getChildNodes());
				   } else if (nodeName.equals("indentation")) {
							addIndentationElements(currentrule.getChildNodes());
			     }
			}
		} catch (SAXParseException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * adds the outline elements to the list, according to the occurences in de xml file
	 */
	private void addOutlineElements(NodeList childNodes) {
		
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		
		for(int i= 0; i< childNodes.getLength(); i ++){
			Node currNode = childNodes.item(i);
			if (currNode.getNodeName().equals("element")){
				
				Node child = currNode.getFirstChild();
				String[] v = new String[2];
				v[0] = child.getNodeValue();
				v[1] = currNode.getAttributes().getNamedItem("description").getTextContent();
				
				_outlineElements.add(v);
				if (currNode.getAttributes().getNamedItem("default").getTextContent().equals("visible")){
					_defaultOutlineElements.add(child.getNodeValue());
					String fieldNaam = "outlineElement_"+languageName()+"_"+child.getNodeValue();
					store.setDefault(fieldNaam, true);
				} else {
					String fieldNaam = "outlineElement_"+languageName()+"_"+child.getNodeValue();
					store.setDefault(fieldNaam, true);
				}
			}
		}
		
	}

	private void addIndentationElements(NodeList childNodes) {
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		for(int i=0; i<childNodes.getLength(); i ++){
			Node currNode = childNodes.item(i);
			if (currNode.getNodeName().equals("element")){
				Node child = currNode.getFirstChild();
				String[] v = new String[2];
				v[0] = child.getNodeValue();
				v[1] = currNode.getAttributes().getNamedItem("description").getTextContent();
				_indentElements.add(v);
				if (currNode.getAttributes().getNamedItem("default").getTextContent().equalsIgnoreCase("true")){
					_defaultIndentElements.add(child.getNodeValue());
					String fieldNaam = "indentElement_"+languageName()+"_"+child.getNodeValue();
					store.setDefault(fieldNaam, true);
				} else {
					String fieldNaam = "indentElement_"+languageName()+"_"+child.getNodeValue();
					store.setDefault(fieldNaam, false);
				}
			}
		}

	}
	/*
	 * adds a new rule to the rules
	 */
	private void addRule(Node currentrule) {
		String element = currentrule.getAttributes().getNamedItem("element").getTextContent();
		String dec = currentrule.getAttributes().getNamedItem("decorator").getTextContent();
		String desc = currentrule.getAttributes().getNamedItem("description").getTextContent();
		PresentationStyle style = new PresentationStyle(currentrule.getChildNodes());
		addRule(style, new Selector(element, dec, desc));
	}

	/*
	 * adds a rule to the rules. A new styleRule is made from the style and the selector
	 */
	private void addRule(PresentationStyle style, Selector selector)	{
		addDefaultToStore(style, selector);
		if (ruleSetInStore(selector)){
			_rules.add(new StyleRule(getFromStore(selector),selector));
		}
		else {
			_rules.add(new StyleRule(style, selector));
			addToStore(style, selector);
		}
	}
	
	/*
	 * returns the style for the given selector, it is retrieved from the store.
	 */
	private PresentationStyle getFromStore(Selector selector) {
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		String fieldname = "stylerule_"+languageName()+"_"+selector.getElementType()+"_"+selector.getPositionType()+"_";
		return PresentationStyle.fromStore(store, fieldname);
	}


	private boolean ruleSetInStore(Selector selector) {
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		String fieldNaam = "stylerule_"+languageName()+"_"+selector.getElementType()+"_"+selector.getPositionType()+"_ruleSet";
		return store.getBoolean(fieldNaam);
	}

	private void addDefaultToStore(PresentationStyle style, Selector selector){
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		String fieldNaam = "stylerule_"+languageName()+"_"+selector.getElementType()+"_"+selector.getPositionType()+"_";
		store.setDefault(fieldNaam+"foreground_color",style.getForeground().getColorString());
		store.setDefault(fieldNaam+"background_color",style.getBackground().getColorString());
		store.setDefault(fieldNaam+"foreground_set",style.getForeground().isDefined());
		store.setDefault(fieldNaam+"background_set",style.getBackground().isDefined());
		store.setDefault(fieldNaam+"folded",style.isFolded());
		store.setDefault(fieldNaam+"foldable",style.isfoldable());
		store.setDefault(fieldNaam+"bold",style.isBold());
		store.setDefault(fieldNaam+"italic",style.isItalic());
		store.setDefault(fieldNaam+"underline",style.isUnderlined());
	}

	private void addToStore(PresentationStyle style, Selector selector){
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		String fieldName = "stylerule_"+languageName()+"_"+selector.getElementType()+"_"+selector.getPositionType()+"_";
		store.setValue(fieldName+"foreground_color",style.getForeground().getColorString());
		store.setValue(fieldName+"background_color",style.getBackground().getColorString());
		store.setValue(fieldName+"foreground_set",style.getForeground().isDefined());
		store.setValue(fieldName+"background_set",style.getBackground().isDefined());
		store.setValue(fieldName+"folded",style.isFolded());
		store.setValue(fieldName+"foldable",style.isfoldable());
		store.setValue(fieldName+"bold",style.isBold());
		store.setValue(fieldName+"italic",style.isItalic());
		store.setValue(fieldName+"underline",style.isUnderlined());

		store.setValue(fieldName+"ruleSet",true);
		
	}

//	/*
//	 * creates a color from a string 
//	 */
//	private Color createColor(String coded) {
//		int r = Integer.parseInt(coded.substring(1,3),16);
//		int g = Integer.parseInt(coded.substring(3,5),16);
//		int b = Integer.parseInt(coded.substring(5,7),16);
//		return new Color(Display.getCurrent(),r,g,b);
//	}	
	
	/**
	 * Finds a presentation style for a certain position
	 * @param element
	 * 	the element type of the given position.  May be expressed hierarchically using periods
	 * @param tag
	 *  the tag type of the given position
	 * @return the style if any, null otherwise
	 */
	public PresentationStyle getRule(String element, String tag){
		Iterator it = _rules.iterator();
		while (it.hasNext()){
			StyleRule sr = (StyleRule) it.next();
			if (sr.selector.map(element, tag))
				return sr.style;
		}
		return null;
	}
	
	/**
	 * Represents a new style rule with a certain style & selector
	 *
	 */
	public class StyleRule {
		public StyleRule(PresentationStyle style, Selector selector){
			this.style=style;
			this.selector=selector;
		}

		private PresentationStyle style;
		
		public PresentationStyle style() {
			return this.style;
		}
		
		private Selector selector;
		
		public Selector selector() {
			return this.selector;
		}
	}

	/**
	 * 
	 * @param offset
	 * 	the offset of the element
	 * @param length
	 * 	the length of the element
	 * @param element
	 * 	the element where a stylerange is to be made of
	 * @param decname
	 * 	the decorator namen for the element
	 * @return a new styleRange for a given element
	 */
	public StyleRange map(int offset, int length, String element, String decname) {
		for(StyleRule rule: _rules)	{
			if (rule.selector.map(element,decname)) {
				return rule.style.getStyleRange(offset,length);
			}
		}
		return null;
	}

	public List<String[]> getOutlineElements() {
		return new ArrayList<>(_outlineElements);
	}

	public void setOutlineElements(List<String[]> outlineElements) {
		this._outlineElements = new ArrayList<>(outlineElements);
	}

	public List<String> getDefaultOutlineElements() {
		return new ArrayList<>(_defaultOutlineElements);
	}

	public List<String[]> getIndentElements() {
		return new ArrayList<>(_indentElements);
	}

	public List<String> getDefaultIndentElements() {
		return new ArrayList<>(_defaultIndentElements);
	}

	public List<String> getOutlineElementsSimple(){
		List<String[]> pm = getOutlineElements();
		List<String> v = new ArrayList<String>();
		for (Iterator<String[]> iter = pm.iterator(); iter.hasNext();) {
			String[] element = iter.next();
			v.add(element[0]);
		}		
		return v;
	}

	public List<String> getIndentElementsSimple(){
		List<String[]> pm = getIndentElements();
		List<String> v = new ArrayList<String>();
		for (Iterator<String[]> iter = pm.iterator(); iter.hasNext();) {
			String[] element = iter.next();
			v.add(element[0]);
		}		
		return v;
	}

	public HashMap<Selector, PresentationStyle> getRules() {
		HashMap<Selector, PresentationStyle> result = new HashMap<Selector, PresentationStyle>();
		
		for (Iterator<StyleRule> iter = _rules.iterator(); iter.hasNext();) {
			StyleRule element = iter.next();
			result.put(element.selector, element.style);
		}
		
		return result;
	}

	public void updateRule(Selector selector, PresentationStyle style) {
		addToStore(style, selector);
		Iterator<StyleRule> it = _rules.iterator();
		while(it.hasNext())
		{
			StyleRule rule = it.next();
			
			if (rule.selector.map(selector.getElementType(),selector.getPositionType()))
				rule.style=style;
		}
	}
	
	
	

	//	the inited languages for the outline
	private static List<String> initedOutlineLanguages = new ArrayList<String>();

	/**
	 * initializes a given languages' outline
	 * @param lang
	 * 	The language that is initialised
	 */
	public static void initAllowedOutlineElementsByDefaults(String lang){

		PresentationModel pm = LanguageMgt.getInstance().getPresentationModel(lang);

		List<String> v = new ArrayList<String>();
		for (Iterator<String[]> iter = pm.getOutlineElements().iterator(); iter.hasNext();) {
			String[] element = iter.next();
			v.add(element[0]);
		}
		initAllowedOutlineElementsByDefaults(lang, v, pm.getDefaultOutlineElements());
	}

	/**
	 * Initialises this language with the given defaults if no defaults are found in the 
	 * preference store
	 */
	public static void initAllowedOutlineElementsByDefaults(String language, List<String> allowedElements, List<String> defaultAllowedElements) {
		if (!initedOutlineLanguages.contains(language)){

			IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();

			List<String> allowed = new ArrayList<String>();
			if (store.getBoolean("Chameleon_outline_prefs_inited")) {

				for (Iterator<String> iter = allowedElements.iterator(); iter.hasNext();) {
					String element = iter.next();

					String fieldNaam = "outlineElement_"+language+"_"+element;
					if (store.getBoolean(fieldNaam)) allowed.add(element);
				}
			} else {
				allowed = defaultAllowedElements;

				for (Iterator<String> iter = allowedElements.iterator(); iter.hasNext();) {
					String element = iter.next();
					String fieldNaam = "outlineElement_"+language+"_"+element;
					boolean b = (allowed.contains(element));
					store.setValue(fieldNaam, b);
				}
				store.setValue("Chameleon_outline_prefs_inited", true);
			}
			//ChameleonOutlineTree.setAllowedElements(language, allowed);
			initedOutlineLanguages.add(language);
		}
	}

	// the inited languages for the indentation
	private static List<String> initedIndentLanguages = new ArrayList<String>();

	public static void initIndentElementsByDefaults(String lang){

		PresentationModel presModel = LanguageMgt.getInstance().getPresentationModel(lang);

		List<String> v = new ArrayList<String>();
		for (Iterator<String[]> iter = presModel.getIndentElements().iterator(); iter.hasNext();) {
			String[] element = iter.next();
			v.add(element[0]);
		}
		initIndentElementsByDefaults(lang, v, presModel.getDefaultIndentElements());
	}

	public static void initIndentElementsByDefaults(String language, List<String> indentElements, List<String> defaultIndentElements) {
		if (!initedIndentLanguages.contains(language)){

			IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();

			List<String> allowed = new ArrayList<String>();
			if (store.getBoolean("Chameleon_indent_prefs_inited")) {

				for (Iterator<String> iter = indentElements.iterator(); iter.hasNext();) {
					String element = iter.next();
					String fieldNaam = "indentElement_"+language+"_"+element;
					if (store.getBoolean(fieldNaam)) 
						allowed.add(element);
				}
				
			} else {
				allowed = defaultIndentElements;

				for (Iterator<String> iter = indentElements.iterator(); iter.hasNext();) {
					String element = iter.next();
					String fieldNaam = "indentElement_"+language+"_"+element;
					boolean b = (allowed.contains(element));
					store.setValue(fieldNaam, b);
				}
				store.setValue("Chameleon_indent_prefs_inited", true);
			}
			ChameleonFormatterStrategy.setIndentElements(language, allowed);
			initedIndentLanguages.add(language);
		}
	}

	public void initIndentElementsByDefaults() {
		initIndentElementsByDefaults(languageName(), getIndentElementsSimple(), getDefaultIndentElements());
	}

	public String languageName() {
		return _name;
	}
	
	private String _name;
}

package chameleon.workspace;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import chameleon.support.modifier.Constructor;
import chameleon.util.Util;

public abstract class ConfigElement {

	/**
	 * Return the text of this configuration element. The default
	 * implementation return the empty string "".
	 */
	public String $getText() {
		return "";
	}
	
	public void $setText(String text) throws ConfigException {
		
	}
	
	public void readFromXML(File xmlFile) throws ConfigException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = fac.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			process(doc.getDocumentElement()); 
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new ConfigException(e);
		}
	}
	
	private void process(Element node) throws ConfigException {
		$before();
		// 1. Set the text
		$setText(node.getTextContent());
		
		// 2. Set the attributes
		NamedNodeMap attributeMap = node.getAttributes();
		int nbAttributes = attributeMap.getLength();
		for(int i = 0; i < nbAttributes; i++) {
			processAttribute((Attr)attributeMap.item(i));
		}
		
		NodeList nodes = node.getChildNodes();
		int size = nodes.getLength();
		for(int i = 0; i < size; i++) {
			Node childNode = nodes.item(i);
			if(childNode instanceof Element) {
				processChild((Element)childNode);
			}
		}
		$after();
	}

	protected void $before() throws ConfigException {}

	protected void $after() throws ConfigException {}
	
	/**
	 * A map from upper case elements names to the corresponding classes.
	 */
	protected Map<String, Class> _childClassMap;

	protected void processChild(Element child) throws ConfigException {
		String name = child.getNodeName();
		Class childClass = getChildClass(name);
		ConfigElement childConfig;
		try {
			@SuppressWarnings("unused")
			boolean inner = childClass.isMemberClass();
			if(inner) {
//				java.lang.reflect.Constructor[] cs = childClass.getConstructors();
				@SuppressWarnings("unchecked")
				java.lang.reflect.Constructor ctor = childClass.getConstructors()[0];
				childConfig = (ConfigElement) ctor.newInstance(this);
			} else {
				childConfig = (ConfigElement) childClass.getDeclaredConstructor().newInstance();
			}
			childConfig.process(child);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
			// Ignore error to stop a single configuration file error from loading the entire project.
			e.printStackTrace();
		}
	}
	
	protected Class getChildClass(String childName) {
		if(_childClassMap == null) {
			initChildCache();
		}
		return _childClassMap.get(childName.toUpperCase());
	}
	
	private void initChildCache() {
		_childClassMap = new HashMap<>();
		for(Class c : getClass().getClasses()) {
			String name = c.getName();
			name = Util.getLastPart(name.replace('$', '.'));
			String key = name.toUpperCase();
			Class existing = _childClassMap.get(key);
			if((existing == null) || existing.isAssignableFrom(c)) {
				_childClassMap.put(key, c);
			}
		}
	}

	/**
	 * A map from upper case attribute names to the corresponding setter methods.
	 */
	protected Map<String, Method> _attributeMethodMap;
	
	protected Method attributeSetter(String attributeName) {
		if(_attributeMethodMap == null) {
			initAttributeCache();
		}
		return _attributeMethodMap.get(attributeName.toUpperCase());
	}
	
	protected void initAttributeCache() {
		_attributeMethodMap = new HashMap<String, Method>();
		Method[] methods = getClass().getMethods();
		for(Method method: methods) {
			String methodName = method.getName();
			if(methodName.startsWith("set")) {
				Type[] types = method.getGenericParameterTypes();
				if(types.length == 1 && types[0] instanceof Class) {
					Class c = (Class) types[0];
					if(c.getCanonicalName().equals("java.lang.String")) {
						// cut of "set" from the start of the name
						String key = methodName.toUpperCase().substring(3,methodName.length());
						_attributeMethodMap.put(key, method);
					}
				}
			}
		}
	}
	
	protected void processAttribute(Attr attribute) {
		try {
			String name = attribute.getName();
			String value = attribute.getValue();
			attributeSetter(name).invoke(this, value);
		} catch (IllegalAccessException | InvocationTargetException e) {
		  // We ignore this to stop errors in a config file from preventing loading the configuration file. 
			e.printStackTrace();
		}
	}
	
	public void writeToXML(File xmlFile) {
		
	}
}

package org.aikodi.chameleon.workspace;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.aikodi.chameleon.util.Util;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.action.SafeAction;
import org.aikodi.rejuse.association.OrderedMultiAssociation;
import org.aikodi.rejuse.association.SingleAssociation;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A class for elements in XML files that can process themselves.
 * 
 * @author Marko van Dooren
 *
 */
public abstract class ConfigElement {

	protected ConfigElement() {
		initChildCache();
		initAttributeCache();
	}
	
	/**
	 * Return the text of this configuration element. The default
	 * implementation return the empty string "".
	 */
	public String $getText() {
		return _text;
	}
	
	public void $setText(String text) throws ConfigException {
		_text = text;
	}
	
	private String _text;
	
	public String nodeName() {
		return Util.getLastPart(getClass().getName().replace('$', '.')).toLowerCase();
	}
	
	protected void readFromXML(File xmlFile) throws ConfigException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = fac.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			read(doc.getDocumentElement());
		} catch (Exception e) {
			throw new ConfigException(e);
		}
	}
	
	public void writeToXML(File xmlFile) throws ConfigException {
 	  DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ConfigException(e);
		}
		Document doc = docBuilder.newDocument();
		// doc must be passed as an argument because it is the factory of the XML nodes
		doc.appendChild(toElement(doc));
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(xmlFile);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new ConfigException(e);
		}
	}
	
	protected Element toElement(Document doc) {
		Element result = wrap(doc.createElement(nodeName()),doc);
		addImplicitChildren(result, doc);
		String text = $getText();
		if(text != null) {
			result.setTextContent(text);
		}
		// Add the attributes.
		for(Method method: _attributeGetters) {
			try {
				String attributeName = attributeKey(method.getName());
//				Attr attr = doc.createAttribute(attributeName);
				String value = (String) method.invoke(this);
//				attr.setValue(value);
				result.setAttribute(attributeName, value);
//				result.appendChild(attr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Add the child elements.
		for(ConfigElement cfg: _children.getOtherEnds()) {
			result.appendChild(cfg.toElement(doc));
		}
		return result;
	}
	
	protected Element wrap(Element original, Document doc) {
		return original;
	}
	
	protected void addImplicitChildren(Element result, Document doc) {
		
	}

	private void read(Element node) throws ConfigException {
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
	 * A map from lower case elements names to the corresponding classes.
	 */
	private Map<String, Class<? extends ConfigElement>> _childClassMap;
	private Map<Class<?>, String> _reverseChildClassMap;

	protected void processChild(Element child) throws ConfigException {
		String name = child.getNodeName();
		Class<? extends ConfigElement> childClass = getChildClass(name);
		if(childClass != null) {
				ConfigElement childConfig = createChild(childClass);
				addChild(childConfig); 
				childConfig.read(child);
		} else {
			_unprocessed.add(child);
		}
	}

	private <T extends ConfigElement> T createChild(Class<T> c) {
		Class<? extends T> childClass = bind(c);
		try {
			T childConfig;
			@SuppressWarnings("unused")
			boolean inner = childClass.isMemberClass();
			if(inner) {
				//				java.lang.reflect.Constructor[] cs = childClass.getConstructors();
				childConfig = (T) childClass.getConstructors()[0].newInstance(this);
			} else {
				childConfig = childClass.getDeclaredConstructor().newInstance();
			}
			addChild(childConfig);
			return childConfig;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * Map class 'c' to the most specific inner class that is its subclass. If there
	 * is more than one, an arbitrary class will be chosen (you should avoid this, though).
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> Class<? extends T> bind(Class<T> c) {
		Class<? extends T> result = c;
		Class<?>[] classes = getClass().getClasses();
		for(Class<?> k: classes) {
			if(result.isAssignableFrom(k)) {
				result = (Class<? extends T>) k;
			}
		}
		return result;
	}
	
	private List<Element> _unprocessed = new ArrayList<Element>();
	
	private OrderedMultiAssociation<ConfigElement,ConfigElement> _children = new OrderedMultiAssociation<ConfigElement,ConfigElement>(this);
	
	private SingleAssociation<ConfigElement,ConfigElement> _parent = new SingleAssociation<ConfigElement,ConfigElement>(this);
	
	public ConfigElement parent() {
		return _parent.getOtherEnd();
	}
	
	public void addChild(ConfigElement element) {
		if(element != null) {
			_children.add(element._parent);
		}
	}
	
	public void removeChild(ConfigElement element) {
		if(element != null) {
			_children.remove(element._parent);
		}
	}
	
	protected List<Element> unprocessedElements() {
		return new ArrayList<Element>(_unprocessed);
	}
	
	protected Class<? extends ConfigElement> getChildClass(String childName) {
		return _childClassMap.get(childName.toLowerCase());
	}
	
	/**
	 * This is inefficient (on purpose!) as it is done for every element while it
	 * is a class-level property. I don't want to use static code to make it efficient
	 * because config files are typically small.
	 */
	private void initChildCache() {
		_childClassMap = new HashMap<String, Class<? extends ConfigElement>>();
		_reverseChildClassMap = new HashMap<Class<?>, String>();
		for(Class<?> c : getClass().getClasses()) {
			String name = c.getName();
			name = Util.getLastPart(name.replace('$', '.'));
			String key = name.toLowerCase();
			Class<? extends ConfigElement> existing = _childClassMap.get(key);
			if((existing == null) || existing.isAssignableFrom(c)) {
				_childClassMap.put(key, (Class<? extends ConfigElement>) c);
				_reverseChildClassMap.put(c,key);
			}
		}
	}

	/**
	 * A map from lower case attribute names to the corresponding setter methods.
	 */
	private Map<String, Method> _attributeMethodMap;

	private List<Method> _attributeGetters;

	protected Method attributeSetter(String attributeName) {
		return _attributeMethodMap.get(attributeName.toLowerCase());
	}
	
	protected void initAttributeCache() {
		_attributeMethodMap = new HashMap<String, Method>();
		_attributeGetters = new ArrayList<Method>();
		Method[] methods = getClass().getMethods();
		for(Method method: methods) {
			String methodName = method.getName();
			if(methodName.startsWith("set")) {
				Type[] types = method.getGenericParameterTypes();
				if(types.length == 1 && types[0] instanceof Class) {
					Class<?> c = (Class<?>) types[0];
					if(isString(c)) {
						// cut of "set" from the start of the name
						String key = attributeKey(methodName);
						_attributeMethodMap.put(key, method);
					}
				}
			}
		}
		// Look for getters that have a corresponding setter.
		for(Method method: methods) {
			String methodName = method.getName();
			if(methodName.startsWith("get")) {
				Type[] types = method.getGenericParameterTypes();
				Type genericReturnType = method.getGenericReturnType();
				
				if(types.length == 0 && genericReturnType.equals(String.class)) {
					if(_attributeMethodMap.containsKey(attributeKey(methodName))) {
						_attributeGetters.add(method);
					}
				}				
			}
		}
	}

	protected boolean isString(Class<?> c) {
		return c.getCanonicalName().equals("java.lang.String");
	}

	protected String attributeKey(String methodName) {
		return methodName.toLowerCase().substring(3,methodName.length());
	}
	
	protected void processAttribute(Attr attribute) {
		try {
			String name = attribute.getName();
			String value = attribute.getValue();
			Method attributeSetter = attributeSetter(name);
			if(attributeSetter != null) {
				attributeSetter.invoke(this, value);
			}
		} catch (IllegalAccessException e) {
		  // We ignore this to stop errors in a config file from preventing loading the configuration file. 
			e.printStackTrace();
		} catch (InvocationTargetException e) {
		  // We ignore this to stop errors in a config file from preventing loading the configuration file. 
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ConfigElement> T createOrGetChild(Class<T> type)  {
		for(ConfigElement element: _children.getOtherEnds()) {
			if(type.isInstance(element)) {
				return (T) element;
			}
		}
		// If we reach this point, no element of the given type was found.
		return createChild(type);
	}
	
	public <T extends ConfigElement> T createOrUpdateChild(Class<T> type, Object object) throws ConfigException  {
		ConfigElement element = childFor(object);
		if(element == null) {
			element = createChild(type);
			
			element.setModelElement(object);
			addChild(element);
		}
		element.$update();
		return (T) element;
	}
	
	protected void $update() throws ConfigException {}
	
	public ConfigElement childFor(Object object) {
		for(ConfigElement element: _children.getOtherEnds()) {
			if(element.modelElement() == object) {
				return element;
			}
		}
		return null;
	}
	
	public void removeChildFor(Object object) {
		ConfigElement element = childFor(object);
		if(element != null) {
			element.disconnect();
		}		
	}
	
	public void disconnect() {
		_parent.clear();
		_children.apply(new SafeAction<ConfigElement>(ConfigElement.class) {
			@Override
			public void accept(ConfigElement object) throws Nothing {
				object.disconnect();
			}
		});
//		for(ConfigElement child: _children.getOtherEnds()) {
//			child.disconnect();
//		}
	}
	
	private Object _modelElement;
	
	public Object modelElement() {
		return _modelElement;
	}
	
	public void setModelElement(Object m) {
		_modelElement = m;
	}

}

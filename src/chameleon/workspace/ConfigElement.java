package chameleon.workspace;

import java.io.File;
import java.io.IOException;
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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import chameleon.util.Util;

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
		return _reverseChildClassMap.get(getClass());
	}
	
	protected void readFromXML(File xmlFile) throws ConfigException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = fac.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			read(doc.getDocumentElement()); 
		} catch (ParserConfigurationException | SAXException | IOException e) {
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
			
			StreamResult result =  new StreamResult(xmlFile);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new ConfigException(e);
		}
	}
	
	protected Element toElement(Document doc) {
		Element result = doc.createElement(nodeName());
		String text = $getText();
		if(text != null) {
			result.setTextContent(text);
		}
		// Add the attributes.
		for(Method method: _attributeGetters) {
			try {
				Attr attr = doc.createAttribute(attributeKey(method.getName()));
				String value = (String) method.invoke(this);
				attr.setValue(value);
				result.appendChild(attr);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		// Add the child elements.
		for(ConfigElement cfg: _children.getOtherEnds()) {
			result.appendChild(cfg.toElement(doc));
		}
		return result;
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
	protected Map<String, Class> _childClassMap;
	protected Map<Class, String> _reverseChildClassMap;

	protected void processChild(Element child) throws ConfigException {
		String name = child.getNodeName();
		Class childClass = getChildClass(name);
		if(childClass != null) {
				ConfigElement childConfig = createChild(childClass);
				addChild(childConfig);
				childConfig.read(child);
		} else {
			_unprocessed.add(child);
		}
	}

	private <T extends ConfigElement> T createChild(Class<T> childClass) {
		try {
		T childConfig;
		@SuppressWarnings("unused")
		boolean inner = childClass.isMemberClass();
		if(inner) {
			//				java.lang.reflect.Constructor[] cs = childClass.getConstructors();
			@SuppressWarnings("unchecked")
			java.lang.reflect.Constructor ctor = childClass.getConstructors()[0];
				childConfig = (T) ctor.newInstance(this);
		} else {
			childConfig = (T) childClass.getDeclaredConstructor().newInstance();
		}
		return childConfig;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
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
	
	protected Class getChildClass(String childName) {
		return _childClassMap.get(childName.toLowerCase());
	}
	
	/**
	 * This is inefficient (on purpose!) as it is done for every element while it
	 * is a class-level property. I don't want to use static code to make it efficient
	 * because config files are typically small.
	 */
	private void initChildCache() {
		_childClassMap = new HashMap<>();
		_reverseChildClassMap = new HashMap<>();
		for(Class c : getClass().getClasses()) {
			String name = c.getName();
			name = Util.getLastPart(name.replace('$', '.'));
			String key = name.toLowerCase();
			Class existing = _childClassMap.get(key);
			if((existing == null) || existing.isAssignableFrom(c)) {
				_childClassMap.put(key, c);
				_reverseChildClassMap.put(c,key);
			}
		}
	}

	/**
	 * A map from lower case attribute names to the corresponding setter methods.
	 */
	protected Map<String, Method> _attributeMethodMap;

	protected List<Method> _attributeGetters;

	protected Method attributeSetter(String attributeName) {
		return _attributeMethodMap.get(attributeName.toLowerCase());
	}
	
	protected void initAttributeCache() {
		_attributeMethodMap = new HashMap<String, Method>();
		_attributeGetters = new ArrayList<>();
		Method[] methods = getClass().getMethods();
		for(Method method: methods) {
			String methodName = method.getName();
			if(methodName.startsWith("set")) {
				Type[] types = method.getGenericParameterTypes();
				if(types.length == 1 && types[0] instanceof Class) {
					Class c = (Class) types[0];
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
				if(types.length == 0 && isString(method.getGenericReturnType().getClass())) {
					if(_attributeMethodMap.containsValue(attributeKey(methodName))) {
						_attributeGetters.add(method);
					}
				}				
			}
		}
	}

	protected boolean isString(Class c) {
		return c.getCanonicalName().equals("java.lang.String");
	}

	protected String attributeKey(String methodName) {
		return methodName.toLowerCase().substring(3,methodName.length());
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
	
	public <T extends ConfigElement> T createOrGetChild(Class<T> type)  {
		for(ConfigElement element: _children.getOtherEnds()) {
			if(type.isInstance(element)) {
				return (T) element;
			}
		}
		// If we reach this point, no element of the given type was found.
		return createChild(type);
	}
	
	public <T extends ConfigElement> T createOrUpdateChild(Class<T> type, Object object)  {
		ConfigElement element = childFor(object);
		if(element == null) {
			element = createChild(type);
			element.setModelElement(object);
			addChild(element);
		}
		element.$update();
		return (T) element;
	}
	
	protected abstract void $update();
	
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
		for(ConfigElement child: _children.getOtherEnds()) {
			child.disconnect();
		}
	}
	
	private Object _modelElement;
	
	public Object modelElement() {
		return _modelElement;
	}
	
	public void setModelElement(Object m) {
		_modelElement = m;
	}

}

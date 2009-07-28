package chameleon.core.language;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.association.Reference;
import org.rejuse.association.ReferenceSet;
import org.rejuse.association.Relation;
import org.rejuse.property.Property;
import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertySet;
import org.rejuse.property.PropertyUniverse;

import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupStrategyFactory;
import chameleon.core.namespace.RootNamespace;
import chameleon.core.property.PropertyRule;
import chameleon.core.type.TypeReference;
import chameleon.tool.ToolExtension;

/**
 * @author Marko van Dooren
 */

public abstract class Language implements PropertyUniverse<Element> {
	
	public Language(String name) {
		this(name, new LookupStrategyFactory());
	}
	
	public Language(String name, LookupStrategyFactory factory) {
		setName(name);
		setLookupStrategyFactory(factory);
		initProperties();
		initializePropertyRules();
	}
	
	/**
	 * Return the name of this language.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String getName() {
		return _name;
	}
	
	/**
	 * Return the default properties of the given element.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ (* The properties of all rules are added to the result.*)
   @ post (\forall PropertyRule rule; propertyRules().contains(rule);
   @         \result.containsAll(rule.properties(element)));
   @ (* Only the properties given by the property rules are in the result *);
   @ post (\forall Property<Element> p; \result.contains(p);
   @        \exists(PropertyRule rule; propertyRules().contains(rule);
   @           rule.properties(element).contains(p)));
   @*/
	public PropertySet<Element> defaultProperties(Element element) {
		PropertySet<Element> result = new PropertySet<Element>();
		for(PropertyRule rule:propertyRules()) {
			result.addAll(rule.properties(element));
		}
		return result;
	}
	
	/**
	 * Return the list of rule that determine the default properties of an element.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<PropertyRule> propertyRules() {
		return _propertyRules.getOtherEnds();
	}
	
  /**
   * Add all property rules in this method.
   */
	protected abstract void initializePropertyRules();
	
	/**
	 * Add a property rule to this language object.
	 * @param rule
	 */
 /*@
   @ public behavior
   @
   @ pre rule != null;
   @
   @ post propertyRules().contains(rule);
   @*/
	public void addPropertyRule(PropertyRule rule) {
		if(rule == null) {
			throw new ChameleonProgrammerException("adding a null property rule to a language");
		}
		_propertyRules.add(rule.languageLink());
	}
	
	/**
	 * Remove a property rule from this language object.
	 * @param rule
	 */
 /*@
   @ public behavior
   @
   @ pre rule != null;
   @
   @ post ! propertyRules().contains(rule);
   @*/
	public void removePropertyRule(PropertyRule rule) {
		if(rule == null) {
			throw new ChameleonProgrammerException("removing a null property rule to a language");
		}
		_propertyRules.remove(rule.languageLink());
	} 
	
	private OrderedReferenceSet<Language,PropertyRule> _propertyRules = new OrderedReferenceSet<Language,PropertyRule>(this);
	
	/**
	 * Set the name of this language.
	 * @param name
	 *        The new name of this language
	 */
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName() == name;
   @*/
	public void setName(String name) {
		_name = name;
	}
	
	private String _name;

	public RootNamespace defaultNamespace() {
        return (RootNamespace)_default.getOtherEnd();
    }

		/******************
     * TOOLEXTENSIONS *
     ******************/

	public final PropertyMutex<Element> SCOPE_MUTEX = new PropertyMutex<Element>();

	
    private static class MapWrapper<T> {  //todo iets voor rejuse?
        private Map<Class<? extends T>,T> myMap = new HashMap<Class<? extends T>,T>();

        public <S extends T> S get(Class<S> key) {
            return (S)myMap.get(key);
        }

        public <S extends T> void put(Class<? extends S> key, S value) {
            myMap.put(key,value);
        }

        public <S extends T> void remove(Class<S> key) {
            myMap.remove(key);
        }

        public Collection<T> values() {
            return myMap.values();
        }

        public <S extends T> boolean containsKey(Class<S> key) {
            return myMap.containsKey(key);
        }

        public boolean isEmpty() {
            return myMap.isEmpty();
        }
    }

    private MapWrapper<ToolExtension> toolExtensions = new MapWrapper<ToolExtension>();

    //private Map<Class<? extends ToolExtension>,? extends ToolExtension> toolExtensions = new HashMap<Class<? extends ToolExtension>,ToolExtension>();
    /*private <T extends ToolExtension> Map<Class<T>,T> getMap() {
        return (Map<Class<T>,T>)toolExtensions;
    }*/

    public <T extends ToolExtension> T getToolExtension(Class<? extends T> toolExtensionClass) {
        return toolExtensions.get(toolExtensionClass);//((Map<Class<T>,T>)getMap()).get(toolExtensionClass);
    }

    public <T extends ToolExtension> void removeToolExtension(Class<? extends T> toolExtensionClass) {
        T old = toolExtensions.get(toolExtensionClass);
        if ((old!=null) && (old.getLanguage()!=this)) {
            old.setLanguage(null, toolExtensionClass);
        }
        toolExtensions.remove(toolExtensionClass);
    }

    public <T extends ToolExtension> void setToolExtension(Class<? extends T> toolExtensionClass, T toolExtension) {
        T old = toolExtensions.get(toolExtensionClass);
        if (old!=toolExtension) {
            if ((toolExtension!=null) && (toolExtension.getLanguage()!=this)) {
                toolExtension.setLanguage(this, toolExtensionClass);
            }
            if (old!=null) {
                old.setLanguage(null, toolExtensionClass);
            }
            toolExtensions.put(toolExtensionClass, toolExtension);
        }
    }

    /**
     * Return the tool extensions attached to this language object.
     * @return
     */
   /*@
     @ public behavior
     @
     @ post \result != null; 
     @*/
    public Collection<ToolExtension> getToolExtensions() {
        return toolExtensions.values();
    }
    

    public <T extends ToolExtension> boolean hasToolExtension(Class<? extends T> toolExtensionClass) {
        return toolExtensions.containsKey(toolExtensionClass);
    }

    public boolean hasToolExtensions() {
        return ! toolExtensions.isEmpty();
    }

    /**************************************************************************
     *                                 PROPERTIES                             *
     **************************************************************************/
    
    /**
     * Return the properties that can be used for elements in this model.
     * 
     * For every class of properties, one object is in the set.
     * @return
     */
    public Set<Property<Element>> properties() {
      return new HashSet<Property<Element>>(_properties.getOtherEnds());
    }

    /**
     * Return the object representing the association between this language and the
     * properties to which it is attached.
     * 
     * DO NOT MODIFY THE RESULTING OBJECT. IT IS ACCESSIBLE ONLY BECAUSE OF THE 
     * VERY DUMB ACCESS CONTROL IN JAVA.
     */
    public ReferenceSet<PropertyUniverse<Element>,Property<Element>> propertyLink() {
      return _properties;
    }
    
    private ReferenceSet<PropertyUniverse<Element>,Property<Element>> _properties = new ReferenceSet<PropertyUniverse<Element>, Property<Element>>(this);
    
    /**
     * 
     * @param name
     * @return
     * @throws ChameleonProgrammerException
     *         There is no property with the given name.
     */
    public Property<Element> property(String name) throws ChameleonProgrammerException {
    	for(Property<Element> p: properties()) {
    		if(p.name().equals(name)) {
    			return p;
    		}
    	}
    	throw new ChameleonProgrammerException("Property with name: "+name+" not found.");
    }
    
    protected abstract void initProperties();
    
    /**************************************************************************
     *                           DEFAULT NAMESPACE                            *
     **************************************************************************/
    
    public void setDefaultNamespace(RootNamespace defaultNamespace) {
        _default.connectTo(defaultNamespace.parentLink());
    }

    private Reference _default = new Reference(this); //todo wegens setDefaultNamespace kan dit niet generisch worden gemaakt?

    /**
     * @return
     */
    public Relation defaultNamespaceLink() {
        return _default;
    }

    public LookupStrategyFactory lookupFactory() {
    	return _contextFactory;
    }
    
    protected void setLookupStrategyFactory(LookupStrategyFactory factory) {
    	_contextFactory = factory;
    }
    
    private LookupStrategyFactory _contextFactory;

}


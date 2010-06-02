package chameleon.core.language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.rejuse.association.Association;
import org.rejuse.association.MultiAssociation;
import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;
import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertySet;
import org.rejuse.property.PropertyUniverse;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupStrategyFactory;
import chameleon.core.namespace.RootNamespace;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.property.PropertyRule;
import chameleon.core.validation.Valid;
import chameleon.core.validation.ValidityRule;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.tool.Connector;
import chameleon.tool.Processor;

/**
 * A class representing a Chameleon language.
 *
 * The language object contains the default namespace, which is the entry point into the model.
 * 
 * The language object contains the properties that elements in that language can have. In addition, it contains
 * the property rule which define the default properties of elements. These rules are used if the explicitly declared
 * properties of an element say nothing about a certain property.
 * 
 * The language object contains the connectors and processors for a tool that allow that tool to work with that particular language. 
 * 
 * @author Marko van Dooren
 */

public abstract class Language implements PropertyUniverse<ChameleonProperty> {
	
	/**
	 * Initialize a new language with the given name.
	 * 
	 * @param name
	 *        The name of the language.
	 */
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post name().equals(name);
   @ post (* The lookup strategy factory is initialized to the default LookupStrategyFactory *);
   @*/
	public Language(String name) {
		this(name, new LookupStrategyFactory());
	}
	
	/**
	 * Initialize a new language with the given name and lookup strategy factory.
	 * 
	 * @param name
	 *        The name of the language.
	 */
 /*@
   @ public behavior
   @
   @ pre name != null;
   @ pre factory != null;
   @
   @ post name().equals(name);
   @ post lookupFactory() == factory;
   @*/
	public Language(String name, LookupStrategyFactory factory) {
		setName(name);
		setLookupStrategyFactory(factory);
		initializePropertyRules();
		SCOPE_MUTEX = new PropertyMutex<ChameleonProperty>();
	}
	
	public final Language clone() {
		Language result = cloneThis();
		result.cloneConnectorsFrom(this);
		result.cloneProcessorsFrom(this);
		return result;
	}
	
	protected abstract Language cloneThis();
	
	/**
	 * Return the name of this language.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String name() {
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
	public PropertySet<Element,ChameleonProperty> defaultProperties(Element element) {
		// FIXME: verify all dynamic properties? Check if result is TRUE or FALSE, and add property or
		//       property.inverse() respectively.
		PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
		for(PropertyRule rule:propertyRules()) {
			if(rule.elementType().isInstance(element)) {
			  result.addAll(rule.properties(element));
			}
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
	
	private OrderedMultiAssociation<Language,PropertyRule> _propertyRules = new OrderedMultiAssociation<Language,PropertyRule>(this);
	
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
	
	/*
	 * The name of this language.
	 */
	private String _name;

	/**
	 * Return the default namespace attached to this language. A language is always attached to a default namespace because a language
	 * may need access to predefined elements, which are somewhere in the model.
	 * @return
	 */
	public RootNamespace defaultNamespace() {
        return (RootNamespace)_default.getOtherEnd();
    }

	/**
	 * A property mutex for the scope property.
	 */
	public final PropertyMutex<ChameleonProperty> SCOPE_MUTEX;

	 /**************
    * CONNECTORS *
    **************/
	
    private static class MapWrapper<T> {
        private Map<Class<? extends T>,T> _map = new HashMap<Class<? extends T>,T>();

        public <S extends T> S get(Class<S> key) {
            return (S)_map.get(key);
        }
        
        public Set<Entry<Class<? extends T>,T>> entrySet() {
        	return _map.entrySet();
        }

        public <S extends T> void put(Class<? extends S> key, S value) {
            _map.put(key,value);
        }
        
        public void putAll(MapWrapper<T> other) {
        	_map.putAll(other._map);
        }

        public <S extends T> void remove(Class<S> key) {
            _map.remove(key);
        }

        public Collection<T> values() {
            return _map.values();
        }

        public <S extends T> boolean containsKey(Class<S> key) {
            return _map.containsKey(key);
        }

        public boolean isEmpty() {
            return _map.isEmpty();
        }
    }

    private static class ListMapWrapper<T> {
      private Map<Class<? extends T>,List<? extends T>> _map = new HashMap<Class<? extends T>,List<? extends T>>();

      public int size() {
      	return _map.size();
      }
      
      public Set<Class<? extends T>> keySet() {
      	return _map.keySet();
      }
      
      public Map<Class<? extends T>,List<? extends T>> map() {
      	return new HashMap<Class<? extends T>,List<? extends T>>(_map);
      }
      
      public <S extends T> List<S> get(Class<S> key) {
      	List<S> processors = (List<S>)_map.get(key);
      	if(processors == null) {
      		return new ArrayList<S>();
      	} else {
          return new ArrayList<S>(processors);
      	}
      }
      
      public void addAll(Map<Class<? extends T>,List<? extends T>> map) {
      	_map.putAll(map);
      }

      public <S extends T> void add(Class<S> key, S value) {
      	  List<S> list = (List<S>)_map.get(key);
      	  if(list == null) {
      	  	list = new ArrayList<S>();
      	  	_map.put(key, list);
      	  }
      	  list.add(value);
      }

      public <S extends T> void remove(Class<S> key, S value) {
      	_map.get(key).remove(key);
      }

      public Collection<List<? extends T>> values() {
          return _map.values();
      }

      public <S extends T> boolean containsKey(Class<S> key) {
          return _map.containsKey(key);
      }

      public boolean isEmpty() {
          return _map.isEmpty();
      }
  }

    private MapWrapper<Connector> _toolConnectors = new MapWrapper<Connector>();

    //private Map<Class<? extends ToolExtension>,? extends ToolExtension> toolExtensions = new HashMap<Class<? extends ToolExtension>,ToolExtension>();
    /*private <T extends ToolExtension> Map<Class<T>,T> getMap() {
        return (Map<Class<T>,T>)toolExtensions;
    }*/

    /**
     * Return the connector corresponding to the given connector interface.
     */
   /*@
     @ public behavior
     @
     @ pre connectorInterface != null;
     @*/
    public <T extends Connector> T connector(Class<T> connectorInterface) {
        return _toolConnectors.get(connectorInterface);//((Map<Class<T>,T>)getMap()).get(toolExtensionClass);
    }

    /**
     * Remove the connector corresponding to the given connector interface. The
     * bidirectional relation is kept in a consistent state.
     * 
     * @param <T>
     * @param connectorInterface
     */
   /*@
     @ public behavior
     @
     @ pre connectorInterface != null;
     @
     @ post connector(connectorInterface) == null;
     @*/
    public <T extends Connector> void removeConnector(Class<T> connectorInterface) {
        T old = _toolConnectors.get(connectorInterface);
        _toolConnectors.remove(connectorInterface);
        if (old!=null && old.language() == this) {
            old.setLanguage(null, connectorInterface);
        }
    }

    /**
     * Set the connector corresponding to the given connector interface. The bidirectional relation is 
     * kept in a consistent state.
     * 
     * @param <T>
     * @param connectorInterface
     * @param connector
     */
   /*@
     @ public behavior
     @
     @ pre connectorInterface != null;
     @ pre connector != null;
     @
     @ post connector(connectorInterface) == connector; 
     @*/
    public <T extends Connector> void setConnector(Class<T> connectorInterface, T connector) {
        T old = _toolConnectors.get(connectorInterface);
        if (old!=connector) {
            if ((connector!=null) && (connector.language()!=this)) {
                connector.setLanguage(this, connectorInterface);
            }
            // Clean up old backpointer
            if (old!=null) {
                old.setLanguage(null, connectorInterface);
            }
            // Either
            if(connector != null) {
            	// Add connector to map
              _toolConnectors.put(connectorInterface, connector);
            } else {
            	// Remove entry in map
            	_toolConnectors.remove(connectorInterface);
            }
        }
    }
    
  	public <S extends Connector> void cloneConnectorsFrom(Language from) {
  		for(Entry<Class<? extends Connector>, Connector> entry: from._toolConnectors.entrySet()) {
  			Class<S> key = (Class<S>) entry.getKey();
				S value = (S) entry.getValue();
				_toolConnectors.put(key, (S)value.clone());
  		}
  	}

    /**
     * Return all connectors attached to this language object.
     * @return
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post (\forall Connector c; ; \result.contains(c) == 
     @           (\exists Class<? extends Connector> connectorInterface;; connector(connectorInterface) == c)
     @      ); 
     @*/
    public Collection<Connector> connectors() {
        return _toolConnectors.values();
    }
    

    /**
     * Check if this language has a connector for the given connector type. Typically
     * the type is an interface or abstract class for a specific tool.
     */
   /*@
     @ public behavior
     @
     @ pre connectorInterface != null;
     @
     @ post \result == connector(connectorInterface) != null;
     @*/
    public <T extends Connector> boolean hasConnector(Class<T> connectorInterface) {
        return _toolConnectors.containsKey(connectorInterface);
    }

    /**
     * Check if this language object has any connectors.
     */
   /*@
     @ public behavior
     @
     @ post \result ==  
     @*/
    public boolean hasConnectors() {
        return ! _toolConnectors.isEmpty();
    }

    /**************
     * PROCESSORS *
     **************/
    
    private ListMapWrapper<Processor> _processors = new ListMapWrapper<Processor>();

    /**
     * Return the processors corresponding to the given processor interface.
     */
   /*@
     @ public behavior
     @
     @ post \result.equals(processorMap().get(connectorInterface));
     @*/
    public <T extends Processor> List<T> processors(Class<T> connectorInterface) {
      return _processors.get(connectorInterface);
    }

    /**
     * Remove the given processor. The
     * bidirection relation is kept in a consistent state.
     * 
     * @param <T>
     * @param connectorInterface
     */
   /*@
     @ public behavior
     @
     @ pre connectorInterface != null;
     @ pre processor != null;
     @
     @ post !processor(connectorInterface).contains(processor); 
     @*/
    public <T extends Processor> void removeProcessor(Class<T> connectorInterface, T processor) {
        List<T> list = _processors.get(connectorInterface);
        if (list!=null && list.contains(processor)) {
            processor.setLanguage(null, connectorInterface);
            list.remove(processor);
        }
    }


    /**
     * Add the given processor to the list of processors correponding to the given connector interface. 
     * The bidirectional relation is kept in a consistent state.
     * 
     * @param <T>
     * @param connectorInterface
     * @param connector
     */
   /*@
     @ public behavior
     @
     @ pre connectorInterface != null;
     @ pre processor != null;
     @
     @ post processor(connectorInterface).contains(processor); 
     @*/
    public <T extends Processor> void addProcessor(Class<T> connectorInterface, T processor) {
      _processors.add(connectorInterface, processor);
      if(processor.language() != this) {
      	processor.setLanguage(this, connectorInterface);
      }
    }

    /**
     * Copy the processor mapping from the given language to this language.
     */
   /*@
     @ public behavior
     @
     @ post (\forall Class<? extends Processor> cls; from.processorMap().containsKey(cls);
     @         processors(cls).containsAll(from.processorMap().valueSet());
     @*/
  	public <S extends Processor> void cloneProcessorsFrom(Language from) {
  		for(Entry<Class<? extends Processor>, List<? extends Processor>> entry: from.processorMap().entrySet()) {
  			Class<S> key = (Class<S>) entry.getKey();
				List<S> value = (List<S>) entry.getValue();
				for(S processor: value) {
				  _processors.add(key, (S)processor.clone());
				}
  		}
  	}
  	
  	/**
  	 * Return the mapping of classes/interfaces to the processors of that kind.
  	 */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
  	public Map<Class<? extends Processor>, List<? extends Processor>> processorMap() {
  		return _processors.map();
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
    public Set<ChameleonProperty> properties() {
      return new HashSet<ChameleonProperty>(_properties.getOtherEnds());
    }

    /**
     * Return the object representing the association between this language and the
     * properties to which it is attached.
     * 
     * DO NOT MODIFY THE RESULTING OBJECT. IT IS ACCESSIBLE ONLY BECAUSE OF THE 
     * VERY DUMB ACCESS CONTROL IN JAVA.
     */
    public MultiAssociation<Language,ChameleonProperty> propertyLink() {
      return _properties;
    }
    
    private MultiAssociation<Language,ChameleonProperty> _properties = new MultiAssociation<Language, ChameleonProperty>(this);
    
    /**
     * 
     * @param name
     * @return
     * @throws ChameleonProgrammerException
     *         There is no property with the given name.
     */
    public ChameleonProperty property(String name) throws ChameleonProgrammerException {
    	for(ChameleonProperty p: properties()) {
    		if(p.name().equals(name)) {
    			return p;
    		}
    	}
    	throw new ChameleonProgrammerException("Property with name: "+name+" not found.");
    }
    
    /**************************************************************************
     *                           DEFAULT NAMESPACE                            *
     **************************************************************************/
    
    public void setDefaultNamespace(RootNamespace defaultNamespace) {
        _default.connectTo(defaultNamespace.languageLink());
    }

    private SingleAssociation<Language,RootNamespace> _default = new SingleAssociation<Language,RootNamespace>(this); //todo wegens setDefaultNamespace kan dit niet generisch worden gemaakt?

    /**
     * @return
     */
    public Association defaultNamespaceLink() {
        return _default;
    }

    public LookupStrategyFactory lookupFactory() {
    	return _contextFactory;
    }
    
    protected void setLookupStrategyFactory(LookupStrategyFactory factory) {
    	_contextFactory = factory;
    }
    
    private LookupStrategyFactory _contextFactory;

    /**
		 * Returns true if the given character is a valid character
		 * for an identifier.
		 */
		public abstract boolean isValidIdentifierCharacter(char character);
		
		
		
		
		
		/**
		 * Return the list of rule that determine the language specific validity conditions of an element.
		 * @return
		 */
	 /*@
	   @ public behavior
	   @
	   @ post \result != null;
	   @*/
		public List<ValidityRule> validityRules() {
			return _validityRules.getOtherEnds();
		}
		
	  /**
	   * Add all property rules in this method.
	   */
		protected abstract void initializeValidityRules();
		
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
		public void addValidityRule(ValidityRule rule) {
			if(rule == null) {
				throw new ChameleonProgrammerException("adding a null validity rule to a language");
			}
			_validityRules.add(rule.languageLink());
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
		public void removeValidityRule(ValidityRule rule) {
			if(rule == null) {
				throw new ChameleonProgrammerException("removing a null validity rule to a language");
			}
			_validityRules.remove(rule.languageLink());
		} 
		
		public VerificationResult verify(Element element) {
			VerificationResult result = Valid.create();
			for(ValidityRule rule: validityRules()) {
				if(rule.elementType().isInstance(element)) {
				  result = result.and(rule.verify(element));
				}
			}
			return result;
		}
		
		private OrderedMultiAssociation<Language,ValidityRule> _validityRules = new OrderedMultiAssociation<Language,ValidityRule>(this);
		
		
		/**
		 * Flush the caches kept by this language. Caches of model elements are flushed separately. The default behavior is to do nothing.
		 */
		public void flushCache() {
			
		}

}


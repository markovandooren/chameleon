package org.aikodi.chameleon.core.language;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupContextFactory;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.PropertyRule;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.core.validation.VerificationRule;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.plugin.LanguagePlugin;
import org.aikodi.chameleon.plugin.LanguageProcessor;
import org.aikodi.chameleon.plugin.PluginContainerImpl;
import org.aikodi.chameleon.plugin.ProcessorContainer;
import org.aikodi.rejuse.association.MultiAssociation;
import org.aikodi.rejuse.association.OrderedMultiAssociation;
import org.aikodi.rejuse.junit.Revision;
import org.aikodi.rejuse.property.Property;
import org.aikodi.rejuse.property.PropertyMutex;
import org.aikodi.rejuse.property.PropertySet;

import java.util.*;
import java.util.Map.Entry;

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

public abstract class LanguageImpl extends PluginContainerImpl<LanguagePlugin> implements Language {
	
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
	public LanguageImpl(String name, Revision version) {
		this(name, new LookupContextFactory(),version);
	}
	
	protected final StaticChameleonProperty OVERRIDABLE;
	
	protected final ChameleonProperty REFINABLE;
	
	
	protected final StaticChameleonProperty INHERITABLE;

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
	public LanguageImpl(String name, LookupContextFactory factory, Revision version) {
  	OVERRIDABLE = add(new StaticChameleonProperty("overridable",Declaration.class));
  	REFINABLE = add(new StaticChameleonProperty("refinable", Declaration.class));
  	INHERITABLE = add(new StaticChameleonProperty("inheritable",Declaration.class));
    OVERRIDABLE().addImplication(REFINABLE());
    REFINABLE().addImplication(INHERITABLE());
		setName(name);
		setLookupStrategyFactory(factory);
		initializePropertyRules();
		initializeValidityRules();
		SCOPE_MUTEX = new PropertyMutex<ChameleonProperty>();
		_version = version;
	}
	
	@Override
   public Revision version() {
		return _version;
	}
	
	private Revision _version;
	
//	public final Language clone() {
//		Language result = cloneThis();
//		result.clonePluginsFrom(this);
//		result.cloneProcessorsFrom(this);
//		return result;
//	}
	
//	protected abstract Language cloneThis();
	
	/**
	 * Return the name of this language.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	@Override
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
	@Override
   public PropertySet<Element,ChameleonProperty> defaultProperties(Element element,PropertySet<Element,ChameleonProperty> explicit) {
		// FIXME: verify all dynamic properties? Check if result is TRUE or FALSE, and add property or
		//       property.inverse() respectively.
		PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
		for(PropertyRule rule:propertyRules()) {
			if(rule.elementType().isInstance(element)) {
			  result.addAll(rule.properties(element,explicit));
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
	@Override
   public List<PropertyRule> propertyRules() {
		return _propertyRules.getOtherEnds();
	}
	
  /**
   * Add all property rules in this method.
   */
	protected void initializePropertyRules() {
	}
	
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
	@Override
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
	@Override
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
	@Override
   public void setName(String name) {
		_name = name;
	}
	
	/*
	 * The name of this language.
	 */
	private String _name;

	/**
	 * A property mutex for the scope property.
	 */
	public final PropertyMutex<ChameleonProperty> SCOPE_MUTEX;

	@Override
   public final PropertyMutex<ChameleonProperty> SCOPE_MUTEX() {
		return SCOPE_MUTEX;
	}
	
    /**************
     * PROCESSORS *
     **************/
    
    private ListMapWrapper<LanguageProcessor> _processors = new ListMapWrapper<LanguageProcessor>();

    /**
     * Return the processors corresponding to the given processor interface.
     */
   /*@
     @ public behavior
     @
     @ post \result.equals(processorMap().get(connectorInterface));
     @*/
    @Override
   public <T extends LanguageProcessor> List<T> processors(Class<T> connectorInterface) {
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
    @Override
   public <T extends LanguageProcessor> void removeProcessor(Class<T> connectorInterface, T processor) {
        List<T> list = _processors.get(connectorInterface);
        if (list!=null && list.contains(processor)) {
            processor.setContainer(null, connectorInterface);
            list.remove(processor);
        }
    }


    /**
     * Add the given processor to the list of processors corresponding to the given connector interface.
     * The bidirectional relation is kept in a consistent state.
     * 
     * @param <K> The type of the key.
	 * @param <V> The type of the value.
     * @param keyInterface The interface under which the processor should be looked up.
     * @param processor The process to add.
     */
   /*@
     @ public behavior
     @
     @ pre connectorInterface != null;
     @ pre processor != null;
     @
     @ post processor(connectorInterface).contains(processor); 
     @*/
    @Override
    public <K extends LanguageProcessor, V extends K> void addProcessor(Class<K> keyInterface, V processor) {
      _processors.add(keyInterface, processor);
      if(processor.container() != this) {
      	processor.setContainer(this, keyInterface);
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
    @Override
  	public void cloneProcessorsFrom(ProcessorContainer<LanguageProcessor> from) {
  		for(Entry<Class<? extends LanguageProcessor>, List<? extends LanguageProcessor>> entry: from.processorMap().entrySet()) {
  			Class<LanguageProcessor> key = (Class<LanguageProcessor>) entry.getKey();
				List<LanguageProcessor> value = (List<LanguageProcessor>) entry.getValue();
				for(LanguageProcessor processor: value) {
				  _processors.add(key, (LanguageProcessor)processor.clone());
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
  	@Override
   public Map<Class<? extends LanguageProcessor>, List<? extends LanguageProcessor>> processorMap() {
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
    @Override
   public Set<ChameleonProperty> properties() {
      return Collections.unmodifiableSet(_properties);
    }

    /**
     * Return the object representing the association between this language and the
     * properties to which it is attached.
     * 
     * DO NOT MODIFY THE RESULTING OBJECT. IT IS ACCESSIBLE ONLY BECAUSE OF THE 
     * VERY DUMB ACCESS CONTROL IN JAVA.
     */
    @Override
   public MultiAssociation<Language,ChameleonProperty> propertyLink() {
      throw new RuntimeException();
//      return _properties;
    }
    
    //private MultiAssociation<Language,ChameleonProperty> _properties = new MultiAssociation<Language, ChameleonProperty>(this);
    
    private Set<ChameleonProperty> _properties = new HashSet<>();
    /**
     * 
     * @param name
     * @return
     * @throws ChameleonProgrammerException
     *         There is no property with the given name.
     */
    @Override
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
    
    @Override
   public LookupContextFactory lookupFactory() {
    	return _contextFactory;
    }
    
    protected void setLookupStrategyFactory(LookupContextFactory factory) {
    	_contextFactory = factory;
    }
    
    private LookupContextFactory _contextFactory;

    /**
		 * Returns true if the given character is a valid character
		 * for an identifier.
		 */
  	@Override
   public boolean isValidIdentifierCharacter(char character) {
  		return true;
  	}

		
		
		
		
		
		/**
		 * Return the list of rule that determine the language specific validity conditions of an element.
		 * @return
		 */
	 /*@
	   @ public behavior
	   @
	   @ post \result != null;
	   @*/
		@Override
      public List<VerificationRule> validityRules() {
			return _validityRules.getOtherEnds();
		}
		
	  /**
	   * Add all property rules in this method.
	   */
		protected void initializeValidityRules() {
		}
		
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
		@Override
      public void addValidityRule(VerificationRule rule) {
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
		@Override
      public void removeValidityRule(VerificationRule rule) {
			if(rule == null) {
				throw new ChameleonProgrammerException("removing a null validity rule to a language");
			}
			_validityRules.remove(rule.languageLink());
		} 
		
		@Override
      public Verification verify(Element element) {
			Verification result = Valid.create();
			for(VerificationRule rule: validityRules()) {
				if(rule.elementType().isInstance(element)) {
				  result = result.and(rule.verify(element));
				}
			}
			return result;
		}
		
		private OrderedMultiAssociation<Language,VerificationRule> _validityRules = new OrderedMultiAssociation<Language,VerificationRule>(this);
		
		
		/**
		 * Flush the caches kept by this language. Caches of model elements are flushed separately. The default behavior is to do nothing.
		 */
		@Override
    public void flushCache() {
		  for(Property property: _properties) {
		    property.flushCache();
		  }
		}

	    protected <T extends ChameleonProperty> T add(T t) {
	      _properties.add(t);
	      _properties.add(t.inverse());
	      return t;
	    }

	@Override
	public StaticChameleonProperty OVERRIDABLE() {
		return OVERRIDABLE;
	}

	@Override
	public ChameleonProperty REFINABLE() {
		return REFINABLE;
	}

	@Override
	public StaticChameleonProperty INHERITABLE() {
		return INHERITABLE;
	}
}

